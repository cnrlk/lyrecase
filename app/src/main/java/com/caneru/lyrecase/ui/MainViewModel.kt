package com.caneru.lyrecase.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.caneru.lyrecase.data.model.Overlay
import com.caneru.lyrecase.data.repository.DataRepository
import com.caneru.lyrecase.ui.widget.OnPreviewOverlaySelected
import com.caneru.lyrecase.ui.widget.OverlayPreviewRecyclerAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel(), OnPreviewOverlaySelected {

    private var overlaysData: List<Overlay> = listOf()
    private var overlayPreviewRecyclerAdapter =
        MutableLiveData(OverlayPreviewRecyclerAdapter(clickListener = this))
    var previewOverlayBitmaps = MutableLiveData(mutableListOf<Bitmap>())
    var previewOverlayNames = MutableLiveData(mutableListOf<String>())
    private var overlayBitmaps = mutableListOf<Bitmap>()
    var selectedOverlay = MutableLiveData<Bitmap>()

    fun fetchOverlays(context: Context) {
        viewModelScope.launch {
            val response = dataRepository.getOverlays()
            if (response.isSuccessful) {
                overlaysData = response.body() ?: listOf()
                previewOverlayNames.value!!.addAll(overlaysData.map { it.overlayName ?: "" })
                fetchOverlaysAsBitmap(context)
                Log.d("caneru", response.body().toString())
                updateAdapter()
            } else {
                Log.d("caneru", response.errorBody().toString())
            }
        }
    }

    private fun fetchOverlaysAsBitmap(context: Context) {
        overlaysData.forEach {
            Glide.with(context).asBitmap().load(
                it.overlayPreviewIconUrl
            ).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    previewOverlayBitmaps.value!!.add(resource)
                    previewOverlayBitmaps.postValue(previewOverlayBitmaps.value)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Nothing
                }
            })
            Glide.with(context).asBitmap().load(
                it.overlayUrl
            ).into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    overlayBitmaps.add(resource)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // Nothing
                }
            })
        }
    }

    private fun updateAdapter() {
        overlayPreviewRecyclerAdapter.value!!.addItems(
            previewOverlayBitmaps.value!!.toList(),
            previewOverlayNames.value!!.toList()
        )
    }

    fun getAdapter(): OverlayPreviewRecyclerAdapter {
        return overlayPreviewRecyclerAdapter.value!!
    }

    override fun onOverlaySelected(index: Int) {
        selectedOverlay.postValue(overlayBitmaps[index])
    }
}