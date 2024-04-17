package com.xdeveloperss.fourbrothers.data
data class UserData (
    val id: Long,
    val name: String? = null,
    val username: String,
    val email: String? = null,
    val emailVerifiedAt: Any? = null,
    val isAdmin: Boolean,
    val createdAt: String,
    val updatedAt: String
)
