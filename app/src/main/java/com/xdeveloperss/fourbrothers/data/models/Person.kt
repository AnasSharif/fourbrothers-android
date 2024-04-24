package com.xdeveloperss.fourbrothers.data.models

data class Person (
    val id: Int,
    val name: String,
    val phonenumber: String,
    val currentRateID: String? = null,
    val mainRoleID: String? = null,
    val createdAt: String,
    val updatedAt: String,
    val products: List<PersonProduct>? = null
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
