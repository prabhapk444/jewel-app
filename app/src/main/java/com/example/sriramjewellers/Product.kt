package com.example.sriramjewellers

data class Product(
    val id: String = "",
    val category: String = "",
    val description: String = "",
    val image_url: String = "",
    val is_available: Boolean = false,
    val material: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val stock: Int = 0
)
