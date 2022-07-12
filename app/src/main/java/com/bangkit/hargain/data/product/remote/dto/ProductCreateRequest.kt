package com.bangkit.hargain.data.product.remote.dto

import com.google.gson.annotations.SerializedName

data class ProductCreateRequest(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("brandId") val brandId: String,
    @SerializedName("categoryId") val categoryId: String,
    @SerializedName("currentPrice") val currentPrice: Double,
    @SerializedName("cost") val cost: Double,
    @SerializedName("image") val image: String,

    )
