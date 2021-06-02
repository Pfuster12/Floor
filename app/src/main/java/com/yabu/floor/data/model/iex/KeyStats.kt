package com.yabu.floor.data.model.iex

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class KeyStats(@PrimaryKey
                    val symbol: String,
                    val marketCap: Double,
                    val peRatio: Double,
                    val dividendYield: Double,
                    val eps: Double,
                    val day200MovingAvg: Double,
                    val day50MovingAvg: Double,
                    val nextDividendDate: String,
                    val nextEarningsDate: String,
                    val week52change: Double,
                    val week52high: Double,
                    val week52low: Double,
                    val ytdChangePercent: Double)