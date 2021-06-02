package com.yabu.floor.data.model.iex

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Quote(@PrimaryKey val symbol: String,
                 val change: Double,
                 val changePercent: Double,
                 val close: Double,
                 val closeTime: Long,
                 val companyName: String,
                 val extendedChange: Double,
                 val extendedChangePercent: Double,
                 val extendedPrice: Double,
                 val extendedPriceTime: Long,
                 val isUSMarketOpen: Boolean,
                 val latestPrice: Double,
                 val latestSource: String,
                 val latestTime: String,
                 val latestUpdate: Long,
                 val latestVolume: Int,
                 val peRatio: Double,
                 val volume: Int,
                 val week52High: Double,
                 val week52Low: Double,
                 val ytdChange: Double)