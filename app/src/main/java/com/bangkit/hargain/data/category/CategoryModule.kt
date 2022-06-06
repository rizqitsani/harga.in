package com.bangkit.hargain.data.category

import com.bangkit.hargain.data.category.remote.api.CategoryApi
import com.bangkit.hargain.data.category.repository.CategoryRepositoryImpl
import com.bangkit.hargain.data.common.module.NetworkModule
import com.bangkit.hargain.domain.category.CategoryRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
class CategoryModule {
    @Singleton
    @Provides
    fun provideCategoryApi(retrofit: Retrofit) : CategoryApi {
        return retrofit.create(CategoryApi::class.java)
    }

    @Singleton
    @Provides
    fun provideCategoryRepository(categoryApi: CategoryApi) : CategoryRepository {
        return CategoryRepositoryImpl(categoryApi)
    }
}