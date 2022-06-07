package com.bangkit.hargain.domain.product

import com.bangkit.hargain.data.common.utils.WrappedResponse
import com.bangkit.hargain.data.product.remote.dto.ProductCreateRequest
import com.bangkit.hargain.data.product.remote.dto.ProductResponse
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductDetail(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>

    suspend fun createProduct(productCreateRequest: ProductCreateRequest) : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>
    
    suspend fun deleteProduct(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>>
}