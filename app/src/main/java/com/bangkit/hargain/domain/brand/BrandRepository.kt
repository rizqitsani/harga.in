package com.bangkit.hargain.domain.brand

import com.bangkit.hargain.data.brand.remote.dto.BrandResponse
import com.bangkit.hargain.data.common.utils.WrappedListResponse
import com.bangkit.hargain.domain.brand.entity.BrandEntity
import com.bangkit.hargain.presentation.common.base.BaseResult
import kotlinx.coroutines.flow.Flow

interface BrandRepository {
    suspend fun getAllBrands(): Flow<BaseResult<List<BrandEntity>, WrappedListResponse<BrandResponse>>>

}