package com.example.mercadona.ui.edit_product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mercadona.R
import com.example.mercadona.databinding.ActivityEditProductBinding
import com.example.mercadona.helpers.Constants.FORMAT
import com.example.mercadona.helpers.Constants.ID
import com.example.mercadona.helpers.Constants.IMAGE
import com.example.mercadona.helpers.Constants.NAME
import com.example.mercadona.helpers.Constants.PACKAGING
import com.example.mercadona.helpers.Constants.PRICE
import com.example.mercadona.helpers.Constants.PRODUCT
import com.example.mercadona.helpers.Constants.PRODUCT_RESPONSE
import com.example.mercadona.helpers.Constants.WEIGHT

class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductBinding
    private lateinit var viewModel: EditProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_product)
        viewModel = ViewModelProvider(this).get(EditProductViewModel::class.java)

        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel

        initObservers()
        getData()
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

    private fun getData() {
        viewModel.name.value = intent.getStringExtra(NAME)
        viewModel.price.value = intent.getStringExtra(PRICE)
        viewModel.weight.value = intent.getStringExtra(WEIGHT)
        viewModel.format.value = intent.getStringExtra(FORMAT)
        viewModel.packaging.value = intent.getStringExtra(PACKAGING)
        viewModel.id.value = intent.getStringExtra(ID)
        val image = intent.getStringExtra(IMAGE)

        Glide.with(binding.root.context)
            .load(image)
            .placeholder(R.drawable.img_placeholder)
            .encodeQuality(100)
            .into(binding.imageProduct)
    }

    fun editProduct() {
        setResult(RESULT_OK, Intent().apply {
            putExtra(PRODUCT, PRODUCT_RESPONSE)
            putExtra(NAME, viewModel.name.value)
            putExtra(PRICE, viewModel.price.value)
            putExtra(WEIGHT, viewModel.weight.value)
            putExtra(FORMAT, viewModel.format.value)
            putExtra(PACKAGING, viewModel.packaging.value)
            putExtra(ID, viewModel.id.value)
        })
        finish()
    }
}