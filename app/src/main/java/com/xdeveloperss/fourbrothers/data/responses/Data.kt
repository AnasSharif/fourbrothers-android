package com.xdeveloperss.fourbrothers.data.responses

import com.xdeveloperss.fourbrothers.data.models.Employee
import com.xdeveloperss.fourbrothers.data.models.Expense
import com.xdeveloperss.fourbrothers.data.models.ExpenseType
import com.xdeveloperss.fourbrothers.data.models.KachraPayment
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
    val employees: List<Employee> = listOf(),
    val supplies: List<Supply> = listOf(),
    val expenses: List<Expense>? = null,
    val expenseType: List<ExpenseType> = listOf(),
    val products: List<Product> = listOf(),
    val kachraBuyer: List<Person> = listOf(),
    val shop: List<Person> = listOf(),
    val dailyKacharaPayment: List<KachraPayment>? = null,
    )
