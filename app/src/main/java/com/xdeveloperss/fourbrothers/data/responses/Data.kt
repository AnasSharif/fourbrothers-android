package com.xdeveloperss.fourbrothers.data.responses

import com.xdeveloperss.fourbrothers.data.models.Person


data class Data(
    var dailyRates:DailyRates? = null,
    val orderItems: List<OrderItems>,
    val stockItems: List<OrderItems>,
    val persons: List<Person>)
