package com.yabu.floor.ui.components.livechart

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.yabu.floor.R
import com.yabu.floor.data.Status
import com.yabu.floor.data.model.iex.HistoricalDataItem
import com.yabu.floor.data.remote.iex.request.IEXChartRange
import com.yabu.floor.databinding.FragmentLiveChartBinding
import com.yabu.livechart.model.DataPoint
import com.yabu.livechart.model.Dataset
import com.yabu.livechart.view.LiveChart
import com.yabu.livechart.view.LiveChartStyle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Displays a reusable [Fragment] displaying a LiveChart with dataset bounds selectors
 * to chart a ticker's historical data.
 */
@AndroidEntryPoint
class LiveChartFragment : Fragment(),
    // The Bounds selector listener
    BoundsSelectorView.OnBoundsListener,
    // Listen to the touch events on the LiveChart
    LiveChart.OnTouchCallback {
    private val logtag = this::class.simpleName

    private val coroutine = CoroutineScope(Dispatchers.Main + Job())

    private lateinit var binding: FragmentLiveChartBinding

    private val viewModel: LiveChartViewModel by viewModels()

    private var symbol: String = ""

    private var resourceStatus = ObservableField(Status.LOADING)

    private var liveChartTouchListener: LiveChart.OnTouchCallback? = null

    private val liveChartStyle by lazy {
        LiveChartStyle().apply {
            try {
                guideLineColor = requireContext().getColor(R.color.guidelines)
                overlayCircleColor = requireContext().getColor(R.color.colorOnSurface)
                overlayLineColor = requireContext().getColor(R.color.colorOnSurfaceVariant)
                positiveColor = requireContext().getColor(R.color.green)
                positiveFillColor = requireContext().getColor(R.color.green)
                negativeColor = requireContext().getColor(R.color.red)
                negativeFillColor = requireContext().getColor(R.color.red)
                textColor = requireContext().getColor(R.color.colorOnSurfaceVariant)
            } catch (e: Exception) {
                Log.e(logtag, "Error init livechart style ${e.message}")
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_live_chart,
            container,
            false)
        binding.apply {
            status = this@LiveChartFragment.resourceStatus
            livechartLiveChart.setOnTouchCallbackListener(this@LiveChartFragment)
            livechartBounds.addOnBoundsListener(this@LiveChartFragment)
        }

        return binding.root
    }

    /**
     * Set the symbol this fragment looks up historical data for.
     * @param symbol
     */
    fun setSymbol(symbol: String?) {
        if (symbol == null) {
            return
        }
        this.symbol = symbol

        // get historical data for the default day bucket.
        onBoundsChanged(ChartBounds.ONE_DAY)
    }

    fun setLiveChartTouchListener(listener: LiveChart.OnTouchCallback) {
        liveChartTouchListener = listener
    }

    private fun getChartHistoricalDataset(symbol: String,
                             range: IEXChartRange,
                             chartInterval: Int?,
                             chartLast: Int?,
                             includeToday: Boolean? = false,
                             chartCloseOnly: Boolean? = true) {
        coroutine.launch {
            binding.livechartError.visibility = View.GONE
            binding.livechartLoader.visibility = View.VISIBLE
            binding.livechartLiveChart.visibility = View.INVISIBLE
            val res = viewModel.getChartHistoricalDataset(symbol,
                range,
                chartInterval,
                chartLast,
                includeToday,
                chartCloseOnly)

            resourceStatus.set(res.status)
            when (res.status) {
                Status.SUCCESS -> {
                    try {
                        val intradayData = res.data?.filterIntraday()
                        val historicalData = res.data?.filterNonIntraday()

                        setLiveChart(intradayData, historicalData)
                    } catch (e: Exception) {
                        Log.e(logtag, e.message ?: "")
                    }
                }
                Status.LOADING -> {}
                Status.ERROR -> {
                    binding.livechartError.visibility = View.VISIBLE
                    Log.d(logtag, "Error fetching chart data: ${res.message}")
                }
            }
        }
    }

    private fun setLiveChart(intradayData: List<HistoricalDataItem>?,
                             historicalData: List<HistoricalDataItem>?) {
        val historicalDataset = Dataset(historicalData?.mapIndexedNotNull { index, it ->
            if (it.close == null && it.average == null) {
                return@mapIndexedNotNull null
            }

            DataPoint(index.toFloat(),
                if (it.close == null) {
                    it.average!!.toFloat()
                } else {
                    it.close.toFloat()
                })
        }?.toMutableList() ?: mutableListOf())

        val intradayDataset = Dataset(intradayData?.mapIndexedNotNull { index, it ->
            if (it.close == null && it.average == null) {
                return@mapIndexedNotNull null
            }

            DataPoint(index.toFloat() + (historicalData?.size ?: 0).toFloat(),
                if (it.close == null) {
                    it.average!!.toFloat()
                } else {
                    it.close.toFloat()
                })
        }?.toMutableList() ?: mutableListOf())

        if (historicalDataset.points.isNotEmpty()
            && intradayDataset.points.isNotEmpty()) {
            intradayDataset.points.add(0,
                DataPoint(
                    x = (historicalDataset.points.size - 1).toFloat(),
                    y = historicalDataset.points.last().y
                )
            )
        }

        if (intradayDataset.points.size == 0) {
            binding.livechartLiveChart.setDataset(historicalDataset)
                // reset second dataset.
                .setSecondDataset(Dataset(mutableListOf()))
        } else {
            binding.livechartLiveChart.setDataset(intradayDataset)
                .setSecondDataset(historicalDataset)
        }

        binding.livechartLiveChart.visibility = View.VISIBLE
        binding.livechartLoader.visibility = View.GONE

        binding.livechartLiveChart
            .setLiveChartStyle(liveChartStyle)
            .drawBaselineConditionalColor()
            .drawBaseline()
            .drawBaselineFromFirstPoint()
            .drawHorizontalGuidelines(steps = 4)
            .drawVerticalGuidelines(steps = 4)
            .drawLastPointLabel()
            .drawFill(withGradient = true)
            .drawYBounds()
            .drawDataset()
    }

    override fun onBoundsChanged(bounds: ChartBounds) {
        when (bounds) {
            ChartBounds.ONE_DAY -> getChartHistoricalDataset(
                symbol = symbol,
                range = IEXChartRange.FIVE_DAYS_10_MIN_INTERVALS,
                chartInterval = null,
                // This is important to only get the last two days of trading
                // in 10 minute intervals.
                chartLast = 39,
                chartCloseOnly = false,
                includeToday = true)

            ChartBounds.ONE_WEEK -> getChartHistoricalDataset(
                symbol = symbol,
                range = IEXChartRange.FIVE_DAYS_10_MIN_INTERVALS,
                chartInterval = 2,
                chartLast = null)

            ChartBounds.ONE_MONTH -> getChartHistoricalDataset(
                symbol = symbol,
                range = IEXChartRange.ONE_MONTH_30_MIN_INTERVALS,
                chartInterval = 5,
                chartLast = null)

            ChartBounds.THREE_MONTHS -> getChartHistoricalDataset(
                symbol = symbol,
                range = IEXChartRange.THREE_MONTHS,
                chartInterval = 1,
                chartLast = null)

            ChartBounds.ONE_YEAR -> getChartHistoricalDataset(
                symbol = symbol,
                range = IEXChartRange.ONE_YEAR,
                chartInterval = 6,
                chartLast = null)

            ChartBounds.FIVE_YEARS -> getChartHistoricalDataset(
                symbol = symbol,
                range = IEXChartRange.FIVE_YEARS,
                chartInterval = 20,
                chartLast = null)
        }
    }

    override fun onTouchCallback(point: DataPoint) {
        binding.livechartLiveChart
            .parent
            .requestDisallowInterceptTouchEvent(true)

        liveChartTouchListener?.onTouchCallback(point)
    }

    override fun onTouchFinished() {
        binding.livechartLiveChart
            .parent
            .requestDisallowInterceptTouchEvent(false)

        liveChartTouchListener?.onTouchFinished()
    }
}