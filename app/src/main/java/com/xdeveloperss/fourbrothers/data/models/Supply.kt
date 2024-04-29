package com.xdeveloperss.fourbrothers.data.models

import com.google.gson.annotations.SerializedName

data class Supply(
    val id: Int,
    val suppliersID: Int,
    val vendorSuppliesID: Int,
    val mandiRatesID: Int? = null,
    var rate: Int,
    var weight: Double,
    val date: String,
    val supplier: Supplier,
    val supplieVan: SupplieVan,
    @SerializedName("vendor_supplie")
    val vendorSupplie: VendorSupplie,
    val createdAt: String,
    val updatedAt: String
)

data class SupplieVan (
    val id: Int,
    val suppliesID: Long,
    val vendorSuppliesID: Long,
    val vehiclesID: Any? = null,
    val employeesID: Any? = null,
    val createdAt: String,
    val updatedAt: String
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
    val usersID: Any? = null,
    val vendorSuppliesVansID: Any? = null,
    val vendorSupplieTypeID: Long,
    val rate: Long,
    @SerializedName("mandi_rate")
    val mandiRate: Int,
    val weight: Long,
    val waste: Long,
    val saving: Any? = null,
    val date: String,
    val createdAt: String,
    val updatedAt: String,
    val items: List<VendorSupplieItems>,
    val media: List<Media>? = null,
    val expenses: List<Expense>,
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
data class Expense (
    val id: Long,
    val vendorSuppliesID: Long,
    val vendorSupplieExpenseTypeID: Long,
    val amount: Long,
    val createdAt: String,
    val updatedAt: String
)

