package com.xdeveloperss.fourbrothers.data.responses

import com.google.gson.annotations.SerializedName
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.models.PersonRate
import com.xdeveloperss.fourbrothers.data.models.Product

data class DailyRates(val zindarate:Int, val chickenrate: Int)

data class ShopItemData(val id:Int? = null,
                        @SerializedName("daily_rate")
                        var dailyRate: DailyRates,
                        var weight: Double = 0.0,
                        var total: Double = 0.0,
                        @SerializedName("person_name")
                        var personName: String,
                        val product: Product,
                        val person: Person,
                        @SerializedName("item_rate")
                        val itemRate: PersonRate,
)
