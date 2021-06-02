package com.yabu.floor.data.model.iex

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
class ChartHistoricalData(@PrimaryKey
                          val compositeId: String,
                          val symbol: String,
                          val range: String,
                          val historicalData: List<HistoricalDataItem>,
                          val timestamp: Long = System.currentTimeMillis()) {
    companion object {

        /**
         * Helper to create the composite Id used as a PrimaryKey in the Room db.
         */
        fun createCompositeId(symbol: String, range: String, interval: Int?): String {
            return "$symbol-$range-${interval ?: 1}"
        }
    }

    fun filterIntraday(): List<HistoricalDataItem> {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .format(Date())

        return historicalData.filter {
            it.date == todayDate
        }
    }


    fun filterNonIntraday(): List<HistoricalDataItem> {
        val todayDate = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            .format(Date())

        return historicalData.filter {
            it.date != todayDate
        }
    }
}