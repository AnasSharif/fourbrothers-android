package com.xdeveloperss.fourbrothers.data.models

data class Product(
    val id: Long,
    val suppliesID: Any? = null,
    val name: String,
    val slug: String,
    val category: String,
    val createdAt: String,
    val updatedAt: String
)


data class ProductRate (
    val id: Long,
    val amount: Double,
    val type: Type,
)

data class Type (
    val id: Long,
    val name: String,
    val slug: String,
    val createdAt: String,
    val updatedAt: String
)