package com.bangkit.hargain.data.brand.repository

import com.bangkit.hargain.data.brand.remote.api.BrandApi
import com.bangkit.hargain.data.brand.remote.dto.BrandResponse
import com.bangkit.hargain.data.common.utils.WrappedListResponse
import com.bangkit.hargain.domain.brand.BrandRepository
import com.bangkit.hargain.domain.brand.entity.BrandEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BrandRepositoryImpl @Inject constructor(private val brandApi: BrandApi) : BrandRepository {
    override suspend fun getAllBrands(): Flow<BaseResult<List<BrandEntity>, WrappedListResponse<BrandResponse>>> {
        return flow {
            val response = brandApi.getAllBrands()
            if(response.isSuccessful) {
                val body = response.body()
                val brands = mutableListOf<BrandEntity>()
                body?.data?.forEach { categoryResponse ->
                    brands.add(
                        BrandEntity(
                            categoryResponse.brandId,
                            categoryResponse.name,
                        )
                    )
                }

                emit(BaseResult.Success(brands))
            } else {
                val type = object : TypeToken<WrappedListResponse<BrandResponse>>(){}.type
                val err = Gson().fromJson<WrappedListResponse<BrandResponse>>(response.errorBody()!!.charStream(), type)!!
//                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

}