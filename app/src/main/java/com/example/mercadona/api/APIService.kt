package com.example.mercadona.api

import com.example.mercadona.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    @GET("categories/92")
    suspend fun getProducts(): Response<Product>
}