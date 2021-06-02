package com.yabu.floor.ui.components.livechart

import android.content.Context
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.databinding.ObservableField
import com.yabu.floor.databinding.BoundsSelectorBinding

/**
 * Chart Bounds Selectors for 1 Day, 1 Week, 1 Month, 3 months, 1 Year and 5 Years.
 * Subclass of [FrameLayout].
 */
class BoundsSelectorView : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr)

    interface OnBoundsListener {
        fun onBoundsChanged(bounds: ChartBounds)
    }

    private var boundsListener: OnBoundsListener? = null

    private var chartBounds: ObservableField<ChartBounds> = ObservableField(ChartBounds.ONE_DAY)

    private val binding: BoundsSelectorBinding = BoundsSelectorBinding.inflate(
        LayoutInflater.from(context),
        this,
        false
    ).apply {
        bounds = this@BoundsSelectorView.chartBounds
    }

    init {
        addView(binding.root)
        bindBoundsSelectorViews()
    }

    /**
     * Add a listener for a change of [ChartBounds] in this view.
     * @param listener [OnBoundsListener]
     */
    fun addOnBoundsListener(listener: OnBoundsListener) {
        boundsListener = listener
    }

    /**
     * Binds all the bound selector buttons to select its [ChartBounds].
     */
    private fun bindBoundsSelectorViews() {
        val bounds = listOf(binding.boundsSelector1Day,
            binding.boundsSelector1Week,
            binding.boundsSelector1Month,
            binding.boundsSelector3Months,
            binding.boundsSelector1Year,
            binding.boundsSelector5Years)

        bounds.forEachIndexed { index, button ->
            button.setOnClickListener { v ->
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
                val boundsSelected = when (index) {
                    0 -> ChartBounds.ONE_DAY
                    1 -> ChartBounds.ONE_WEEK
                    2 -> ChartBounds.ONE_MONTH
                    3 -> ChartBounds.THREE_MONTHS
                    4 -> ChartBounds.ONE_YEAR
                    5 -> ChartBounds.FIVE_YEARS
                    else -> ChartBounds.ONE_DAY
                }
                chartBounds.set(boundsSelected)
                boundsListener?.onBoundsChanged(boundsSelected)
            }
        }
    }
}