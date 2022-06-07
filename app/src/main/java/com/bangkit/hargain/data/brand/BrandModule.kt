package com.bangkit.hargain.data.brand

import com.bangkit.hargain.data.brand.remote.api.BrandApi
import com.bangkit.hargain.data.brand.repository.BrandRepositoryImpl
import com.bangkit.hargain.data.common.module.NetworkModule
import com.bangkit.hargain.domain.brand.BrandRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class BrandModule {
    @Singleton
    @Provides
    fun provideBrandApi(retrofit: Retrofit) : BrandApi {
        return retrofit.create(BrandApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBrandRepository(brandApi: BrandApi) : BrandRepository {
        return BrandRepositoryImpl(brandApi)
    }
}