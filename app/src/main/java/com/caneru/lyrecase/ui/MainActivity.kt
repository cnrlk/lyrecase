package com.caneru.lyrecase.ui

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caneru.lyrecase.R
import com.caneru.lyrecase.databinding.ActivityMainBinding
import com.caneru.lyrecase.ui.widget.OverlayPreviewRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileOutputStream


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
            val adapter =
                (activityMainBinding.rvOverlayPreviews.adapter as OverlayPreviewRecyclerAdapter)
            adapter.addItems(it, mainViewModel.previewOverlayNames.value!!.toList())
            adapter.notifyDataSetChanged()
        }
        mainViewModel.selectedOverlay.observeForever {
            activityMainBinding.oivImage.setOverlayImage(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        saveImage(activityMainBinding.oivImage.getFinalBitmap())
        return super.onOptionsItemSelected(item)
    }

    private fun saveImage(bitmap: Bitmap?) {
        if (bitmap != null) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                Log.d("CANERUU", "ask for permission")
                requestPermissions(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    0
                )

            } else {
                val path = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES
                )
                val file = File(path, Math.random().toString() + ".jpg")

                try {
                    path.mkdirs()
                    val out = FileOutputStream(file.absolutePath)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
                    out.flush()
                    out.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        }
    }
}