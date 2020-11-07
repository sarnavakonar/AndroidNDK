package net.surina.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class RecordView : View {

    var p = Paint()
    var textPaint = Paint()
    var rectF = RectF()
    var text = "10"
    var sweepAngle = 360f
    var second = "seconds"

    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {}

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {}

    fun updateTimeText(timeLeft: Long){
        sweepAngle = (timeLeft.toFloat()/10000*360)
        val time = (timeLeft/1000).toInt()
        text = time.toString()
        if(time == 1)
            second = "second"
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width.toFloat()
        val height = height.toFloat()

        textPaint.color = Color.BLACK
        textPaint.textSize = 40f
        textPaint.textAlign = Paint.Align.CENTER
        canvas.drawText(text, width / 2, height / 2, textPaint)
        val yPos = height / 2 - (textPaint.descent() + textPaint.ascent())
        textPaint.color = Color.DKGRAY
        textPaint.textSize = 27.5f
        canvas.drawText(second, width / 2, yPos, textPaint)

        rectF = RectF(width / 2 - 100, height / 2 - 100, width / 2 + 100, height / 2 + 100)
        p.color = Color.parseColor("#01C7FE")
        //p.strokeCap = Paint.Cap.ROUND
        p.style = Paint.Style.STROKE
        p.strokeWidth = 50f
        canvas.drawArc(rectF, -90f, sweepAngle, false, p)
    }
}