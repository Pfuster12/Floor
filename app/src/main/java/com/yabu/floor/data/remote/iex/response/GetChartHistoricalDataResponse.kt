package com.yabu.floor.data.remote.iex.response

import com.yabu.floor.data.model.iex.ChartHistoricalData
import com.yabu.floor.data.model.iex.HistoricalDataItem

class GetChartHistoricalDataResponse
    : ArrayList<GetChartHistoricalDataResponseItem>() {
    fun map(symbol: String, range: String, interval: Int): ChartHistoricalData {
        return ChartHistoricalData(
            compositeId = ChartHistoricalData.createCompositeId(symbol, range, interval),
            symbol = symbol,
            range = range,
            historicalData = this.map {
                HistoricalDataItem(it.marketAverage,
                    it.close,
                    it.date ?: "",
                    it.label ?: "")
            })
    }
}