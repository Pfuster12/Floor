package com.yabu.floor.utils

import com.yabu.floor.R
import java.util.*
import kotlin.math.absoluteValue

object NumberFormatUtils {
    private const val MILLION = 1000000L
    private const val BILLION = 1000000000L
    private const val TRILLION = 1000000000000L

    fun formatLargePrice(value: Double?): String {
        if (value == null) {
            return "N/A"
        }
        return when {
            value.absoluteValue < MILLION -> "$%.0f".format(value)
            value.absoluteValue < BILLION -> "$%.2f".format(value / MILLION) + "M"
            value.absoluteValue < TRILLION -> "$%.2f".format(value / BILLION) + "B"
            else -> "$%.2f".format(value / TRILLION) + "T"
        }
    }

    fun formatLargeNumber(value: Double?): String {
        if (value == null) {
            return "N/A"
        }
        return when {
            value.absoluteValue < MILLION -> "%.0f".format(value)
            value.absoluteValue < BILLION -> "%.2f".format(value / MILLION) + "M"
            value.absoluteValue < TRILLION -> "%.2f".format(value / BILLION) + "B"
            else -> "%.2f".format(value / TRILLION) + "T"
        }
    }

    fun getPercentStringResource(change: Double): Int {
        return if (change > 0.0) {
            R.string.positive_percent
        } else {
            R.string.negative_percent
        }
    }

    fun getLocalCurrencySymbol() =  when (Locale.getDefault().country) {
        Locale.UK.country -> Currency.getInstance(Locale.UK).symbol
        Locale.US.country -> Currency.getInstance(Locale.US).symbol
        Locale.GERMANY.country -> Currency.getInstance(Locale.GERMANY).symbol
        "ES" -> Currency.getInstance(Locale.GERMANY).symbol
        else -> Currency.getInstance(Locale.US).symbol
    }
}