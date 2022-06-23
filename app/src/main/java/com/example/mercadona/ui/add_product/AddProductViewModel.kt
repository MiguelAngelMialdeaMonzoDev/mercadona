package com.example.mercadona.ui.add_product

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mercadona.R

class AddProductViewModel: ViewModel() {
    val name = MutableLiveData<String>()
    val price = MutableLiveData<String>()
    val weight = MutableLiveData<String>()
    val format = MutableLiveData<String>()
    val packaging = MutableLiveData<String>()

    val nameError = MutableLiveData<Int>()
    val priceError = MutableLiveData<Int>()
    val weightError = MutableLiveData<Int>()
    val formatError = MutableLiveData<Int>()
    val packagingError = MutableLiveData<Int>()

    val isAllFilled = MutableLiveData(false)

    fun checkName() {
        when {
            name.value.isNullOrEmpty() -> nameError.value = R.string.empty_field
            name.value.toString().length < 2 -> nameError.value = R.string.enough_lenght
            else -> nameError.value = 0
        }
    }

    fun checkPrice() {
        if (price.value.isNullOrEmpty()) {
            priceError.value = R.string.empty_field
        } else {
            priceError.value = 0
        }
    }

    fun checkWeight() {
        if (weight.value.isNullOrEmpty()) {
            weightError.value = R.string.empty_field
        } else {
            weightError.value = 0
        }
    }

    fun checkFormat() {
        if (format.value.isNullOrEmpty()) {
            formatError.value = R.string.empty_field
        } else {
            formatError.value = 0
        }
    }

    fun checkPackaging() {
        when {
            packaging.value.isNullOrEmpty() -> packagingError.value = R.string.empty_field
            packaging.value.toString().length < 2 -> packagingError.value = R.string.enough_lenght
            else -> packagingError.value = 0
        }
    }

    fun checkFieldsCorrect() {
        isAllFilled.value = nameError.value == 0 && priceError.value == 0 && weightError.value == 0 && formatError.value == 0 && packagingError.value == 0
    }
}