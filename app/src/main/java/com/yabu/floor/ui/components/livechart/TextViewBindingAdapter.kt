package com.yabu.floor.ui.components.livechart

import android.widget.TextView
import androidx.databinding.BindingAdapter

/**
 * Set the [TextView] isSelected property.
 */
@BindingAdapter("isSelected")
fun setIsSelected(view: TextView, isSelected: Boolean) {
    view.isSelected = isSelected
}