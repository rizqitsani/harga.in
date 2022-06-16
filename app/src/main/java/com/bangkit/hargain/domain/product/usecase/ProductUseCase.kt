package com.bangkit.hargain.domain.product.usecase

import com.bangkit.hargain.data.common.utils.WrappedResponse
import com.bangkit.hargain.data.product.remote.dto.*
import com.bangkit.hargain.domain.product.ProductRepository
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class ProductUseCase @Inject constructor(private val productRepository: ProductRepository) {
    suspend fun create(productCreateRequest: ProductCreateRequest): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>> {
        return productRepository.createProduct(productCreateRequest)
    }

    suspend fun getSearched(
        title: String,
        categoryId: String
    ): Flow<BaseResult<List<ProductEntity>, WrappedResponse<ProductListResponse>>> {
        return productRepository.getSearchedProduct(title, categoryId)
    }

    suspend fun getAll(): Flow<BaseResult<List<ProductEntity>, WrappedResponse<ProductListResponse>>> {
        return productRepository.getAllProduct()
    }

    suspend fun getDetail(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return productRepository.getProductDetail(productId)
    }

    suspend fun update(
        productId: String,
        productUpdateRequest: ProductUpdateRequest
    ): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>> {
        return productRepository.updateProduct(productId, productUpdateRequest)
    }

    suspend fun delete(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>> {
        return productRepository.deleteProduct(productId)
    }
}