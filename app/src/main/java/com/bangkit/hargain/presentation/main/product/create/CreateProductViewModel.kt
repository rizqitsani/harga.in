package com.bangkit.hargain.presentation.main.product.create

import android.net.Uri
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
import com.bangkit.hargain.presentation.common.helper.reduceFileImage
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CreateProductViewModel @Inject constructor(
    private val productUseCase: ProductUseCase,
    private val getAllCategoriesUseCase: GetAllCategoriesUseCase,
    private val brandUseCase: BrandUseCase
) : ViewModel() {

    private val state =
        MutableStateFlow<CreateProductFragmentState>(CreateProductFragmentState.Init)
    val mState: StateFlow<CreateProductFragmentState> get() = state

    private val categories = MutableStateFlow<List<CategoryEntity>>(mutableListOf())
    val mCategories: StateFlow<List<CategoryEntity>> get() = categories

    private val brands = MutableStateFlow<List<BrandEntity>>(mutableListOf())
    val mBrands: StateFlow<List<BrandEntity>> get() = brands

    private val imageUrl = MutableStateFlow("")
    val mImageUrl: StateFlow<String> get() = imageUrl

    private fun setLoading(isLoading: Boolean) {
        state.value = CreateProductFragmentState.IsLoading(isLoading)
    }

    private fun showToast(message: String) {
        state.value = CreateProductFragmentState.ShowToast(message)
    }

    private fun successCreate() {
        state.value = CreateProductFragmentState.SuccessCreate
    }

    fun createProduct(productCreateRequest: ProductCreateRequest) {
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
                    when (result) {
                        is BaseResult.Success -> {
                            showToast("Product created.")
                            successCreate()
                        }
                        is BaseResult.Error -> {
                            Log.w(TAG, result.rawResponse.message)
                            showToast(result.rawResponse.message)
                        }
                    }
                }
        }
    }

    fun uploadImage(getFile: File) {

        val file = reduceFileImage(getFile)

        val fileUri = Uri.fromFile(file)
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

        viewModelScope.launch {
            setLoading(true)
            refStorage.putFile(fileUri)
                .addOnSuccessListener { task ->
                    task.storage.downloadUrl.addOnSuccessListener {
                        imageUrl.value = it.toString()
                    }
                }
                .addOnFailureListener { e ->
                    print(e.message)
                    showToast("Upload image failed.")
                    Log.d(TAG, "Upload image failed.")
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
                    when (result) {
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
    object Init : CreateProductFragmentState()
    object SuccessCreate : CreateProductFragmentState()
    data class IsLoading(val isLoading: Boolean) : CreateProductFragmentState()
    data class ShowToast(val message: String) : CreateProductFragmentState()
}