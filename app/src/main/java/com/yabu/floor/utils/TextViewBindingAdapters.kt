package com.yabu.floor.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.yabu.floor.R

@BindingAdapter("percentChange")
fun setPercentChange(view: TextView, value: Double) {
    when {
        value > 0 -> view.setTextColor(view.context.getColor(R.color.green))

        value < 0 -> view.setTextColor(view.context.getColor(R.color.red))

        else -> view.setTextColor(view.context.getColor(R.color.colorOnSurface))
    }
}
