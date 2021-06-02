package com.yabu.floor.data.remote.iex.response

import com.yabu.floor.data.model.iex.ChartHistoricalData
import com.yabu.floor.data.model.iex.HistoricalDataItem

class GetBatchChartHistoricalDataResponse : HashMap<String, HistoricalDataResponseItem>() {

    fun map(range: String, interval: Int): List<ChartHistoricalData> {
        return this.values.toList().mapIndexed { index, item ->
            val symbol = this.keys.toList()[index]
            ChartHistoricalData(
                compositeId = ChartHistoricalData.createCompositeId(symbol, range, interval),
                symbol = symbol,
                range = range,
                historicalData = item.chart.mapNotNull {
                    it.marketAverage ?: return@mapNotNull null

                    HistoricalDataItem(it.marketAverage,
                    it.close,
                    it.date ?: "",
                    it.label ?: "")
                })
        }
    }
}

data class HistoricalDataResponseItem(val chart: List<GetChartHistoricalDataResponseItem>)

data class GetChartHistoricalDataResponseItem(
    val marketAverage: Double?,
    val close: Double?,
    val date: String?,
    val label: String?
)