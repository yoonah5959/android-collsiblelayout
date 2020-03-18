package com.heenu.collapsiblelayout

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import kotlin.math.roundToInt


class CollapsibleLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ConstraintLayout(context, attrs) {

    private var duration = 300L

    private var resizeRatio = 0f

    private var first = true

    companion object {

        private const val DEFAULT_DURATION = 300

    }

    init {
        if (attrs != null) {
            val a = getContext().obtainStyledAttributes(attrs, R.styleable.CollapsibleLayout)
            duration = a.getInt(R.styleable.CollapsibleLayout_duration, DEFAULT_DURATION).toLong()
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (first)
            return

        val width = measuredWidth

        val height = measuredHeight

        val offset = height - (height * resizeRatio).roundToInt()

        val parallaxDelta = offset * 1f

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            child.translationY = -parallaxDelta

        }

        setMeasuredDimension(width, height - offset)
    }

    fun collapse() {
        first = false
        val animator = ValueAnimator.ofFloat(1f, 0f)
        animator.duration = 300L
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener {
            resizeHeight(animator.animatedValue as Float)
        }
        animator.start()
    }

    private fun resizeHeight(animatedValue: Float) {
        if (this.resizeRatio == animatedValue) {
            return
        }
        this.resizeRatio = animatedValue
        requestLayout()
    }

}