package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import com.xdeveloperss.fourbrothers.data.responses.ShopResponse
import com.xdeveloperss.fourbrothers.ui.join.data.AuthRepository
import com.xdeveloperss.fourbrothers.xnetwork.config.models.AuthResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.repository.XBaseApiRepo
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.response.getValueFromResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.server.ServerInterface
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class ShopRepoImpl(private val api: ServerInterface): XBaseApiRepo(), ShopRepo {
    override suspend fun shopData(date: String): XNetworkResponse<ShopResponse> {

        return withContext(Dispatchers.IO){
            try {
                val loginResponse = async {
                    val response = safeApiCall {
                        api.shopData(date)
                    }
                    if (response is XNetworkResponse.Success){
                        response.value
                    }else{
                        response.getValueFromResponse() ?: ShopResponse()
                    }
                }
                val list = loginResponse.await()
                XNetworkResponse.Success(list)
            }catch (exception:Exception){
                XNetworkResponse.Failure(exception,exception.message,null)
            }
        }
    }
}