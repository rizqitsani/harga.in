package com.bangkit.hargain.presentation.main.product.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.domain.product.usecase.GetProductDetailUseCase
import com.bangkit.hargain.presentation.common.base.BaseResult
import com.bangkit.hargain.presentation.common.extension.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel @Inject constructor(private val getProductDetailUseCase: GetProductDetailUseCase) :
    ViewModel() {

    private val state =
        MutableStateFlow<DetailProductFragmentState>(DetailProductFragmentState.Init)
    val mState: StateFlow<DetailProductFragmentState> get() = state

    private val product = MutableStateFlow<ProductEntity?>(null)
    val mProduct: StateFlow<ProductEntity?> get() = product

    private fun setLoading(isLoading: Boolean) {
        state.value = DetailProductFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String) {
        state.value = DetailProductFragmentState.ShowToast(message)
    }

    fun fetchProductDetail(productId: String) {
        viewModelScope.launch {
            getProductDetailUseCase.invoke(productId)
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
                            product.value = result.data
                        }
                        is BaseResult.Error -> {
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }

}

sealed class DetailProductFragmentState {
    object Init : DetailProductFragmentState()
    data class IsLoading(val isLoading: Boolean) : DetailProductFragmentState()
    data class ShowToast(val message: String) : DetailProductFragmentState()
}