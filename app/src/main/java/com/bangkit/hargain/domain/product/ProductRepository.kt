package com.bangkit.hargain.domain.product

import com.bangkit.hargain.data.common.utils.WrappedResponse
import com.bangkit.hargain.data.product.remote.dto.*
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getSearchedProduct(
        title: String,
        categoryId: String
    ): Flow<BaseResult<List<ProductEntity>, WrappedResponse<ProductListResponse>>>

    suspend fun getAllProduct(): Flow<BaseResult<List<ProductEntity>, WrappedResponse<ProductListResponse>>>
    suspend fun getProductDetail(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>
    suspend fun createProduct(productCreateRequest: ProductCreateRequest): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>>
    suspend fun updateProduct(
        productId: String,
        productUpdateRequest: ProductUpdateRequest
    ): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>>

    suspend fun deleteProduct(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>>
}