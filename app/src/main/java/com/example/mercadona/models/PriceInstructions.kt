package com.example.mercadona.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PriceInstructions(
    @SerializedName("size_format")
    var format: String? = "",
    @SerializedName("bulk_price")
    var pricePerKg: String? = "",
    @SerializedName("unit_price")
    var price: String? = ""
) : Serializable