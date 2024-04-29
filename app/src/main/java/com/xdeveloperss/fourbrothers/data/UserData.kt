package com.xdeveloperss.fourbrothers.data

import com.google.gson.annotations.SerializedName

data class UserData (
    val id: Long,
    val name: String? = null,
    val username: String,
    val email: String? = null,
    val emailVerifiedAt: Any? = null,
    @SerializedName("is_admin")
    val isAdmin: Boolean,
    val createdAt: String,
    val updatedAt: String
)
