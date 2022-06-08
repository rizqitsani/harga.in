package com.bangkit.hargain.data.brand.remote.dto

import com.google.gson.annotations.SerializedName

data class BrandResponse(
    @SerializedName("brandId") var brandId: String,
    @SerializedName("name") var name: String,
)
