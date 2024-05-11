package com.xdeveloperss.fourbrothers.ui.main


import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import java.io.File

interface MainRepo {
    suspend fun getData(date:String? = null, types: List<String>): XNetworkResponse<BaseResponseRepo>

    suspend fun store(type: String, itemId:String, fileName: String, files:List<File>): XNetworkResponse<BaseResponseRepo>

    suspend fun<T> saveData(type: Class<T>?= null, stringType:String? = null, data: T): XNetworkResponse<BaseResponseRepo>

    suspend fun getData(queryParams: Map<String, String>, types: List<String>): XNetworkResponse<BaseResponseRepo>

}