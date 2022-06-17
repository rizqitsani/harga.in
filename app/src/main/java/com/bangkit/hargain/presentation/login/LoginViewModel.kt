package com.bangkit.hargain.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bangkit.hargain.BuildConfig
import com.bangkit.hargain.data.login.remote.api.AuthAPI
import com.bangkit.hargain.domain.login.entity.LoginEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginViewModel: ViewModel() {

    private val state = MutableStateFlow<LoginActivityState>(LoginActivityState.Init)
    val mState: StateFlow<LoginActivityState> get() = state


    private fun setLoading(isLoading: Boolean){
        state.value = LoginActivityState.IsLoading(isLoading)
    }

    private fun showToast(message: String){
        state.value = LoginActivityState.ShowToast(message)
    }

    private var auth: FirebaseAuth = Firebase.auth

    fun login(email: String, password: String) {
        // Create Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://identitytoolkit.googleapis.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create Service
        val service = retrofit.create(AuthAPI::class.java)

        setLoading(true)

        CoroutineScope(Dispatchers.IO).launch {
            // Do the POST request and get response
            val response = service.login(BuildConfig.GOOGLE_API_KEY, email, password, true)

            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    setLoading(false)
                    val response = response.body()
                    val loginEntity = LoginEntity(response?.displayName ?: "", response?.email ?: "", response?.idToken ?: "")
                    state.value = LoginActivityState.SuccessLogin(loginEntity)
                    showToast("Authentication success.")

                } else {
                    setLoading(false)
                    Log.e("RETROFIT_ERROR", response.code().toString())
                    showToast("Authentication failed.")
                }
            }
        }
    }




}

sealed class LoginActivityState  {
    object Init : LoginActivityState()
    data class IsLoading(val isLoading: Boolean) : LoginActivityState()
    data class ShowToast(val message: String) : LoginActivityState()
    data class SuccessLogin(val loginEntity: LoginEntity) : LoginActivityState()
//    data class ErrorLogin(val rawResponse: WrappedResponse<LoginResponse>) : LoginActivityState()
}