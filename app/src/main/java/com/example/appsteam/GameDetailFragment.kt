package com.example.appsteam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsteam.databinding.FragmentGameDetailBinding

class GameDetailFragment : Fragment() {

    private var _binding: FragmentGameDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gameId = arguments?.getString("gameId") ?: ""
        val game = GameRepository.getGameById(gameId)

        val gameTitle = game?.title ?: arguments?.getString("gameTitle") ?: "Jogo Sem Título"
        val gamePrice = game?.price ?: arguments?.getString("gamePrice") ?: "Grátis"
        val gameReview = game?.review ?: arguments?.getString("gameReview") ?: "Muito Positivas"
        val imageResId = game?.bannerResId?.takeIf { it != 0 }
            ?: game?.imageResId
            ?: arguments?.getInt("imageResId", 0)
            ?: 0
        val description = game?.description ?: arguments?.getString("gameDescription") ?: "Descrição do jogo indisponível no momento."
        val originalPrice = game?.originalPrice ?: arguments?.getString("gameOriginalPrice") ?: ""
        val discount = game?.discountPercent ?: arguments?.getString("gameDiscount") ?: ""
        val tags = game?.tags?.joinToString(" • ") ?: "Ação • Aventura"

        binding.tvDetailTitle.text = gameTitle
        binding.tvDetailPrice.text = gamePrice
        binding.tvDetailReview.text = gameReview
        binding.tvDetailDescription.text = description
        binding.tvDetailTags.text = tags

        if (imageResId != 0) {
            binding.imgDetailBanner.setImageResource(imageResId)
        }

        if (discount.isNotEmpty()) {
            binding.tvDetailDiscount.visibility = View.VISIBLE
            binding.tvDetailDiscount.text = discount
        } else {
            binding.tvDetailDiscount.visibility = View.GONE
        }

        if (originalPrice.isNotEmpty()) {
            binding.tvDetailOriginalPrice.visibility = View.VISIBLE
            binding.tvDetailOriginalPrice.text = originalPrice
        } else {
            binding.tvDetailOriginalPrice.visibility = View.GONE
        }

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnBuy.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Adicionado ao carrinho Steam: $gameTitle!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
