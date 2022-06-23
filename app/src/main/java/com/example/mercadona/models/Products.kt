package com.example.mercadona.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Products(
    var id: String? = "",
    @SerializedName("display_name")
    var name: String? = "",
    @SerializedName("share_url")
    var url: String? = "",
    @SerializedName("thumbnail")
    var image: String? = "",
    var price_instructions: PriceInstructions,
    var packaging : String? = ""
) : Serializable