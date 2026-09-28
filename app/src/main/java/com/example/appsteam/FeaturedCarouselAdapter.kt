package com.example.appsteam

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.appsteam.databinding.ItemFeaturedCarouselBinding

class FeaturedCarouselAdapter(
    private val featuredGames: List<Game>,
    private val onGameClick: (Game) -> Unit
) : RecyclerView.Adapter<FeaturedCarouselAdapter.CarouselViewHolder>() {

    class CarouselViewHolder(val binding: ItemFeaturedCarouselBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val binding = ItemFeaturedCarouselBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CarouselViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        val game = featuredGames[position]
        with(holder.binding) {
            tvBannerTitle.text = game.title
            tvBannerReview.text = game.review
            tvBannerPrice.text = game.price
            imgBannerBg.setImageResource(game.bannerResId)

            if (!game.discountPercent.isNullOrEmpty()) {
                tvBannerDiscount.visibility = View.VISIBLE
                tvBannerDiscount.text = game.discountPercent
            } else {
                tvBannerDiscount.visibility = View.GONE
            }

            root.setOnClickListener {
                onGameClick(game)
            }
        }
    }

    override fun getItemCount(): Int = featuredGames.size
}
