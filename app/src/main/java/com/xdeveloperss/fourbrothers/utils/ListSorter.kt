package com.xdeveloperss.fourbrothers.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object ListSorter {

    fun <T>filterObjectsByMonth(objects: List<T>,
                                memberName:String = "createdAt",
                                targetMonth: String, targetYear: String): List<T> {
        val filteredList = mutableListOf<T>()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        val targetCalendar = Calendar.getInstance()
        val targetMonthFormat = SimpleDateFormat("MMMM", Locale.getDefault())
        // Set target month
        targetCalendar.time = targetMonthFormat.parse(targetMonth) ?: return emptyList()
        for (obj in objects) {
            val createdAtProperty = obj!!::class.members.find { it.name == memberName} ?: throw IllegalArgumentException("Object does not have 'createdAt' property")
            val createdAt = (createdAtProperty.call(obj) as? String) ?: throw IllegalArgumentException("'createdAt' property is null or not a String")

            val calendar = Calendar.getInstance()
            calendar.time = dateFormat.parse(createdAt) ?: continue
            val objYear = calendar.get(Calendar.YEAR).toString()
            if (calendar.get(Calendar.MONTH) == targetCalendar.get(Calendar.MONTH) && objYear == targetYear) {
                filteredList.add(obj)
            }
        }
        return filteredList
    }
}