package com.xdeveloperss.fourbrothers.data.responses

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.xdeveloperss.fourbrothers.data.BaseResponseRepo
import com.xdeveloperss.fourbrothers.data.models.Media
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.models.PersonRate
import com.xdeveloperss.fourbrothers.data.models.Product
import com.xdeveloperss.fourbrothers.utils.format2
import com.xdeveloperss.fourbrothers.utils.formattedDate
import java.util.Date

data class DailyRates(
    val id:Int? = null,
    var zindarate:Int? = null,
    var chickenrate: Int? = null,
    var media: List<Media>? = null,
    @SerializedName("added_at")
    var addedAt:String? = Date().formattedDate(),)

data class OrderItems(
    val id: Int? = null,
    @SerializedName("daily_rate")
    var dailyRate: DailyRates,
    @SerializedName("products_id")
    var productsId: Int? = null,
    var personsId: Int? = null,
    @SerializedName("person_rates_id")
    var personRatesId: Int? = null,
    var weight: Double = 0.0,
    var total: Double = 0.0,
    @SerializedName("person_name")
    var personName: String,
    val product: Product,
    val person: Person,
    @SerializedName("item_rate")
    val itemRate: PersonRate,
    var media: List<Media>? = null,
    var modelType:String = "orderItems"
)


data class VasuliItem (
    val id: Long? = null,
    val usersID: Long? = null,
    @SerializedName("persons_id")
    var personsID: Long? = null,
    var person: Person? = null,
    var amount: Long? = null,
    @SerializedName("created_at")
    val createdAt: String = Date().format2(),
    @SerializedName("added_at")
    val addedAt: String = Date().formattedDate(),
    var media: MutableList<Media> = mutableListOf(),
)

