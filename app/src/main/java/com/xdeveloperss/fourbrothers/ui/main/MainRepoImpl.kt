package com.xdeveloperss.fourbrothers.ui.main

import com.google.gson.Gson
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.repository.XBaseApiRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.server.ServerInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.IOException

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

    override suspend fun store(
        type: String,
        itemId: String,
        fileName: String,
        files: List<File>
    ): XNetworkResponse<BaseResponseRepo> {
        return withContext(Dispatchers.IO){
            try {
                val servicesList = async {
                    val multipartArray = mutableListOf<MultipartBody.Part>()
                    for (file in files){
                        val requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
                        val body = MultipartBody.Part.createFormData("fileName[]", file.name, requestBody)
                        multipartArray.add(body)
                    }
                    val classType = RequestBody.create("text/plain".toMediaTypeOrNull(), type)
                    val requestItemId = RequestBody.create("text/plain".toMediaTypeOrNull(), itemId)
                    val response = safeApiCall {
                        api.store(classType, requestItemId, multipartArray.toTypedArray())
                    }
                    if (response is XNetworkResponse.Success && response.value.success){
                        XNetworkResponse.Success(response.value)
                    }else{
                        XNetworkResponse.Failure(IOException(), "Enable connecting to server please try again!",2)
                    }
                }
                val list = servicesList.await()
                XNetworkResponse.Success(list).value
            }catch (exception:Exception){
                XNetworkResponse.Failure(exception, exception.message,1)
            }
        }
    }

    override suspend fun <T> saveData(
        type: Class<T>,
        data: T
    ): XNetworkResponse<BaseResponseRepo> {
        return withContext(Dispatchers.IO){
            try {
                val servicesList = async {
                    val response = safeApiCall {
                        val params =  mutableMapOf(
                            "type" to type.simpleName.lowercase(),
                            type.simpleName.lowercase() to Gson().toJson(data))
                        api.saveData(params)
                    }
                    if (response is XNetworkResponse.Success && response.value.success){
                        XNetworkResponse.Success(response.value)
                    }else{
                        XNetworkResponse.Failure(IOException(), "Enable connecting to server please try again!",2)
                    }
                }
                servicesList.await()
            }catch (exception:Exception){
                XNetworkResponse.Failure(exception, exception.message,1)
            }
        }
    }
}