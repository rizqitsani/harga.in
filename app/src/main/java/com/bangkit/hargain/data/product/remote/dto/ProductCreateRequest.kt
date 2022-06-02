package com.bangkit.hargain.data.product.remote.dto

import com.google.gson.annotations.SerializedName
import com.google.type.DateTime

data class ProductCreateRequest(
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("brand_id") val brandId: Int,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("current_price") val currentPrice: Int,
    @SerializedName("image") val image: String,

)
