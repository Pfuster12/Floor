package com.yabu.floor.ui.components.tickerlogo

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.ObservableField
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.yabu.floor.data.NetworkConstants
import com.yabu.floor.data.Status
import com.yabu.floor.databinding.TickerLogoBinding
import java.util.*

/**
 * [FrameLayout] subclass displaying a Stock Market Ticker Logo.
 * The layout displays the ticker in text if the logo image does not exist.
 */
class TickerLogoView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr)

    private val binding =
        TickerLogoBinding.inflate(LayoutInflater.from(context), this, false)

    var ticker: ObservableField<String> = ObservableField("")
    set(value) {
        binding.ticker = value
        field = value
    }

    var resourceStatus = ObservableField(Status.LOADING)

    init {
        binding.ticker = ticker
        binding.status = resourceStatus
        binding.tickerLogoImage.clipToOutline = true
        addView(binding.root)
    }

    @SuppressLint("CheckResult")
    fun loadLogo(): TickerLogoView {
        Glide.with(context)
            .load("${NetworkConstants.FLOORED_LOGOS}${ticker.get()?.toLowerCase(Locale.ROOT)}.png")
            .transition(DrawableTransitionOptions.withCrossFade())
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.tickerLogoText.visibility = View.VISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    binding.tickerLogoText.visibility = View.INVISIBLE
                    return false
                }

            })
            .into(binding.tickerLogoImage)
        return this
    }
}