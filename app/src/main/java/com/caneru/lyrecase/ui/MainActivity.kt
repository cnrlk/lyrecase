package com.caneru.lyrecase.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.caneru.lyrecase.R
import com.caneru.lyrecase.ui.view.OverlayImageView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainViewModel.fetchOverlays { setOverlay() }
    }

    fun setOverlay() {
        val overlayImageView = findViewById<ImageView>(R.id.iv_overlay)
        Glide.with(baseContext).asBitmap().load(
            mainViewModel.getOverlays()[0].overlayPreviewIconUrl
        ).into(overlayImageView)
        Glide.with(baseContext).asBitmap().load(
            mainViewModel.getOverlays()[0].overlayUrl
        ).into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                findViewById<OverlayImageView>(R.id.oiv_image).setOverlayImage(resource)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // Nothing
            }
        })

    }

}