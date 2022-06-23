package com.example.mercadona.models

data class Product(
    var id: Int? = 0,
    var name: String? = "",
    var categories: ArrayList<Categories>? = null
)