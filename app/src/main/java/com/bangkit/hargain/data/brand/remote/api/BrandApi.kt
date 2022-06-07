package com.bangkit.hargain.data.brand.remote.api

import com.bangkit.hargain.data.brand.remote.dto.BrandResponse
import com.bangkit.hargain.data.common.utils.WrappedListResponse
import retrofit2.Response
import retrofit2.http.GET

interface BrandApi {
    @GET("brands")
    suspend fun getAllBrands() : Response<WrappedListResponse<BrandResponse>>
}