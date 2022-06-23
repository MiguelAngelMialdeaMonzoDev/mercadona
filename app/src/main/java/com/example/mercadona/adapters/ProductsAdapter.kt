package com.example.mercadona.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mercadona.R
import com.example.mercadona.databinding.ItemProductBinding
import com.example.mercadona.models.Products

class ProductsAdapter(
    private val clickListener: ProductsClickListener
): ListAdapter<Products, ProductsAdapter.ViewHolder>(ListAdapterCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder constructor(
        val binding: ItemProductBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            item: Products,
            clickListener: ProductsClickListener
        ) {
            binding.product = item
            binding.clickListener = clickListener

            Glide.with(binding.root.context)
                .load(item.image)
                .placeholder(R.drawable.img_placeholder)
                .encodeQuality(100)
                .into(binding.imageProduct)

            binding.imageProduct.setOnClickListener {
                clickListener.onAddProduct(item)
            }
            binding.imageEdit.setOnClickListener {
                clickListener.onUpdateProduct(item)
            }
            binding.textPrice.text = binding.root.context.getString(R.string.text_price, item.price_instructions.price)
            binding.textWeight.text = binding.root.context.getString(R.string.weight_format, item.price_instructions.pricePerKg, item.price_instructions.format)
        }
    }

    class ListAdapterCallback : DiffUtil.ItemCallback<Products>() {
        override fun areItemsTheSame(
            oldItem: Products,
            newItem: Products
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Products,
            newItem: Products
        ): Boolean {
            return oldItem == newItem
        }
    }

    interface ProductsClickListener {
        fun onAddProduct(item: Products)
        fun onUpdateProduct(item: Products)
    }
}