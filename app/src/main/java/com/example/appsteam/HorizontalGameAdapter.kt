package com.example.appsteam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsteam.databinding.ItemGameHorizontalBinding

class HorizontalGameAdapter(
    private val games: List<Game>,
    private val onGameClick: (Game) -> Unit
) : RecyclerView.Adapter<HorizontalGameAdapter.GameViewHolder>() {

    class GameViewHolder(val binding: ItemGameHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemGameHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        with(holder.binding) {
            tvGameTitle.text = game.title
            tvGameReview.text = game.review
            tvGamePrice.text = game.price
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
}
