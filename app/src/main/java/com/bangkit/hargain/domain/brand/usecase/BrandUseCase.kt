package com.bangkit.hargain.domain.brand.usecase

import com.bangkit.hargain.data.brand.remote.dto.BrandResponse
import com.bangkit.hargain.data.common.utils.WrappedListResponse
import com.bangkit.hargain.domain.brand.BrandRepository
import com.bangkit.hargain.domain.brand.entity.BrandEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BrandUseCase @Inject constructor(private val brandRepository: BrandRepository) {
    suspend fun getAllBrands() : Flow<BaseResult<List<BrandEntity>, WrappedListResponse<BrandResponse>>> {
        return brandRepository.getAllBrands()
    }
}
