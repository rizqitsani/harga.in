package com.bangkit.hargain.domain.category

import com.bangkit.hargain.data.category.remote.dto.CategoryResponse
import com.bangkit.hargain.data.common.utils.WrappedListResponse
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getAllCategories() : Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryResponse>>>
}