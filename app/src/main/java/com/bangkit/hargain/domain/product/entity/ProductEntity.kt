package com.bangkit.hargain.domain.product.entity

data class ProductEntity(
    var productId: String,
    var title: String,
    var description: String,
    var image: String,
    var brandId: String,
    var categoryId: String,
    var optimalPrice: Int
)
