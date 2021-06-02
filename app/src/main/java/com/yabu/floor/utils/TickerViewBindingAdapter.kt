package com.yabu.floor.utils

import androidx.databinding.BindingAdapter
import com.robinhood.ticker.TickerView
import com.yabu.floor.R

@BindingAdapter("percentChange")
fun setPercentChange(view: TickerView, value: Double) {
    when {
        value > 0 -> view.textColor = view.context.getColor(R.color.green)

        value < 0 -> view.textColor = view.context.getColor(R.color.red)

        else -> view.textColor = view.context.getColor(R.color.colorOnSurface)
    }
}

@BindingAdapter("fontFace")
fun setFontFace(view: TickerView, value: String) {

}