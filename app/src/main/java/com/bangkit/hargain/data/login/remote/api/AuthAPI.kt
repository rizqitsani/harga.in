package com.bangkit.hargain.data.login.remote.api

import com.bangkit.hargain.data.login.remote.dto.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

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