package com.xdeveloperss.fourbrothers.ui.main

import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.repository.XBaseApiRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.server.ServerInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class MainRepoImpl(private val api: ServerInterface): XBaseApiRepo(), MainRepo {
    override suspend fun getData(
        date: String?,
        types: List<String>
    ): XNetworkResponse<BaseResponseRepo> {
        return withContext(Dispatchers.IO){
            try {
                val loginResponse = async {
                    val apiCall = safeApiCall {
                        api.getData(date, types)
                    }
                    if (apiCall is XNetworkResponse.Success){
                        if (!apiCall.value.success){
                            BaseResponseRepo(message = apiCall.value.message)
                        }else{
                            apiCall.value
                        }
                    }else{
                        BaseResponseRepo()
                    }
                }
                val response = loginResponse.await()
                if (!response.success){
                    XNetworkResponse.Failure(Exception(), response.message, response.errorCode)
                }else{
                    XNetworkResponse.Success(response)
                }
            }catch (exception:Exception){
                XNetworkResponse.Failure(exception, exception.message,null)
            }
        }
    }
}