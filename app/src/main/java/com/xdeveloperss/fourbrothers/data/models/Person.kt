package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName

data class Person (
    val id: Int,
    val name: String,
    val phonenumber: String,
    val currentRateID: String? = null,
    val mainRoleID: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val products: List<PersonProduct>? = null,
    val payments: List<Payment>?= null,
    @SerializedName("kachra_payments")
    val kachraPayments:  List<KachraPayment> = listOf()
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
    val id: Long,
    val usersID: Any? = null,
    val personsID: Long,
    val shopID: Any? = null,
    val personProductsID: Any? = null,
    val weight: Any? = null,
    val amount: Long,
    @SerializedName("payment_type")
    val paymentType: String,
    val desc: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    val updatedAt: String,
    val product: Product? = null,
    //This is shop
    val person: Person? = null,
    val media: MutableList<Media> = mutableListOf()
)