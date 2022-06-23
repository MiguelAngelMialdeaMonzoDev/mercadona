package com.example.mercadona

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mercadona.api.APIService
import com.example.mercadona.api.Retrofit
import com.example.mercadona.models.Categories
import com.example.mercadona.models.Products
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    val retrofit = Retrofit()
    val categoriesList = MutableLiveData<ArrayList<Categories>>()
    val completeList = ArrayList<Products>()
    var productsList = MutableLiveData<ArrayList<Products>>()

    fun getAllProducts() {
        viewModelScope.launch {
            val call = retrofit.getRetrofit().create(APIService::class.java).getProducts()
            val body = call.body()
            if (call.isSuccessful) {
                categoriesList.value = body?.categories as ArrayList<Categories>
                categoriesList.value?.forEach {
                    it.products?.forEach {
                        completeList.add(it)
                    }
                }
                productsList.value = completeList
            } else {

            }
        }
    }
}