package com.yabu.floor.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableField
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.yabu.floor.data.Status
import com.yabu.floor.data.model.portfolio.Portfolio
import com.yabu.floor.data.model.portfolio.PortfolioItem
import com.yabu.floor.databinding.FragmentIndicesBinding
import com.yabu.floor.ui.settings.features.Feature
import com.yabu.floor.ui.settings.features.FeatureToggleHelper
import com.yabu.floor.utils.OnRecyclerViewItemClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class IndicesFragment : Fragment(), OnRecyclerViewItemClick {
    private val logtag = this::class.simpleName

    @Inject
    lateinit var featureToggle: FeatureToggleHelper

    private lateinit var binding: FragmentIndicesBinding

    private val viewModel: HomeViewModel by viewModels()

    private var resourceStatus = ObservableField(Status.LOADING)

    private val portfolioItems: MutableList<PortfolioItem> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        binding = FragmentIndicesBinding.inflate(inflater, container, false)

        if (featureToggle.isActivated(Feature.INDICES_LIST)) {
            binding.indexPortfolio.visibility = View.VISIBLE
            binding.indexPortfolio.adapter = IndexRecyclerViewAdapter(portfolioItems,
                this@IndicesFragment)
            getPortfolioItems()
        }

        return binding.root
    }

    private fun getPortfolioItems() {
        lifecycleScope.launch {
            val res = viewModel.getPortfolioItems(Portfolio.INDEX_ID)
            resourceStatus.set(Status.SUCCESS)

            portfolioItems.clear()
            portfolioItems.addAll(res)

            // Once we have the portfolio tickers, we can send out calls to
            // fetch the quotes and historical data for the chart.
            portfolioItems.map { it.ticker.id }.apply {
                getBatchQuotes(this)
                getPortfolioChartHistoricalDatasets(this)
            }
        }
    }

    private fun getBatchQuotes(symbols: List<String>) {
        lifecycleScope.launch {
            val res = viewModel.getBatchQuotes(symbols)
            when (res.status) {
                Status.SUCCESS -> {
                    portfolioItems.forEach { item ->
                        item.quote = res.data?.find {
                            it.symbol == item.ticker.id
                        }
                    }
                    binding.indexPortfolio.adapter
                        ?.notifyItemRangeChanged(0, portfolioItems.size)
                }
                Status.ERROR ->
                    Log.d(logtag, "Error fetching batch quotes: ${res.message}")
                Status.LOADING -> { }
            }
        }
    }

    private fun getPortfolioChartHistoricalDatasets(symbols: List<String>) {
        lifecycleScope.launch {
            val res = viewModel.getPortfolioChartHistoricalDatasets(symbols)
            when (res.status) {
                Status.SUCCESS -> {
                    portfolioItems.forEach { item ->
                        val chartHistoricalData = res.data?.find {
                            it.symbol == item.ticker.id
                        }

                        item.intradayData = chartHistoricalData?.filterIntraday()
                        item.historicalData = chartHistoricalData?.filterNonIntraday()
                    }

                    binding.indexPortfolio.adapter
                        ?.notifyItemRangeChanged(0, portfolioItems.size)
                }
                Status.ERROR ->
                    Log.d(logtag, "Error fetching batch datasets: ${res.message}")
                Status.LOADING -> {
                }
            }
        }
    }

    override fun onItemClick(v: View, position: Int) {

    }
}