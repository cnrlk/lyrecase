package com.caneru.lyrecase.data.repository

import com.caneru.lyrecase.data.model.Overlay
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("candidates/overlay.json")
    suspend fun getOverlays(): Response<List<Overlay>>
}