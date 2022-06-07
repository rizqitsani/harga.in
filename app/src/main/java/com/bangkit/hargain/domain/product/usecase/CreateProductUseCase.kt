package com.bangkit.hargain.domain.product.usecase

import com.bangkit.hargain.data.common.utils.WrappedResponse
import com.bangkit.hargain.data.product.remote.dto.ProductCreateRequest
import com.bangkit.hargain.data.product.remote.dto.ProductResponse
import com.bangkit.hargain.domain.product.ProductRepository
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

<<<<<<<< HEAD:app/src/main/java/com/bangkit/hargain/domain/product/usecase/CreateProductUseCase.kt
class CreateProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun invoke(productCreateRequest: ProductCreateRequest) : Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return productRepository.createProduct(productCreateRequest)
========
class ProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun getDetail(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return productRepository.getProductDetail(productId)
>>>>>>>> ab511b7 (feat: add delete product):app/src/main/java/com/bangkit/hargain/domain/product/usecase/ProductUseCase.kt
    }

    suspend fun delete(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return productRepository.deleteProduct(productId)
    }
}