package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName

data class Expense(
    val id: Int? = null,
    @SerializedName("expense_types_id")
    var expenseTypesID: Int? = null,
    var amount: Double? = null,
    var desc: String? = null,
    val createdAt: String? = null,
    @SerializedName("added_at")
    var addedAt: String? = null,
    val type: ExpenseType ? = null,
    val media: MutableList<Media> = mutableListOf()
)

data class ExpenseType (
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("created_at")
    val createdAt: String,
)