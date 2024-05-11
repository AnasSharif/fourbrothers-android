package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName
import com.xdeveloperss.fourbrothers.utils.formattedServer
import java.util.Date

data class Person (
    val id: Long,
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
    val kachraPayments:  MutableList<KachraPayment>?= null,
)

class PersonProduct(
    val id: Long,
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
    var shopID: Long? = null,
    @SerializedName("person_products_id")
    var personProductsID: Long? = null,
    var weight: Double? = null,
    var amount: Double? = null,
    @SerializedName("payment_type")
    var paymentType: String? = null,
    val desc: String? = null,
    @SerializedName("created_at")
    val createdAt: String? = Date().formattedServer(),
    @SerializedName("person_product")
    var personProduct: PersonProduct? = null,
    //This is shop
    var person: Person? = null,
    val media: MutableList<Media> = mutableListOf()
)