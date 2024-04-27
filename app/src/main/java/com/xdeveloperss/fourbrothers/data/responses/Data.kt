package com.xdeveloperss.fourbrothers.data.responses

import com.xdeveloperss.fourbrothers.data.models.Person


data class Data(
    var dailyRates:DailyRates? = null,
    val orderItems: List<ShopItemData>,
    val stockItems: List<ShopItemData>,
    val persons: List<Person>)
