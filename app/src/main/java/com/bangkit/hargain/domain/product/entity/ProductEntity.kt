package com.bangkit.hargain.domain.product.entity

data class ProductEntity(
    var id: String,
    var title: String,
    var description: String,
    var image: String,
    var brandId: String,
    var categoryId: String
)
