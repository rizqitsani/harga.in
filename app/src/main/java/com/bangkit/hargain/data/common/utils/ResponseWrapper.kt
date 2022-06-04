package com.bangkit.hargain.data.common.utils

import com.google.gson.annotations.SerializedName

data class WrappedListResponse<T>(
    @SerializedName("error") var error: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: List<T>? = null
)

data class WrappedResponse<T>(
    @SerializedName("error") var error: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: T? = null
)