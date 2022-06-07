package com.bangkit.hargain.data.category.remote.dto

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("categoryId") var categoryId: String,
    @SerializedName("name") var name: String,
    @SerializedName("image") var image: String
)
