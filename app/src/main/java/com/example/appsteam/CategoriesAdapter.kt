package com.example.appsteam

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsteam.databinding.ItemCategoryBinding

class CategoriesAdapter(
    private var categoriesList: List<Category>,
    private val onCategoryClick: (Category) -> Unit
) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    class CategoryViewHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoriesList[position]
        with(holder.binding) {
            tvCategoryName.text = category.name
            imgCategoryIcon.setImageResource(category.iconResId)

            val count = GameRepository.getGamesByCategoryTag(category.name).size
            tvCategoryCount.text = holder.itemView.context.getString(R.string.category_game_count, count)

            root.setOnClickListener {
                onCategoryClick(category)
            }
        }
    }

    override fun getItemCount(): Int = categoriesList.size

    fun updateList(newList: List<Category>) {
        categoriesList = newList
        notifyDataSetChanged()
    }
}
