package com.yabu.floor.ui.components.clippedimageview

import androidx.databinding.BindingAdapter

@BindingAdapter("source")
fun setSource(view: ClippedImageView, source: String?) {
    if (source == null) {
        return
    }
    view.loadImage(source)
}