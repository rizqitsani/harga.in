package com.bangkit.hargain.data.login.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("expiresIn")
	val expiresIn: String,

	@field:SerializedName("kind")
	val kind: String,

	@field:SerializedName("displayName")
	val displayName: String,

	@field:SerializedName("idToken")
	val idToken: String,

	@field:SerializedName("registered")
	val registered: Boolean,

	@field:SerializedName("localId")
	val localId: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("refreshToken")
	val refreshToken: String
)
