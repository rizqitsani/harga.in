package com.bangkit.hargain.data.product.remote.dto

import com.google.gson.annotations.SerializedName
import com.google.type.DateTime

data class ProductResponse(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("brandId") val brandId: String,
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("currentPrice") val currentPrice: Int,
    @SerializedName("optimalPrice") val optimalPrice: Int,
    @SerializedName("image") val image: String,
)
