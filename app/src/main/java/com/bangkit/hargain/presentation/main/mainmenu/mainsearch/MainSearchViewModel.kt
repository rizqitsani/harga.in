package com.bangkit.hargain.presentation.main.mainmenu.mainsearch

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.domain.category.usecase.GetAllCategoriesUseCase
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.domain.product.usecase.ProductUseCase
import com.bangkit.hargain.presentation.common.base.BaseResult
import com.bangkit.hargain.presentation.common.extension.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainSearchViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase
    ) : ViewModel() {

    private val state =
        MutableStateFlow<MainSearchFragmentState>(MainSearchFragmentState.Init)
    val mState: StateFlow<MainSearchFragmentState> get() = state

    private val products = MutableStateFlow<List<ProductEntity>>(mutableListOf())
    val mProducts: StateFlow<List<ProductEntity>> get() = products

    private val categories = MutableStateFlow<List<CategoryEntity>>(mutableListOf())
    val mCategories: StateFlow<List<CategoryEntity>> get() = categories

    private fun setLoading(isLoading: Boolean) {
        state.value = MainSearchFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String) {
        state.value = MainSearchFragmentState.ShowToast(message)
    }

    fun searchProducts(title: String, categoryId: String) {
        viewModelScope.launch {
            productUseCase.getSearched(title, categoryId)
                .onStart {
                    setLoading(true)
                }
                .catch { exception ->
                    setLoading(false)
                    Log.w(TAG, "fetchSearchedProduct:failure", exception)
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    setLoading(false)
                    when (result) {
                        is BaseResult.Success -> {
                            products.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            productUseCase.getAll()
                .onStart {
                    setLoading(true)
                }
                .catch { exception ->
                    setLoading(false)
                    Log.w(TAG, "fetchProductDetail:failure", exception)
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    setLoading(false)
                    when (result) {
                        is BaseResult.Success -> {
                            products.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
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
                    when (result) {
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

}

sealed class MainSearchFragmentState {
    object Init : MainSearchFragmentState()
    data class IsLoading(val isLoading: Boolean) : MainSearchFragmentState()
    data class ShowToast(val message: String) : MainSearchFragmentState()
}