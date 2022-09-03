package com.caneru.lyrecase.data.model

import com.google.gson.annotations.SerializedName

data class Overlay (
    @SerializedName("overlayId"             ) var overlayId             : Int?    = null,
    @SerializedName("overlayName"           ) var overlayName           : String? = null,
    @SerializedName("overlayPreviewIconUrl" ) var overlayPreviewIconUrl : String? = null,
    @SerializedName("overlayUrl"            ) var overlayUrl            : String? = null
)