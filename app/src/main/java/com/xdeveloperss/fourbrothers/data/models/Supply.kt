package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName

data class Supply(
    val id: Int,
    val suppliersID: Int? = null,
    val vendorSuppliesID: Int? = null,
    val mandiRatesID: Int? = null,
    var rate: Int,
    var weight: Double,
    val date: String,
    val supplier: Supplier,
    @SerializedName("supplie_van")
    val supplieVan: SupplieVan? = null,
    @SerializedName("vendor_supplie")
    val vendorSupplie: VendorSupplie,
    val createdAt: String,
    val updatedAt: String
)

data class SupplieVan (
    val id: Int,
    val suppliesID: Long,
    val vendorSuppliesID: Long,
    val vehiclesID: Long? = null,
    val employeesID: Long? = null,
    val createdAt: String,
    val updatedAt: String,
    val van: Van? = null,
    val driver: Employees? = null,
)

data class Supplier (
    val id: Int,
    val personsID: Long,
    val name: String,
    val phonenumber: String,
    val createdAt: String,
    val updatedAt: String
)
data class VendorSupplie (
    val id: Long,
    val usersID: Double? = null,
    val vendorSuppliesVansID: Any? = null,
    val vendorSupplieTypeID: Long,
    val rate: Long,
    @SerializedName("mandi_rate")
    val mandiRate: Int,
    val weight: Double,
    val waste: Double,
    val saving: Double? = null,
    val date: String,
    val createdAt: String,
    val updatedAt: String,
    val items: List<VendorSupplieItems>,
    val media: List<Media>? = null,
    val expenses: List<VendorSupplieExpense>,
    val pickup: SupplieVan
)

data class VendorSupplieItems (
    val id: Long,
    var weight: Double,
    val rate: Long,
    val total: Long,
    val date: String,
    val createdAt: String,
    val updatedAt: String,
    val media: MutableList<Media>? = null,
    val vendor: Supplier
)
data class VendorSupplieExpense (
    val id: Long,
    var amount: Double,
    val createdAt: String,
    val type: SupplyExpenseType
)

data class SupplyExpenseType (
    val id: Long,
    val name: String,
    val createdAt: String,
    val updatedAt: String
)

data class Van (
    val id: Int,
    val number: String? = null,
    val name: String? = null,
    val createdAt: String,
    val updatedAt: String
)

data class Employees (
    val id: Long,
    val personsID: Long,
    val employeeTypesID: Long,
    val employeeSalariesID: Long,
    val name: String,
    val phonenumber: String,
    val address: Any? = null,
    val createdAt: String,
    val updatedAt: String
)

data class MandiRate (
    val id: Long? = null,
    var rate: Long? = null,
    @SerializedName("added_at")
    var addedAt: String? = null,
    val createdAt: String? = null,
)