package com.bangkit.hargain.data.product.remote.api

import com.bangkit.hargain.data.common.utils.WrappedListResponse
import com.bangkit.hargain.data.common.utils.WrappedResponse
import com.bangkit.hargain.data.product.remote.dto.ProductCreateRequest
import com.bangkit.hargain.data.product.remote.dto.ProductListResponse
import com.bangkit.hargain.data.product.remote.dto.ProductResponse
import com.bangkit.hargain.data.product.remote.dto.ProductUpdateRequest
import retrofit2.Response
import retrofit2.http.*

interface ProductApi {
    @GET("products")
    suspend fun getAllProducts(): Response<WrappedListResponse<ProductListResponse>>

    @GET("products")
    suspend fun searchProducts(
        @Query("title") title: String,
        @Query("category") categoryId: String
    ): Response<WrappedListResponse<ProductListResponse>>

    @GET("products/{id}")
    suspend fun getProductById(
        @Path("id") id: String
    ): Response<WrappedResponse<ProductResponse>>

    @GET("products/search/{title}")
    suspend fun getProductByTitle(
        @Path("title") title: String
    ): Response<WrappedListResponse<ProductListResponse>>

    @POST("products")
    suspend fun createProduct(
        @Body productCreateRequest: ProductCreateRequest
    ): Response<WrappedResponse<ProductResponse>>

    @PUT("products/{id}")
    suspend fun updateProduct(
        @Path("id") id: String,
        @Body productUpdateRequest: ProductUpdateRequest
    ): Response<WrappedResponse<ProductResponse>>

    @DELETE("products/{id}")
    suspend fun deleteProduct(
        @Path("id") id: String
    ): Response<WrappedResponse<ProductResponse>>

}