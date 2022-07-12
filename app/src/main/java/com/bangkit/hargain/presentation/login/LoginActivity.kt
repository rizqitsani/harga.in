package com.bangkit.hargain.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.bangkit.hargain.databinding.ActivityLoginBinding
import com.bangkit.hargain.domain.login.entity.LoginEntity
import com.bangkit.hargain.infra.utils.SharedPrefs
import com.bangkit.hargain.presentation.common.extension.TAG
import com.bangkit.hargain.presentation.common.extension.isEmail
import com.bangkit.hargain.presentation.common.extension.showToast
import com.bangkit.hargain.presentation.main.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val viewModel: LoginViewModel by viewModels()

    private lateinit var auth: FirebaseAuth
    private lateinit var user: LoginEntity

    @Inject
    lateinit var sharedPrefs: SharedPrefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        login()
        observe()
    }

    private fun login() {

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if(validate(email, password)) {
                loginFirebase(email, password)
            }
        }
    }

    private fun loginFirebase(email: String, password: String) {
        handleLoading(true)
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                handleLoading(false)
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    showToast("Authentication success.")
                    saveToken()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    showToast("Sign in failed.")
                }
            }
    }

    private fun saveToken() {
        val mUser = FirebaseAuth.getInstance().currentUser
        mUser!!.getIdToken(true)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val idToken: String? = task.result.token
                    sharedPrefs.saveToken(idToken ?: "")
                    goToMainActivity()
                } else {
                    showToast(task.exception.toString())
                    showToast("Authentication token failed.")
                }
            }
    }

    private fun validate(email: String, password: String) : Boolean{
        binding.emailEditText.error = null
        binding.passwordEditText.error = null

        if(email.isEmpty()) {
            binding.emailEditText.error = "Email is required."
            return false
        }
        if(!email.isEmail()) {
            binding.emailEditText.error = "Must be an email."
            return false
        }
        if(password.isEmpty()) {
            binding.passwordEditText.error = "Password is required."
            return false
        }

        return true
    }

    private fun observe(){
        viewModel.mState
            .flowWithLifecycle(lifecycle,  Lifecycle.State.STARTED)
            .onEach { state -> handleStateChange(state) }
            .launchIn(lifecycleScope)
    }

    private fun handleStateChange(state: LoginActivityState){
        when(state){
            is LoginActivityState.Init -> Unit
//            is LoginActivityState.ErrorLogin -> handleErrorLogin(state.rawResponse)
            is LoginActivityState.SuccessLogin -> handleSuccessLogin(state.loginEntity)
            is LoginActivityState.ShowToast -> showToast(state.message)
            is LoginActivityState.IsLoading -> handleLoading(state.isLoading)
        }
    }

    private fun handleSuccessLogin(loginEntity: LoginEntity){
        user = loginEntity
        sharedPrefs.saveToken(loginEntity.idToken)
        goToMainActivity()
    }

    private fun handleLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun goToMainActivity(){
        val i = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}