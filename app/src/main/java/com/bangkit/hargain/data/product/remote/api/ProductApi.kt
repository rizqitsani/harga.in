package com.bangkit.hargain.data.product.remote.api

import com.bangkit.hargain.data.common.utils.WrappedListResponse
import com.bangkit.hargain.data.common.utils.WrappedResponse
import com.bangkit.hargain.data.product.remote.dto.ProductCreateRequest
import com.bangkit.hargain.data.product.remote.dto.ProductResponse
import com.bangkit.hargain.data.product.remote.dto.ProductUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {
    @GET("products")
    suspend fun getAllProducts() : Response<WrappedListResponse<ProductResponse>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ) : Response<WrappedResponse<ProductResponse>>

    @POST("products")
    suspend fun createProduct(
        @Body productCreateRequest: ProductCreateRequest
    ) : Response<WrappedResponse<ProductResponse>>

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Body productUpdateRequest: ProductUpdateRequest,
        @Path("id") id: String
    ) : Response<WrappedResponse<ProductResponse>>

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: String
    ) : Response<WrappedResponse<ProductResponse>>

}