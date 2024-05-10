package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName

data class Product(
    val id: Long? = null,
    var name: String? = null,
    var slug: String? = null,
    val category: String = "chicken",
    @SerializedName("created_at")
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val media: MutableList<Media> = mutableListOf()
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