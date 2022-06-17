package com.bangkit.hargain.data.product.repository

import com.bangkit.hargain.data.common.utils.WrappedResponse
import com.bangkit.hargain.data.product.remote.api.ProductApi
import com.bangkit.hargain.data.product.remote.dto.*
import com.bangkit.hargain.domain.product.ProductRepository
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(private val productApi: ProductApi) :
    ProductRepository {

    override suspend fun getAllProduct(): Flow<BaseResult<List<ProductEntity>, WrappedResponse<ProductListResponse>>> {
        return flow {
            val response = productApi.getAllProducts()
            if (response.isSuccessful) {
                val body = response.body()
                val products = mutableListOf<ProductEntity>()
                body?.data?.forEach { productResponse ->
                    products.add(
                        ProductEntity(
                            productResponse.productId,
                            productResponse.title,
                            productResponse.description,
                            productResponse.image,
                            productResponse.brand.name,
                            productResponse.category.name,
                            productResponse.currentPrice,
                            productResponse.optimalPrice,
                            productResponse.cost,
                            productResponse.startPrice,
                            productResponse.endPrice,
                            productResponse.pricePredictions
                        )
                    )
                }

                emit(BaseResult.Success(products))
            } else {
                val type = object : TypeToken<WrappedResponse<ProductListResponse>>() {}.type
                val err = Gson().fromJson<WrappedResponse<ProductListResponse>>(
                    response.errorBody()!!.charStream(), type
                )!!
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun getSearchedProduct(
        title: String,
        categoryId: String
    ): Flow<BaseResult<List<ProductEntity>, WrappedResponse<ProductListResponse>>> {
        return flow {
            val response = productApi.searchProducts(title, categoryId)
            if (response.isSuccessful) {
                val body = response.body()
                val products = mutableListOf<ProductEntity>()
                body?.data?.forEach { productResponse ->
                    products.add(
                        ProductEntity(
                            productResponse.productId,
                            productResponse.title,
                            productResponse.description,
                            productResponse.image,
                            productResponse.brand.name,
                            productResponse.category.name,
                            productResponse.currentPrice,
                            productResponse.optimalPrice,
                            productResponse.cost,
                            productResponse.startPrice,
                            productResponse.endPrice,
                            productResponse.pricePredictions
                        )
                    )
                }

                emit(BaseResult.Success(products))
            } else {
                val type = object : TypeToken<WrappedResponse<ProductListResponse>>() {}.type
                val err = Gson().fromJson<WrappedResponse<ProductListResponse>>(
                    response.errorBody()!!.charStream(), type
                )!!
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun getProductDetail(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            val response = productApi.getProductById(productId)
            if (response.isSuccessful) {
                val productResponse = response.body()?.data

                productResponse?.let {
                    val product = ProductEntity(
                        productResponse.id,
                        productResponse.title,
                        productResponse.description,
                        productResponse.image,
                        productResponse.brand.name,
                        productResponse.category.name,
                        productResponse.currentPrice,
                        productResponse.optimalPrice,
                        productResponse.cost,
                        productResponse.startPrice,
                        productResponse.endPrice,
                        productResponse.pricePredictions
                    )

                    emit(BaseResult.Success(product))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<ProductResponse>>() {}.type
                val err = Gson().fromJson<WrappedResponse<ProductResponse>>(
                    response.errorBody()!!.charStream(), type
                )!!
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun createProduct(productCreateRequest: ProductCreateRequest): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>> {
        return flow {
            val response = productApi.createProduct(productCreateRequest)
            if (response.isSuccessful) {
                val data = response.body()?.data!!
                val product = ProductEntity(
                    data.id,
                    data.title,
                    data.description,
                    data.image,
                    data.brandId,
                    data.categoryId,
                    data.currentPrice,
                    data.optimalPrice,
                    data.cost,
                    data.startPrice,
                    data.endPrice,
                    data.pricePredictions
                )
                emit(BaseResult.Success(product))
            } else {
                val type = object : TypeToken<WrappedResponse<CreateProductResponse>>() {}.type
                val err = Gson().fromJson<WrappedResponse<CreateProductResponse>>(
                    response.errorBody()!!.charStream(), type
                )!!
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun updateProduct(
        productId: String,
        productUpdateRequest: ProductUpdateRequest
    ): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>> {
        return flow {
            val response = productApi.updateProduct(productId, productUpdateRequest)
            if (response.isSuccessful) {
                val data = response.body()?.data!!
                val product = ProductEntity(
                    data.id,
                    data.title,
                    data.description,
                    data.image,
                    data.brandId,
                    data.categoryId,
                    data.currentPrice,
                    data.optimalPrice,
                    data.cost,
                    data.startPrice,
                    data.endPrice,
                    data.pricePredictions
                )
                emit(BaseResult.Success(product))
            } else {
                val type = object : TypeToken<WrappedResponse<CreateProductResponse>>() {}.type
                val err = Gson().fromJson<WrappedResponse<CreateProductResponse>>(
                    response.errorBody()!!.charStream(), type
                )!!
                emit(BaseResult.Error(err))
            }
        }
    }

    override suspend fun deleteProduct(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<CreateProductResponse>>> {
        return flow {
            val response = productApi.deleteProduct(productId)
            if (response.isSuccessful) {
                val productResponse = response.body()?.data

                productResponse?.let {
                    val product = ProductEntity(
                        productResponse.id,
                        productResponse.title,
                        productResponse.description,
                        productResponse.image,
                        productResponse.brandId,
                        productResponse.categoryId,
                        productResponse.currentPrice,
                        productResponse.optimalPrice,
                        productResponse.cost,
                        productResponse.startPrice,
                        productResponse.endPrice,
                        productResponse.pricePredictions
                    )

                    emit(BaseResult.Success(product))
                }
            } else {
                val type = object : TypeToken<WrappedResponse<CreateProductResponse>>() {}.type
                val err = Gson().fromJson<WrappedResponse<CreateProductResponse>>(
                    response.errorBody()!!.charStream(), type
                )!!
                emit(BaseResult.Error(err))
            }
        }
    }

}