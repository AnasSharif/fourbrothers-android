package com.xdeveloperss.fourbrothers.data.responses

import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.ExpenseType
import com.xdeveloperss.fourbrothers.data.models.MandiRate
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.models.Product
import com.xdeveloperss.fourbrothers.data.models.Supply


data class Data(
    var dailyRates:DailyRates? = null,
    var mandiRates:MandiRate? = null,
    val orderItems: List<OrderItems> = listOf(),
    val stockItems: List<OrderItems> = listOf(),
    val vasuliItems: List<VasuliItem> = listOf(),
    val persons: List<Person> = listOf(),
    val supplies: List<Supply> = listOf(),
    val expenses: List<Expense> = listOf(),
    val expenseType: List<ExpenseType> = listOf(),
    val products: List<Product> = listOf(),
    val kachraBuyer: List<Person> = listOf(),
    )
