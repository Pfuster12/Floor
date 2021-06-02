package com.yabu.floor.data.remote.iex.request

enum class IEXChartRange(val range: String) {
    MAX("max"),
    FIVE_YEARS("5y"),
    TWO_YEARS("2y"),
    ONE_YEAR("1y"),
    YTD("ytd"),
    SIX_MONTHS("6m"),
    THREE_MONTHS("3m"),
    ONE_MONTH("1m"),
    ONE_MONTH_30_MIN_INTERVALS("1mm"),
    FIVE_DAYS("5d"),
    FIVE_DAYS_10_MIN_INTERVALS("5dm"),
    ONE_DAY("1d")
}