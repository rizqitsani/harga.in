package com.bangkit.hargain.domain.category.usecase

import com.bangkit.hargain.data.category.remote.dto.CategoryResponse
import com.bangkit.hargain.data.common.utils.WrappedListResponse
import com.bangkit.hargain.domain.category.CategoryRepository
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    suspend fun invoke() : Flow<BaseResult<List<CategoryEntity>, WrappedListResponse<CategoryResponse>>> {
        return categoryRepository.getAllCategories()
    }
}