package com.bangkit.hargain.presentation.main.product.create

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.hargain.data.product.remote.dto.ProductCreateRequest
import com.bangkit.hargain.domain.brand.entity.BrandEntity
import com.bangkit.hargain.domain.brand.usecase.BrandUseCase
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.domain.category.usecase.GetAllCategoriesUseCase
import com.bangkit.hargain.domain.product.usecase.ProductUseCase
import com.bangkit.hargain.presentation.common.base.BaseResult
import com.bangkit.hargain.presentation.common.extension.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val brandUseCase: BrandUseCase
    ): ViewModel() {

    private val state = MutableStateFlow<CreateProductFragmentState>(CreateProductFragmentState.Init)
    val mState: StateFlow<CreateProductFragmentState> get() = state

    private val categories = MutableStateFlow<List<CategoryEntity>>(mutableListOf())
    val mCategories: StateFlow<List<CategoryEntity>> get() = categories

    private val brands = MutableStateFlow<List<BrandEntity>>(mutableListOf())
    val mBrands: StateFlow<List<BrandEntity>> get() = brands

    private fun setLoading(isLoading: Boolean){
        state.value = CreateProductFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String){
        state.value = CreateProductFragmentState.ShowToast(message)
    }

    private fun successCreate(){
        state.value = CreateProductFragmentState.SuccessCreate
    }

    fun createProduct(productCreateRequest: ProductCreateRequest){
        viewModelScope.launch {
            productUseCase.create(productCreateRequest)
                .onStart {
                    setLoading(true)
                }
                .catch { exception ->
                    setLoading(false)
                    showToast(exception.stackTraceToString())
                }
                .collect { result ->
                    setLoading(false)
                    when(result){
                        is BaseResult.Success -> {
                            showToast("Product created.")
                            successCreate()
                        }
                        is BaseResult.Error -> showToast(result.rawResponse.message)
                    }
                }
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase.invoke()
                .onStart {
                }
                .catch { exception ->
                    Log.w(TAG, "fetchCategories:failure", exception)
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    when(result) {
                        is BaseResult.Success -> {
                            categories.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }

    fun getBrands() {
        viewModelScope.launch {
            brandUseCase.getAllBrands()
                .onStart {
                }
                .catch { exception ->
                    Log.w(TAG, "fetchBrands:failure", exception)
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    when(result) {
                        is BaseResult.Success -> {
                            brands.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }
}

sealed class CreateProductFragmentState {
    object Init: CreateProductFragmentState()
    object SuccessCreate : CreateProductFragmentState()
    data class IsLoading(val isLoading: Boolean) : CreateProductFragmentState()
    data class ShowToast(val message: String) : CreateProductFragmentState()
}