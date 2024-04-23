package com.xdeveloperss.fourbrothers.ui.main.ui.shop

import com.xdeveloperss.fourbrothers.data.responses.ShopResponse
import com.xdeveloperss.fourbrothers.xnetwork.config.response.XNetworkResponse

interface ShopRepo {
    suspend fun shopData(date: String): XNetworkResponse<ShopResponse>

}