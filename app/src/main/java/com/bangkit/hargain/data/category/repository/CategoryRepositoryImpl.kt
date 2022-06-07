package com.bangkit.hargain.data.category.repository

import com.bangkit.hargain.data.category.remote.api.CategoryApi
import com.bangkit.hargain.data.category.remote.dto.CategoryResponse
import com.bangkit.hargain.data.common.utils.WrappedListResponse
import com.bangkit.hargain.domain.category.CategoryRepository
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryApi: CategoryApi) : CategoryRepository {
    override suspend fun getAllCategories(): Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryResponse>>> {
        return flow {
            val response = categoryApi.getAllCategories()
            if(response.isSuccessful) {
                val body = response.body()
                val categories = mutableListOf<CategoryEntity>()
                body?.data?.forEach { categoryResponse ->
                    categories.add(
                        CategoryEntity(
                            categoryResponse.categoryId,
                            categoryResponse.name,
                            categoryResponse.image
                        )
                    )
                }

                emit(BaseResult.Success(categories))
            } else {
                val type = object : TypeToken<WrappedListResponse<CategoryResponse>>(){}.type
                val err = Gson().fromJson<WrappedListResponse<CategoryResponse>>(response.errorBody()!!.charStream(), type)!!
//                err.code = response.code()
                emit(BaseResult.Error(err))
            }
        }
    }

}