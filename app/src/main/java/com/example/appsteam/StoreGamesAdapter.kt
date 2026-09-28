package com.example.appsteam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsteam.databinding.ItemGameStoreBinding

class StoreGamesAdapter(
    private var games: List<Game>,
    private val onGameClick: (Game) -> Unit
) : RecyclerView.Adapter<StoreGamesAdapter.StoreGameViewHolder>() {

    class StoreGameViewHolder(val binding: ItemGameStoreBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreGameViewHolder {
        val binding = ItemGameStoreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return StoreGameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoreGameViewHolder, position: Int) {
        val game = games[position]
        with(holder.binding) {
            tvGameTitle.text = game.title
            tvGameReview.text = game.review
            tvGamePrice.text = game.price
            tvGameTags.text = game.tags.joinToString(" • ")
            imgGameCover.setImageResource(game.imageResId)

            if (!game.discountPercent.isNullOrEmpty()) {
                tvDiscountPercent.visibility = View.VISIBLE
                tvDiscountPercent.text = game.discountPercent
            } else {
                tvDiscountPercent.visibility = View.GONE
            }

            if (!game.originalPrice.isNullOrEmpty()) {
                tvOriginalPrice.visibility = View.VISIBLE
                tvOriginalPrice.text = game.originalPrice
            } else {
                tvOriginalPrice.visibility = View.GONE
            }

            root.setOnClickListener {
                onGameClick(game)
            }
        }
    }

    override fun getItemCount(): Int = games.size

    fun updateList(newGames: List<Game>) {
        games = newGames
        notifyDataSetChanged()
    }
}
