package com.xdeveloperss.fourbrothers.xnetwork.config.repository

import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

/**
 *@Author: Anas Sharif
 *@Email: anassharif1992@gmail.com
 *@Date: 29/08/2022
 */
abstract class XBaseApiRepo {
    suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ): XNetworkResponse<T> {
        return withContext(Dispatchers.IO){
            try {
                XNetworkResponse.Success(apiCall.invoke())
            } catch (throwable: Exception) {
                when (throwable) {
                    is HttpException -> {
                        XNetworkResponse.Failure(throwable, throwable.message, throwable.code())
                    }else -> {
                        XNetworkResponse.Failure(throwable, throwable.message, null)
                    }
                }
            }
        }
    }
}