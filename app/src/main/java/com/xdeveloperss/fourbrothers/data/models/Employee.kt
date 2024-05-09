package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName

data class Employee (
    val id: Long,
    val personsID: Long,
    val employeeTypesID: Long? = null,
    val employeeSalariesID: Long? = null,
    val name: String,
    val phonenumber: String,
    val address: Any? = null,
    val createdAt: String,
    val updatedAt: String,
    val type: EmployeeType? = null,
    @SerializedName("current_salary")
    val currentSalary: CurrentSalary? = null,
    val payments: List<SalaryPayment>?
)


data class SalaryPayment (
    val id: Long,
    val employeeSalariesID: Long,
    val employeesID: Long,
    val amount: Long,
    val detail: String,
    val createdAt: String,
    val updatedAt: String
)


data class CurrentSalary (
    val id: Long,
    val employeesID: Long,
    val salary: Long,
    val increment: Any? = null,
    val date: String,
    val createdAt: String,
    val updatedAt: String
)

data class EmployeeType (
    val id: Long,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)