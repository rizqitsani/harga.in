package com.bangkit.hargain.presentation.main.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.domain.category.usecase.GetAllCategoriesUseCase
import com.bangkit.hargain.presentation.common.base.BaseResult
import com.bangkit.hargain.presentation.common.extension.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeMainViewModel @Inject constructor(private val getAllCategoriesUseCase: GetAllCategoriesUseCase) : ViewModel() {

    private val state = MutableStateFlow<HomeMainFragmentState>(HomeMainFragmentState.Init)
    val mState: StateFlow<HomeMainFragmentState> get() = state

    private val categories = MutableStateFlow<List<CategoryEntity>>(mutableListOf())
    val mCategories: StateFlow<List<CategoryEntity>> get() = categories

    init {
        fetchCategories()
    }

    private fun setLoading(isLoading: Boolean) {
        state.value = HomeMainFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String) {
        state.value = HomeMainFragmentState.ShowToast(message)
    }

    fun fetchCategories() {
        viewModelScope.launch {
            getAllCategoriesUseCase.invoke()
                .onStart {
                    setLoading(true)
                }
                .catch { exception ->
                    setLoading(false)
                    Log.w(TAG, "fetchCategories:failure", exception)
                    showToast(exception.message.toString())
                }
                .collect { result ->
                    setLoading(false)
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

}

sealed class HomeMainFragmentState {
    object Init: HomeMainFragmentState()
    data class IsLoading(val isLoading: Boolean) : HomeMainFragmentState()
    data class ShowToast(val message: String) : HomeMainFragmentState()
}