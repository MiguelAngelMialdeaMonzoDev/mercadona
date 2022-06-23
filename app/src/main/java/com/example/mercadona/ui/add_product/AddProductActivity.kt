package com.example.mercadona.ui.add_product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.mercadona.R
import com.example.mercadona.databinding.ActivityAddProductBinding
import com.example.mercadona.helpers.Constants.FORMAT
import com.example.mercadona.helpers.Constants.NAME
import com.example.mercadona.helpers.Constants.PACKAGING
import com.example.mercadona.helpers.Constants.PRICE
import com.example.mercadona.helpers.Constants.WEIGHT

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private lateinit var viewModel: AddProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product)
        viewModel = ViewModelProvider(this).get(AddProductViewModel::class.java)

        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel

        initObservers()
        binding.toolbar.setOnClickListener { onBackPressed() }
    }

    private fun initObservers() {
        viewModel.name.observe(this) {
            viewModel.checkName()
            viewModel.checkFieldsCorrect()
        }

        viewModel.nameError.observe(this) { error ->
            binding.inputName.error = if (error != 0) getString(error) else null
        }

        viewModel.price.observe(this) {
            viewModel.checkPrice()
            viewModel.checkFieldsCorrect()
        }

        viewModel.priceError.observe(this) { error ->
            binding.inputPrice.error = if (error != 0) getString(error) else null
        }

        viewModel.weight.observe(this) {
            viewModel.checkWeight()
            viewModel.checkFieldsCorrect()
        }

        viewModel.weightError.observe(this) { error ->
            binding.inputWeight.error = if (error != 0) getString(error) else null
        }

        viewModel.format.observe(this) {
            viewModel.checkFormat()
            viewModel.checkFieldsCorrect()
        }

        viewModel.formatError.observe(this) { error ->
            binding.inputFormat.error = if (error != 0) getString(error) else null
        }

        viewModel.packaging.observe(this) {
            viewModel.checkPackaging()
            viewModel.checkFieldsCorrect()
        }

        viewModel.packagingError.observe(this) { error ->
            binding.inputPackaging.error = if (error != 0) getString(error) else null
        }
    }

    fun addProduct() {
        setResult(RESULT_OK, Intent().apply {
            putExtra(NAME, viewModel.name.value)
            putExtra(PRICE, viewModel.price.value)
            putExtra(WEIGHT, viewModel.weight.value)
            putExtra(FORMAT, viewModel.format.value)
            putExtra(PACKAGING, viewModel.packaging.value)
        })
        finish()
    }
}