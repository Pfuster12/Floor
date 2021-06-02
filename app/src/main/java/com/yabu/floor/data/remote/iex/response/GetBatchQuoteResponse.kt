package com.yabu.floor.data.remote.iex.response

import com.yabu.floor.data.model.iex.Quote
import com.yabu.floor.data.remote.Mappable

class GetBatchQuoteResponse : HashMap<String, BatchQuoteMap>(), Mappable<List<Quote>> {
    override fun map(): List<Quote> {
        return this.values.toList().map { map ->
            map.quote.run {
                Quote(symbol,
                    change,
                    changePercent,
                    close,
                    closeTime,
                    companyName,
                    extendedChange ?: 0.0,
                    extendedChangePercent ?: 0.0,
                    extendedPrice ?: 0.0,
                    extendedPriceTime ?: 0,
                    isUSMarketOpen,
                    latestPrice,
                    latestSource,
                    latestTime,
                    latestUpdate,
                    latestVolume,
                    peRatio,
                    volume,
                    week52High,
                    week52Low,
                    ytdChange)
            }
        }
    }
}

data class BatchQuoteMap(val quote: GetQuoteResponse)