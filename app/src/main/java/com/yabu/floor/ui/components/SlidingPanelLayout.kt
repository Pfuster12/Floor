package com.yabu.floor.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import kotlin.math.absoluteValue

class SlidingPanelLayout : FrameLayout {
    @Suppress("UNUSED")
    private val logtag = this::class.simpleName

    enum class PanelState {
        OPEN,
        SLIDING,
        CLOSED
    }

    private var touchSlop: Float = 40f

    // Screen properties
    private var screenWidth = 0f
    private var screenHeight = 0f

    // Original touch coordinates
    private var downRawX = 0f

    // Store previous X change in coordinates
    private var oldXChange = 0f

    // The panel views
    private var leftPanel: View? = null
    private var rightPanel: View? = null

    private var screed: View? = null

    // The most up to date panel state, OPEN, SLIDING or CLOSED
    private var rightPanelState = PanelState.CLOSED
    private var leftPanelState = PanelState.CLOSED

    // The old panel state to check if the panel was closed or open
    // when the touch event was started
    private var oldRightPanelState = PanelState.CLOSED
    private var oldLeftPanelState = PanelState.CLOSED

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context,
                attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context,
                attrs: AttributeSet,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        // Calculate the touch slop to intercept touch event.
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop.toFloat()*2

        // Grab the screen width and height
        val metrics = resources.displayMetrics
        screenWidth = metrics.widthPixels.toFloat()
        screenHeight = metrics.heightPixels.toFloat()
    }

    @Suppress("UNUSED")
    fun setLeftPanel(v: View) {
        leftPanel = v

        leftPanel?.x = -screenWidth
    }

    @Suppress("UNUSED")
    fun setRightPanel(v: View) {
        rightPanel = v
        // Init outside to the right of the screen
        rightPanel?.x = screenWidth
    }

    fun setScreed(v: View) {
        screed = v
    }

    override fun onInterceptTouchEvent(event: MotionEvent?): Boolean {
        // This method JUST determines whether we want to intercept the motion.
        // If we return true, onTouchEvent will be called and we do the actual
        // scrolling there.
        when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // grab details
                downRawX = event.rawX
            }
            MotionEvent.ACTION_MOVE -> {
                // calculate change in coordinates of finger move,
                (event.rawX - downRawX).also {
                    // store this X change to store for the next drag,
                    oldXChange = it
                }.let {
                    // check if it is bigger than a touch slop to intercept a drag
                    if (it.absoluteValue > touchSlop) return true
                }
            }
        }
        return false
    }

    /**
     * Once the event is intercepted handle it in this function
     */
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return when (event?.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                // get original coordinates
                downRawX = event.rawX
                true
            }

            MotionEvent.ACTION_MOVE -> {
                // calculate change in coordinates of finger move
                val changeX = event.rawX - downRawX
                // get the dragged value
                val dragChange = changeX - oldXChange

                // change the x position of the layout with the drag
                if (dragChange > 0) {
                    // Moving towards right side
                    rightPanel?.also {
                        if (rightPanelState == PanelState.SLIDING
                                || rightPanelState == PanelState.OPEN
                        ) {
                            when (it.x) {
                                in -screenWidth..screenWidth -> {
                                    it.x += dragChange
                                    screed?.alpha?.plus(1)
                                    rightPanelState = PanelState.SLIDING
                                }
                            }
                        }
                    }

                    if (rightPanelState != PanelState.SLIDING
                            && rightPanelState != PanelState.OPEN
                    ) {
                        leftPanel?.also {
                            when (it.x) {
                                in (-2*screenWidth)..0f -> {
                                    it.x += dragChange
                                    screed?.alpha?.plus(1f)
                                    leftPanelState = PanelState.SLIDING
                                }
                            }
                        }
                    }
                } else if (dragChange < 0) {
                    // Moving towards left side
                    if (leftPanelState != PanelState.SLIDING
                            && leftPanelState != PanelState.OPEN
                    ) {
                        rightPanel?.also {
                            when (it.x) {
                                in 0f..(screenWidth * 2) -> {
                                    it.x += dragChange
                                    screed?.alpha?.minus(0)
                                    rightPanelState = PanelState.SLIDING
                                }
                            }
                        }
                    }

                    leftPanel?.also {
                        if (leftPanelState == PanelState.SLIDING
                                || leftPanelState == PanelState.OPEN
                        ) {
                            when (it.x) {
                                in -screenWidth..screenWidth -> {
                                    it.x += dragChange
                                    screed?.alpha?.minus(0)
                                    leftPanelState = PanelState.SLIDING
                                }
                            }
                        }
                    }
                }

                oldXChange = changeX
                true
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                rightPanel?.also {
                    val openThreshold = screenWidth.minus(screenWidth * 0.33f)
                    val closingThreshold = screenWidth.minus(screenWidth * 0.8f)

                    when (oldRightPanelState) {
                        PanelState.OPEN -> {
                            when (it.x) {
                                // if the drawer is dragged more than halfway,
                                in closingThreshold..screenWidth -> {
                                    // animate it back out,
                                    it.animate().x(screenWidth).start()
                                    rightPanelState = PanelState.CLOSED
                                    oldRightPanelState = PanelState.CLOSED
                                }

                                // when it is not,
                                in -screenWidth..closingThreshold -> {
                                    screed?.animate()?.alpha(0f)
                                    it.animate().x(0f).start()
                                    rightPanelState = PanelState.OPEN
                                    oldRightPanelState = PanelState.OPEN
                                }

                            }
                        }

                        PanelState.CLOSED -> {
                            when (it.x) {
                                // if the drawer is dragged more than halfway,
                                in openThreshold..screenWidth -> {
                                    // animate it back out,
                                    it.animate().x(screenWidth).start()
                                    rightPanelState = PanelState.CLOSED
                                    oldRightPanelState = PanelState.CLOSED
                                }

                                // when it is not,
                                in -screenWidth..openThreshold -> {
                                    it.animate().x(0f).start()
                                    screed?.animate()?.alpha(0f)
                                    rightPanelState = PanelState.OPEN
                                    oldRightPanelState = PanelState.OPEN
                                }
                            }
                        }

                        PanelState.SLIDING -> {
                            when (it.x) {
                                // if the drawer is dragged more than halfway,
                                in closingThreshold..screenWidth -> {
                                    // animate it back out,
                                    it.animate().x(screenWidth).start()
                                    rightPanelState = PanelState.CLOSED
                                    oldRightPanelState = PanelState.CLOSED
                                }

                                // when it is not,
                                in -screenWidth..closingThreshold -> {
                                    screed?.animate()?.alpha(0f)
                                    it.animate().x(0f).start()
                                    rightPanelState = PanelState.OPEN
                                    oldRightPanelState = PanelState.OPEN
                                }
                            }
                        }
                    }
                }

                leftPanel?.also {
                    val openThreshold = -screenWidth * 0.75f
                    val closingThreshold = -screenWidth * 0.2f

                    when (oldLeftPanelState) {
                        PanelState.OPEN -> {
                            when (it.x) {
                                // if the drawer is dragged more than halfway,
                                in -screenWidth..closingThreshold -> {
                                    // animate it back out,
                                    it.animate().x(-screenWidth).start()
                                    leftPanelState = PanelState.CLOSED
                                    oldLeftPanelState = PanelState.CLOSED
                                }

                                // when it is not,
                                in closingThreshold..screenWidth -> {
                                    screed?.animate()?.alpha(0f)
                                    it.animate().x(0f).start()
                                    leftPanelState = PanelState.OPEN
                                    oldLeftPanelState = PanelState.OPEN
                                }

                            }
                        }

                        PanelState.CLOSED -> {
                            when (it.x) {
                                // if the drawer is dragged more than halfway,
                                in -screenWidth..openThreshold -> {
                                    // animate it back out,
                                    it.animate().x(-screenWidth).start()
                                    leftPanelState = PanelState.CLOSED
                                    oldLeftPanelState = PanelState.CLOSED
                                }

                                // when it is not,
                                in openThreshold..0f -> {
                                    it.animate().x(0f).start()
                                    screed?.animate()?.alpha(0f)
                                    leftPanelState = PanelState.OPEN
                                    oldLeftPanelState = PanelState.OPEN
                                }
                            }
                        }

                        PanelState.SLIDING -> {
                            when (it.x) {
                                // if the drawer is dragged more than halfway,
                                in -screenWidth..closingThreshold -> {
                                    // animate it back out,
                                    it.animate().x(-screenWidth).start()
                                    leftPanelState = PanelState.CLOSED
                                    oldLeftPanelState = PanelState.CLOSED
                                }

                                // when it is not,
                                in closingThreshold..0f -> {
                                    it.animate().x(0f).start()
                                    screed?.animate()?.alpha(0f)
                                    leftPanelState = PanelState.OPEN
                                    oldLeftPanelState = PanelState.OPEN
                                }
                            }
                        }
                    }
                }
                oldXChange = 0f
                true
            }
            else -> true
        }
    }
}