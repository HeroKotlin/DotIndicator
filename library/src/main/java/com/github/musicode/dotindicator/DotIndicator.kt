package com.github.herokotlin.dotindicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View

class DotIndicator : View {

    companion object {

        var DEFAULT_PAGE = 0

        var DEFAULT_TOTAL = 0

        var DEFAULT_COLOR = Color.parseColor("#D6D6D6")

        var DEFAULT_TINT_COLOR = Color.parseColor("#FFFFFF")

        var DEFAULT_RADIUS = 4

        var DEFAULT_TINT_RADIUS = 4

        var DEFAULT_GAP = 4

    }

    // 当前页
    var page = DEFAULT_PAGE

    // 总页数
    var total = DEFAULT_TOTAL

    // 默认颜色
    var color = DEFAULT_COLOR

    // 当前页的颜色
    var tintColor = DEFAULT_TINT_COLOR

    // 圆点的半径
    var radius = DEFAULT_RADIUS

    // 当前页圆点的半径
    var tintRadius = DEFAULT_TINT_RADIUS

    // 圆点的间距
    var gap = DEFAULT_GAP

    private var contentWidth = 0

        get() {
            return 2 * radius * (total - 1) + 2 * tintRadius + gap * (total - 1)
        }

    private var contentHeight = 0

        get() {
            return 2 * Math.max(tintRadius, radius)
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs, defStyle)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {

        val typedArray = context.obtainStyledAttributes(
                attrs, R.styleable.DotIndicator, defStyle, 0)

        page = typedArray.getInt(R.styleable.DotIndicator_dot_indicator_page, DEFAULT_PAGE)

        total = typedArray.getInt(R.styleable.DotIndicator_dot_indicator_total, DEFAULT_TOTAL)

        color = typedArray.getColor(R.styleable.DotIndicator_dot_indicator_color, DEFAULT_COLOR)

        radius = typedArray.getDimensionPixelSize(
            R.styleable.DotIndicator_dot_indicator_radius,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_RADIUS.toFloat(), resources.displayMetrics).toInt()
        )

        gap = typedArray.getDimensionPixelSize(
            R.styleable.DotIndicator_dot_indicator_gap,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_GAP.toFloat(), resources.displayMetrics).toInt()
        )

        tintColor = typedArray.getColor(R.styleable.DotIndicator_dot_indicator_tint_color, DEFAULT_TINT_COLOR)

        tintRadius = typedArray.getDimensionPixelSize(
            R.styleable.DotIndicator_dot_indicator_tint_radius,
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, DEFAULT_TINT_RADIUS.toFloat(), resources.displayMetrics).toInt()
        )

        // 获取完 TypedArray 的值后，
        // 一般要调用 recycle 方法来避免重新创建的时候出错
        typedArray.recycle()

    }



    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        var width = widthSize
        var height = heightSize

        if (widthMode != MeasureSpec.EXACTLY) {
            width = contentWidth
        }

        if (heightMode != MeasureSpec.EXACTLY) {
            height = contentHeight
        }

        setMeasuredDimension(width, height)

    }

    override fun onDraw(canvas: Canvas) {

        super.onDraw(canvas)

        if (total <= 0) {
            return
        }

        paint.style = Paint.Style.FILL

        var startX = (width - contentWidth) / 2
        val centerY = (height / 2).toFloat()
        var dotIndex = 0
        var dotRadius = 0

        while (dotIndex < total) {

            if (dotIndex == page) {
                paint.color = tintColor
                dotRadius = tintRadius
            }
            else {
                paint.color = color
                dotRadius = radius
            }

            canvas.drawCircle(
                (startX + dotRadius).toFloat(),
                centerY,
                dotRadius.toFloat(),
                paint
            )

            startX += 2 * dotRadius + gap

            dotIndex++

        }


    }
}
