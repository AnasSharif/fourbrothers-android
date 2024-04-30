package com.xdeveloperss.fourbrothers.data.responses

import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.ExpenseType
import com.xdeveloperss.fourbrothers.data.models.Person
import com.xdeveloperss.fourbrothers.data.models.Product
import com.xdeveloperss.fourbrothers.data.models.Supply


data class Data(
    var dailyRates:DailyRates? = null,
    val orderItems: List<OrderItems>,
    val stockItems: List<OrderItems>,
    val persons: List<Person>,
    val supplies: List<Supply>,
    val expenses: List<Expense>,
    val expenseType: List<ExpenseType>,
    val products: List<Product>,
    val kachraBuyer: List<Person>,
    )
