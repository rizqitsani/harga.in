package com.bangkit.hargain.data.product.repository

import com.bangkit.hargain.data.common.utils.WrappedResponse
import com.bangkit.hargain.data.product.remote.api.ProductApi
import com.bangkit.hargain.data.product.remote.dto.ProductResponse
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
    override suspend fun getProductDetail(productId: String): Flow<BaseResult<ProductEntity, WrappedResponse<ProductResponse>>> {
        return flow {
            val response = productApi.getProductById(productId)
            if (response.isSuccessful) {
                val productResponse = response.body()?.data

                productResponse?.let {
                    val product = ProductEntity(
                        productResponse.productId,
                        productResponse.title,
                        productResponse.description,
                        productResponse.image,
                        productResponse.brandId,
                        productResponse.categoryId,
                        productResponse.optimalPrice
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

}