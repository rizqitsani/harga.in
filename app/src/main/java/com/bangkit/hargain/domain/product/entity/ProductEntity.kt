package com.bangkit.hargain.domain.product.entity

import com.google.gson.annotations.SerializedName

data class ProductEntity(
    var id: String,
    var title: String,
    var description: String,
    var image: String,
    var brandId: String,
    var categoryId: String,
    var currentPrice: Double,
    var optimalPrice: Double,
    var cost: Double,
    var startPrice: Double,
    var endPrice: Double,
)
