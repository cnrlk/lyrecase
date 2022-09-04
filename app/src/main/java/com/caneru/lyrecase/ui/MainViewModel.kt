package com.caneru.lyrecase.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caneru.lyrecase.data.model.Overlay
import com.caneru.lyrecase.data.repository.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataRepository: DataRepository
) : ViewModel() {

    private lateinit var overlays: List<Overlay>

    fun getOverlays() {
        viewModelScope.launch {
            val response = dataRepository.getOverlays()
            if (response.isSuccessful) {
                overlays = response.body() ?: listOf()
                Log.d("caneru", response.body().toString())
            } else {
                Log.d("caneru", response.errorBody().toString())
            }
        }
    }
}