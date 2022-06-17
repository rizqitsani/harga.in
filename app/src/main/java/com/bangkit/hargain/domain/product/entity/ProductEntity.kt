package com.bangkit.hargain.domain.product.entity

import android.os.Parcelable
import com.bangkit.hargain.data.product.remote.dto.PricePrediction
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductEntity(
    var id: String,
    var title: String,
    var description: String,
    var image: String,
    var brandName: String,
    var categoryName: String,
    var currentPrice: Double,
    var optimalPrice: Double,
    var cost: Double,
    var startPrice: Double,
    var endPrice: Double,
    var pricePredictions: List<PricePrediction>
) : Parcelable
