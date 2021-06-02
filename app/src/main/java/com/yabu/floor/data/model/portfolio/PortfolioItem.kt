package com.yabu.floor.data.model.portfolio

import com.yabu.floor.data.model.iex.HistoricalDataItem
import com.yabu.floor.data.model.iex.Quote

data class PortfolioItem(var ticker: Ticker,
                         var quote: Quote? = null,
                         var intradayData: List<HistoricalDataItem>? = listOf(),
                         var historicalData: List<HistoricalDataItem>? = listOf())