package com.bangkit.hargain.presentation.common.helper

import androidx.recyclerview.widget.DiffUtil
import com.bangkit.hargain.domain.category.entity.CategoryEntity

class CategoryDiffCallbak(private val mOldCatefories: List<CategoryEntity>, private val mNewCategories: List<CategoryEntity>) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return mOldCatefories.size
    }

    override fun getNewListSize(): Int {
        return mNewCategories.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return mOldCatefories[oldItemPosition].id == mNewCategories[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = mOldCatefories[oldItemPosition]
        val newItem = mNewCategories[newItemPosition]
        return oldItem.name == newItem.name && oldItem.image == newItem.image
    }
}