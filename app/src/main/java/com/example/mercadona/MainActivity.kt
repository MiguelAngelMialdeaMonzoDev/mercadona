package com.example.mercadona

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.Window
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.mercadona.adapters.ProductsAdapter
import com.example.mercadona.databinding.ActivityMainBinding
import com.example.mercadona.helpers.Constants.FORMAT
import com.example.mercadona.helpers.Constants.ID
import com.example.mercadona.helpers.Constants.IMAGE
import com.example.mercadona.helpers.Constants.NAME
import com.example.mercadona.helpers.Constants.PACKAGING
import com.example.mercadona.helpers.Constants.PRICE
import com.example.mercadona.helpers.Constants.WEIGHT
import com.example.mercadona.models.PriceInstructions
import com.example.mercadona.models.Products
import com.example.mercadona.ui.add_product.AddProductActivity
import com.example.mercadona.ui.edit_product.EditProductActivity
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var productsAdapter: ProductsAdapter
    private lateinit var dialog: Dialog

    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == RESULT_OK) {
                when {
                    !activityResult.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                        .isNullOrEmpty() -> getVoice(activityResult)
                    !activityResult.data?.getStringExtra(ID).isNullOrEmpty() -> {
                        editProduct(activityResult)
                    }
                    else -> getProducts(activityResult)
                }
            }
        }

    private fun editProduct(activityResult: ActivityResult) {
        viewModel.productsList.value?.forEach { product ->
            val productName = activityResult.data?.getStringExtra(NAME)
            val productPrice = activityResult.data?.getStringExtra(PRICE)
            val productWeight = activityResult.data?.getStringExtra(WEIGHT)
            val productFormat = activityResult.data?.getStringExtra(FORMAT)
            val productPackaging = activityResult.data?.getStringExtra(PACKAGING)
            val productId = activityResult.data?.getStringExtra(ID)

            if (!productName.isNullOrEmpty() && !productPrice.isNullOrEmpty() && !productWeight.isNullOrEmpty() && !productFormat.isNullOrEmpty() && !productPackaging.isNullOrEmpty()) {
                viewModel.productsList.value?.forEach { product ->
                    if (product.id?.equals(productId) == true) {
                        product.name = productName
                        product.price_instructions.price = productPrice
                        product.price_instructions.pricePerKg = productWeight
                        product.price_instructions.format = productFormat
                        product.packaging = productPackaging
                    }
                }
                productsAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun getProducts(activityResult: ActivityResult) {
        val productName = activityResult.data?.getStringExtra(NAME)
        val productPrice = activityResult.data?.getStringExtra(PRICE)
        val productWeight = activityResult.data?.getStringExtra(WEIGHT)
        val productFormat = activityResult.data?.getStringExtra(FORMAT)
        val productPackaging = activityResult.data?.getStringExtra(PACKAGING)

        if (!productName.isNullOrEmpty() && !productPrice.isNullOrEmpty() && !productWeight.isNullOrEmpty() && !productFormat.isNullOrEmpty() && !productPackaging.isNullOrEmpty()) {
            val priceInstructions = PriceInstructions(productFormat, productWeight, productPrice)
            val id = viewModel.productsList.value?.size?.plus(1)
            val product = Products(id.toString(), productName, "", "", priceInstructions, productPackaging)
            viewModel.productsList.value?.add(product)
            productsAdapter.submitList(viewModel.productsList.value)
        }
    }

    private fun getVoice(activityResult: ActivityResult) {
        val arrayList =
            activityResult.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
        var voice = arrayList?.get(0)
        binding.searchProducts.onActionViewExpanded()
        binding.searchProducts.setQuery("" + voice, true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        setTheme(R.style.Theme_Mercadona)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        binding.lifecycleOwner = this
        binding.activity = this
        binding.viewModel = viewModel

        viewModel.getAllProducts()
        initAdapter()
        initObservers()
        initSearchView()
    }

    private fun initSearchView() {
        binding.searchProducts.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()) {
                    val listFiltered = viewModel.productsList.value?.filter { coin ->
                        coin.name?.lowercase()?.contains(newText.lowercase()) == true
                    }
                    productsAdapter.submitList(listFiltered)
                } else {
                    productsAdapter.submitList(viewModel.productsList.value)
                }
                return false
            }
        })
    }

    private fun initAdapter() {
        productsAdapter = ProductsAdapter(object : ProductsAdapter.ProductsClickListener {
            override fun onAddProduct(item: Products) {
                showModal(item)
            }

            override fun onUpdateProduct(item: Products) {
                goToEditProduct(item)
            }
        })

        binding.recyclerItems.adapter = productsAdapter
    }

    private fun showModal(item: Products) {
        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)
        getData(item)

        dialog.show()
    }

    private fun getData(item: Products) {
        val imageProduct = dialog.findViewById(R.id.image_products) as ImageView
        val textTitle = dialog.findViewById(R.id.text_title) as TextView
        val textPrice = dialog.findViewById(R.id.text_price) as TextView
        val textFormat = dialog.findViewById(R.id.text_format) as TextView
        val textUrl = dialog.findViewById(R.id.button_url) as MaterialButton
        val textPackaging = dialog.findViewById(R.id.text_packaging) as TextView

        textTitle.text = item.name
        textPrice.text = getString(R.string.text_price, item.price_instructions.price)
        textFormat.text = getString(
            R.string.weight_format,
            item.price_instructions.pricePerKg,
            item.price_instructions.format
        )
        textPackaging.text = item.packaging
        textUrl.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.url)))
        }
        Glide.with(binding.root.context)
            .load(item.image)
            .encodeQuality(100)
            .into(imageProduct)
    }

    private fun initObservers() {
        viewModel.productsList.observe(this) { productsList ->
            productsAdapter.submitList(productsList)
        }
    }

    fun openVoiceDialog() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        responseLauncher.launch(intent)
    }

    fun goToAddProduct() {

        responseLauncher.launch(Intent(this, AddProductActivity::class.java))
    }

    fun goToEditProduct(item: Products) {
        val intent = Intent(this, EditProductActivity::class.java).apply {
            putExtra(NAME, item.name)
            putExtra(PRICE, item.price_instructions.price)
            putExtra(WEIGHT, item.price_instructions.pricePerKg)
            putExtra(FORMAT, item.price_instructions.format)
            putExtra(ID, item.id)
            putExtra(PACKAGING, item.packaging)
            putExtra(IMAGE, item.image)
        }
        responseLauncher.launch(intent)
    }
}