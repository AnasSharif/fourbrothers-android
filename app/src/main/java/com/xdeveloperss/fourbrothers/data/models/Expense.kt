package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName

data class Expense(
    val id: Int,
    @SerializedName("expense_types_id")
    val expenseTypesID: Int,
    var amount: Double,
    var desc: String,
    val createdAt: String,
    val updatedAt: String,
    val type: ExpenseType,
    val media: MutableList<Media> = mutableListOf()
)

data class ExpenseType (
    val id: Long,
    val name: String,
    val slug: String,
    val createdAt: String,
    val updatedAt: String
)