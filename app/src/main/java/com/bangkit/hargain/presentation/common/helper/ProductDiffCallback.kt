package com.bangkit.hargain.presentation.common.helper

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.hargain.domain.product.entity.ProductEntity

class ProductDiffCallback(private val mOldProducts: List<ProductEntity>, private val mNewProducts: List<ProductEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldProducts.size
    }

    override fun getNewListSize(): Int {
        return mNewProducts.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldProducts[oldItemPosition].id == mNewProducts[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldProducts[oldItemPosition]
        val newItem = mNewProducts[newItemPosition]
        return oldItem.title == newItem.title && oldItem.description == newItem.description && oldItem.image == newItem.image
    }
}