package com.caneru.lyrecase.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caneru.lyrecase.R
import com.caneru.lyrecase.databinding.ActivityMainBinding
import com.caneru.lyrecase.ui.widget.OverlayPreviewRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainViewModel.fetchOverlays(this)
        activityMainBinding.rvOverlayPreviews.adapter = mainViewModel.getAdapter()
        activityMainBinding.rvOverlayPreviews.layoutManager =
            LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)

        mainViewModel.previewOverlayBitmaps.observeForever {
            val adapter = (activityMainBinding.rvOverlayPreviews.adapter as OverlayPreviewRecyclerAdapter)
            adapter.addItems(it, mainViewModel.previewOverlayNames.value!!.toList())
            adapter.notifyDataSetChanged()
        }
        mainViewModel.selectedOverlay.observeForever{
            activityMainBinding.oivImage.setOverlayImage(it)
        }
    }

}