package com.yabu.floor.ui.components.clippedimageview

import android.content.Context
import android.util.AttributeSet
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class ClippedImageView : androidx.appcompat.widget.AppCompatImageView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        clipToOutline = true
    }

    @Suppress("UNUSED")
    fun loadImage(src: String) {
        Glide.with(this)
            .load(src)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(this)
    }
}