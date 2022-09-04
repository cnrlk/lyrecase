package com.caneru.lyrecase.ui.view

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import com.caneru.lyrecase.R


class OverlayImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mMatrix: Matrix = Matrix()
    private var paint: Paint = Paint()
    private var backgroundImage: Bitmap? = null
    private var imageRectF: RectF? = null
    private var viewRectF: RectF? = null

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (backgroundImage != null && canvas != null) {
            mMatrix.setRectToRect(imageRectF, viewRectF, Matrix.ScaleToFit.START)

            canvas.drawBitmap(backgroundImage!!, mMatrix, paint)
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
        imageRectF =
            RectF(0f, 0f, backgroundImage!!.width.toFloat(), backgroundImage!!.height.toFloat())
        viewRectF = RectF(
            0f,
            0f,
            Resources.getSystem().displayMetrics.widthPixels.toFloat(),
            Resources.getSystem().displayMetrics.heightPixels.toFloat()
        )
    }

}