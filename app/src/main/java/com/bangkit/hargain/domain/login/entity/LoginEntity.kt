package com.bangkit.hargain.domain.login.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginEntity(
//    var id: Int,
    var name: String,
    var email: String,
    var idToken: String
): Parcelable