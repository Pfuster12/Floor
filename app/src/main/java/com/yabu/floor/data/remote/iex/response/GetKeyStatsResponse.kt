package com.yabu.floor.data.remote.iex.response

import com.yabu.floor.data.model.iex.KeyStats

data class GetKeyStatsResponse(
    val avg10Volume: Double?,
    val avg30Volume: Double?,
    val beta: Double?,
    val companyName: String?,
    val day200MovingAvg: Double?,
    val day30ChangePercent: Double?,
    val day50MovingAvg: Double?,
    val day5ChangePercent: Double?,
    val dividendYield: Double?,
    val employees: Int?,
    val exDividendDate: String?,
    val float: Long?,
    val marketcap: Long?,
    val maxChangePercent: Double?,
    val month1ChangePercent: Double?,
    val month3ChangePercent: Double?,
    val month6ChangePercent: Double?,
    val nextDividendDate: String?,
    val nextEarningsDate: String?,
    val peRatio: Double?,
    val sharesOutstanding: Long?,
    val ttmDividendRate: Double?,
    val ttmEPS: Double?,
    val week52change: Double?,
    val week52high: Double?,
    val week52low: Double?,
    val year1ChangePercent: Double?,
    val year2ChangePercent: Double?,
    val year5ChangePercent: Double?,
    val ytdChangePercent: Double?) {

    fun map(symbol: String): KeyStats {
        return KeyStats(
            symbol = symbol,
            marketCap = marketcap?.toDouble() ?: 0.0,
            peRatio = peRatio ?: 0.0,
            dividendYield = (dividendYield ?: 0.0).times(100.0),
            eps = ttmEPS ?: 0.0,
            day200MovingAvg = day200MovingAvg ?: 0.0,
            day50MovingAvg = day50MovingAvg ?: 0.0,
            nextDividendDate = nextDividendDate ?: "N/A",
            nextEarningsDate = nextEarningsDate ?: "N/A",
            week52change = (week52change ?: 0.0)*100,
            week52high = week52high ?: 0.0,
            week52low = week52low ?: 0.0,
            ytdChangePercent = (ytdChangePercent ?: 0.0)*100
        )
    }
}