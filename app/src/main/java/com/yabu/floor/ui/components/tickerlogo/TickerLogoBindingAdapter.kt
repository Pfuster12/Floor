package com.yabu.floor.ui.components.tickerlogo

import androidx.databinding.BindingAdapter

@BindingAdapter("ticker")
fun setTicker(view: TickerLogoView, ticker: String?) {
    if (ticker == null) {
        return
    }
    view.ticker.set(ticker)

    view.loadLogo()
}