package com.xdeveloperss.fourbrothers.ui.main

import com.google.gson.Gson
import com.kongzue.dialogx.dialogs.WaitDialog
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.repository.XBaseApiRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
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
        WaitDialog.show("Loading Data...")
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
                        BaseResponseRepo(message=(apiCall as XNetworkResponse.Failure).message.toString())
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
        WaitDialog.show("Saving Data...")
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
                        val fail = response as XNetworkResponse.Failure
                        XNetworkResponse.Failure(IOException(), fail.message, fail.code)
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
        type: Class<T>?,
        stringType: String?,
        data: T
    ): XNetworkResponse<BaseResponseRepo> {
        WaitDialog.show("Saving Data...")
        return withContext(Dispatchers.IO){
            try {
                val saveData = async {
                    val className = type?.simpleName?.lowercase() ?: stringType ?: ""
                    val response = safeApiCall {
                        val params =  mutableMapOf(
                            "type" to className,
                            className to Gson().toJson(data))
                        api.saveData(params)
                    }
                    if (response is XNetworkResponse.Success){
                        if (response.value.success){
                            XNetworkResponse.Success(response.value)
                        }else{
                            XNetworkResponse.Failure(null,message = response.value.message, null)
                        }
                    }else{
                        val fail = response as XNetworkResponse.Failure
                        XNetworkResponse.Failure(IOException(), fail.message, fail.code)
                    }
                }
                saveData.await()
            }catch (exception:Exception){
                XNetworkResponse.Failure(exception, exception.message,1)
            }
        }
    }
}