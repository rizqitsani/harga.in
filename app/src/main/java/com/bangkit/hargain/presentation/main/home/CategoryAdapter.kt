package com.bangkit.hargain.presentation.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.hargain.databinding.CardCategoryBinding
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.presentation.common.helper.CategoryDiffCallback
import com.bumptech.glide.Glide

class CategoryAdapter(private val categories: MutableList<CategoryEntity>): RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    interface OnItemTap {
        fun onTap(category: CategoryEntity)
    }

    fun setOnItemTapListener(l: OnItemTap) {
        onItemTapListener = l
    }

    private var onItemTapListener: OnItemTap? = null

    fun setCategories(categories: List<CategoryEntity>) {
        val diffCallback = CategoryDiffCallback(this.categories, categories)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.categories.clear()
        this.categories.addAll(categories)
        diffResult.dispatchUpdatesTo(this)
    }

    class ViewHolder(val itemBinding: CardCategoryBinding) : RecyclerView.ViewHolder(itemBinding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        Glide.with(holder.itemView.context)
            .load(category.image)
            .override(80, 80)
            .centerCrop()
            .into(holder.itemBinding.categoryImage)
        holder.itemBinding.nameTextView.text = category.name

        holder.itemBinding.root.setOnClickListener {
            onItemTapListener?.onTap(category)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}