package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName
import com.xdeveloperss.fourbrothers.utils.formattedDate
import java.util.Date
import java.util.function.DoubleUnaryOperator

data class Person (
    val id: Int,
    @SerializedName("persons_id")
    val personsID: Long? = null,
    val name: String,
    val phonenumber: String,
    val currentRateID: String? = null,
    val mainRoleID: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val products: List<PersonProduct>? = null,
    val payments: List<Payment>?= null,
    @SerializedName("kachra_payments")
    val kachraPayments:  MutableList<KachraPayment> = mutableListOf()
)

class PersonProduct(
    val id: Int,
    val product: Product,
    val rate: PersonRate
)

data class PersonRate (
    val id: Int,
    val personsID: Long,
    val rateTypesID: Long,
    val amount: Double,
    val type: Type? = null
)

data class Payment (
    val id: Long,
    val usersID: Any? = null,
    val personsID: Long,
    val personRolesID: Long? = null,
    val itemID: Long? = null,
    val transactionType: Long,
    val entryType: String,
    val amount: Long,
    val addedAt: String,
    val createdAt: String,
    val updatedAt: String
)

data class KachraPayment (
    val id: Int? = null,
    val usersID: Int? = null,
    @SerializedName("persons_id")
    var personsID: Long? = null,
    @SerializedName("shop_id")
    val shopID: Int? = null,
    @SerializedName("person_products_id")
    val personProductsID: Any? = null,
    var weight: Double? = null,
    var amount: Double? = null,
    @SerializedName("payment_type")
    var paymentType: String? = null,
    val desc: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = Date().formattedDate(),
    val product: Product? = null,
    //This is shop
    val person: Person? = null,
    val media: MutableList<Media> = mutableListOf()
)