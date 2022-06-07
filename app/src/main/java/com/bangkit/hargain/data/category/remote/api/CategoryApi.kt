package com.bangkit.hargain.data.category.remote.api

import com.bangkit.hargain.data.category.remote.dto.CategoryResponse
import com.bangkit.hargain.data.common.utils.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET

interface  CategoryApi {
    @GET("categories")
    suspend fun getAllCategories() : Response<WrappedListResponse<CategoryResponse>>
}