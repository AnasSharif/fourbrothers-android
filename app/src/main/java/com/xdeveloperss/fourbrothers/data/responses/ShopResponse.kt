package com.xdeveloperss.fourbrothers.data.responses

import com.google.gson.annotations.SerializedName
import com.xdeveloperss.fourbrothers.data.BaseResponse


data class ShopResponse(override var success: Boolean = false,
                        override var message: String? = null,
                        val rate: DailyRates? = null,
                        val orders: List<ShopItemData> = listOf(),
                        val buyers: List<ShopItemData> = listOf()): BaseResponse()

data class DailyRates(val zindarate:Int, val chickenrate: Int)

data class ShopItemData(val id:Int? = null,
                        var weight: Double = 0.0,
                        var total: Double = 0.0,
                        @SerializedName("person_name") var personName: String)
