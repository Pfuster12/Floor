package com.yabu.floor.ui.components.livechart

import androidx.lifecycle.ViewModel
import com.yabu.floor.data.remote.iex.request.IEXChartRange
import com.yabu.floor.data.repositories.IEXCloudRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LiveChartViewModel @Inject constructor(
    private val iexRepository: IEXCloudRepository
) : ViewModel() {

    suspend fun getChartHistoricalDataset(symbol: String,
                                  range: IEXChartRange,
                                  chartInterval: Int?,
                                  chartLast: Int?,
                                  includeToday: Boolean? = false,
                                  chartCloseOnly: Boolean? = true) =
            iexRepository.getChartHistoricalDataSet(symbol,
                    range,
                    includeToday,
                    chartInterval,
                    chartLast,
                    chartCloseOnly)
}