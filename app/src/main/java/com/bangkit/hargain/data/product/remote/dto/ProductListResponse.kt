package com.bangkit.hargain.data.product.remote.dto

import com.bangkit.hargain.domain.product.entity.PricePredictionEntity
import com.google.gson.annotations.SerializedName




data class ProductListResponse(

    @field:SerializedName("productId")
    val productId: String,

    @field:SerializedName("startPrice")
    val startPrice: Int,

    @field:SerializedName("image")
    val image: String,

    @field:SerializedName("cost")
    val cost: Int,

    @field:SerializedName("optimalPrice")
    val optimalPrice: Double,

    @field:SerializedName("pricePredictions")
    val PricePredictions: List<PricePrediction> ,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("endPrice")
    val endPrice: Int,

    @field:SerializedName("title")
    val title: String,

    )
