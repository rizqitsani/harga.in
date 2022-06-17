package com.bangkit.hargain.presentation.main.mainmenu.mainsearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.hargain.R
import com.bangkit.hargain.databinding.ProductRowBinding
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.helper.ProductDiffCallback
import com.bangkit.hargain.presentation.common.helper.formatCurrency
import com.bumptech.glide.Glide

class ProductAdapter(private val products: MutableList<ProductEntity>) :
    RecyclerView.Adapter<ProductAdapter.ViewHolder>() {

    interface OnItemTap {
        fun onTap(product: ProductEntity)
    }

    fun setOnItemTapListener(l: OnItemTap) {
        onItemTapListener = l
    }

    private var onItemTapListener: OnItemTap? = null

    fun setProducts(products: List<ProductEntity>) {
        val diffCallback = ProductDiffCallback(this.products, products)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.products.clear()
        this.products.addAll(products)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(val itemBinding: ProductRowBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ProductRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]

        Glide.with(holder.itemView.context)
            .load(product.image)
            .override(80, 80)
            .centerCrop()
            .into(holder.itemBinding.imageProduct)
        holder.itemBinding.textViewProductName.text = product.title
        holder.itemBinding.textViewProductOptimalPrice.text =
            holder.itemView.resources.getString(R.string.product_price, formatCurrency(product.optimalPrice))

        holder.itemBinding.root.setOnClickListener {
            onItemTapListener?.onTap(product)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

}