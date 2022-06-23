package com.example.mercadona.models

data class Categories(
    var id: Int? = 0,
    var name: String? = "",
    var products: ArrayList<Products>? = null
)