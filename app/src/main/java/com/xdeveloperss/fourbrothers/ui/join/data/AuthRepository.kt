package com.xdeveloperss.fourbrothers.ui.join.data

import com.xdeveloperss.fourbrothers.xnetwork.config.models.AuthResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse


interface AuthRepository {

    suspend fun login(email: String, password: String): XNetworkResponse<AuthResponse>

    suspend fun logout(): Boolean
}