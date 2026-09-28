package com.example.appsteam

data class Game(
    val id: String,
    val title: String,
    val price: String,
    val originalPrice: String? = null,
    val discountPercent: String? = null,
    val review: String = "Muito Positivas",
    val imageResId: Int = R.drawable.img_placeholder_game,
    val bannerResId: Int = R.drawable.img_placeholder_banner,
    val tags: List<String> = emptyList(),
    val description: String = "",
    val isOwned: Boolean = false,
    var isInstalled: Boolean = false,
    val hoursPlayed: String? = null,
    val isFeatured: Boolean = false,
    val isDiscounted: Boolean = false,
    val becausePlayed: Boolean = false
)
