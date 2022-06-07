package com.bangkit.hargain.data.common.utils

import android.util.Log
import com.bangkit.hargain.infra.utils.SharedPrefs
import com.bangkit.hargain.presentation.common.extension.TAG
import com.google.firebase.auth.GetTokenResult
import okhttp3.Interceptor
import okhttp3.Response

class RequestInterceptor constructor(private val pref: SharedPrefs) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = pref.getToken()
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()
        return chain.proceed(newRequest)
    }
}