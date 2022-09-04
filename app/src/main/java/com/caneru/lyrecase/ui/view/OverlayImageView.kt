package com.caneru.lyrecase.ui.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.caneru.lyrecase.R
import kotlin.math.hypot


class OverlayImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mMatrix: Matrix = Matrix()
    private var mOverlayMatrix: Matrix = Matrix()
    private var paint: Paint = Paint()
    private var backgroundImageRectF: RectF? = null
    private var overlayImageRectF: RectF? = null
    private var viewRectF: RectF? = RectF(
        0f,
        0f,
        Resources.getSystem().displayMetrics.widthPixels.toFloat(),
        Resources.getSystem().displayMetrics.heightPixels.toFloat()
    )

    private var backgroundImage: Bitmap? = null
    private var overlayImage: Bitmap? = null
    var editMode = -1

    var start = PointF()
    var startHypot = hypot(0f, 0f)
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (backgroundImage != null && canvas != null) {

            canvas.drawBitmap(backgroundImage!!, mMatrix, paint)
            if (overlayImage != null) {
                canvas.drawBitmap(overlayImage!!, mOverlayMatrix, paint)
            }
        }
    }

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.OverlayImageView,
            0, 0
        ).apply {

            try {
                val image = getResourceId(R.styleable.OverlayImageView_image, 0)
                updateBackgroundImage(
                    ResourcesCompat.getDrawable(resources, image, context.theme)!!.toBitmap()
                )
            } finally {
                recycle()
            }
        }
    }

    private fun updateBackgroundImage(bitmap: Bitmap) {
        backgroundImage = bitmap
        backgroundImageRectF =
            RectF(0f, 0f, backgroundImage!!.width.toFloat(), backgroundImage!!.height.toFloat())
        mMatrix.setRectToRect(backgroundImageRectF, viewRectF, Matrix.ScaleToFit.START)
    }

    fun setOverlayImage(bitmap: Bitmap) {
        overlayImage = bitmap
        overlayImageRectF =
            RectF(0f, 0f, overlayImage!!.width.toFloat(), overlayImage!!.height.toFloat())
        mOverlayMatrix.setRectToRect(overlayImageRectF, viewRectF, Matrix.ScaleToFit.START)
        startHypot = hypot(
            (overlayImage!!.height.toFloat()) / Resources.getSystem().displayMetrics.density,
            (overlayImage!!.width.toFloat()) / Resources.getSystem().displayMetrics.density
        )
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event != null) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    Log.d("CANERUU", "start drag")
                    start = PointF(event.x, event.y)
                    editMode = event.action
                }
                MotionEvent.ACTION_POINTER_2_DOWN -> {
                    Log.d("CANERUU", "start scale")
                    start = PointF(event.x, event.y)
                    editMode = event.action
                }
                MotionEvent.ACTION_MOVE -> {
                    if (editMode == MotionEvent.ACTION_DOWN) {
                        Log.d("CANERUU", "dragging")
                        mOverlayMatrix.postTranslate((event.x - start.x), (event.y - start.y))
                        start = PointF(event.x, event.y)
                    } else if (editMode == MotionEvent.ACTION_POINTER_2_DOWN) {
                        Log.d("CANERUU", "scaling")
                        val scale = PointF(event.x, event.y)
                        mOverlayMatrix.postScale(
                            scale.length() / start.length(),
                            scale.length() / start.length(),
                            overlayImage!!.width / 2f,
                            overlayImage!!.height / 2f
                        )
                        if (scale.length() - start.length() > 100) {
                            start = PointF(event.x, event.y)
                        }
                    }
                }
            }
        }
        invalidate()
        return true
    }
}