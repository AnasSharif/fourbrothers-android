package com.xdeveloperss.fourbrothers.ui.main


import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse

interface MainRepo {
    suspend fun getData(date:String? = null, types: List<String>): XNetworkResponse<BaseResponseRepo>

}