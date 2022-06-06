package com.bangkit.hargain.data.login.remote.api

import com.bangkit.hargain.data.login.remote.dto.LoginResponse
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface AuthAPI {

    @FormUrlEncoded
    @POST("v1/accounts:signInWithPassword")
    suspend fun login(
        @Query("key") key: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("returnSecureToken") returnSecureToken: Boolean
    ): Response<LoginResponse>
}