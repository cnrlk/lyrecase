package com.caneru.lyrecase.data.repository

import com.caneru.lyrecase.data.model.Overlay
import retrofit2.Response
import javax.inject.Inject

class DataRepository @Inject constructor(
    private val apiInterface: ApiInterface
) {

    suspend fun getOverlays(): Response<List<Overlay>> {
        return apiInterface.getOverlays()
    }

}