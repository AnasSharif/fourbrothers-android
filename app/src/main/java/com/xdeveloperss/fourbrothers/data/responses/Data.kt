package com.xdeveloperss.fourbrothers.data.responses

import com.xdeveloperss.fourbrothers.data.models.Person


data class Data(
    var dailyRates:List<DailyRates>,
    val orderItems: List<ShopItemData>,
    val stockItems: List<ShopItemData>,
    val persons: List<Person>)
