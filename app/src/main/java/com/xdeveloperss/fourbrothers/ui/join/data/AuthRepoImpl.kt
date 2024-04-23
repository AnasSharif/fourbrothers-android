package com.xdeveloperss.fourbrothers.ui.join.data

import com.blankj.utilcode.util.ToastUtils
import com.xdeveloperss.fourbrothers.xnetwork.config.models.AuthResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.repository.XBaseApiRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.server.ServerInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class AuthRepoImpl(private val api: ServerInterface): XBaseApiRepo(), AuthRepository {

    override suspend fun login(email: String, password: String): XNetworkResponse<AuthResponse> {
        return withContext(Dispatchers.IO){
            try {
                val loginResponse = async {
                    val response = safeApiCall {
                        api.login(email, password)
                    }
                    if (response is XNetworkResponse.Success){
                        response.value
                    }else{
                        response.getValueFromResponse() ?: AuthResponse()
                    }
                }
                val list = loginResponse.await()
                XNetworkResponse.Success(list)
            }catch (exception:Exception){
                XNetworkResponse.Failure(exception,exception.message,null)
            }
        }
    }

    override suspend fun logout(): Boolean {
        return withContext(Dispatchers.IO){
            try {
                val logoutService = async {
                    val response = safeApiCall {
                        api.logout()
                    }
                    if (response is XNetworkResponse.Success){
                        response.value
                    }else{
                        false
                    }
                }
                 logoutService.await()
            }catch (exception:Exception){
                ToastUtils.showShort(exception.message)
               false
            }
        }
    }
}