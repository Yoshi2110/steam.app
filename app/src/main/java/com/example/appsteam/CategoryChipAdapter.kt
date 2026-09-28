package com.example.appsteam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsteam.databinding.ItemCategoryChipBinding

class CategoryChipAdapter(
    private val categories: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoryChipAdapter.ChipViewHolder>() {

    class ChipViewHolder(val binding: ItemCategoryChipBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChipViewHolder {
        val binding = ItemCategoryChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ChipViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChipViewHolder, position: Int) {
        val category = categories[position]
        with(holder.binding) {
            tvChipName.text = category.name
            imgChipIcon.setImageResource(category.iconResId)

            root.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    override fun getItemCount(): Int = categories.size
}
