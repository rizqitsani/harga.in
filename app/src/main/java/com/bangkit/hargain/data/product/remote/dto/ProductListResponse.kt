package com.bangkit.hargain.data.product.remote.dto

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PricePrediction(
    @SerializedName("selling_price")
    val sellingPrice: Double,
    @SerializedName("total_profit")
    val totalProfit: Double,
    @SerializedName("total_sales")
    val totalSales: Double,
) : Parcelable


data class Brand(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
)

data class Category(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
)

data class ProductListResponse(
    @SerializedName("productId") val productId: String,
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("brand") val brand: Brand,
    @SerializedName("category") val category: Category,
    @SerializedName("currentPrice") val currentPrice: Double,
    @SerializedName("optimalPrice") val optimalPrice: Double,
    @SerializedName("cost") val cost: Double,
    @SerializedName("startPrice") val startPrice: Double,
    @SerializedName("endPrice") val endPrice: Double,
    @SerializedName("pricePredictions") val pricePredictions: List<PricePrediction>
)
