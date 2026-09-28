package com.example.appsteam

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appsteam.databinding.FragmentCategoryDetailBinding

class CategoryDetailFragment : Fragment() {

    private var _binding: FragmentCategoryDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryName = arguments?.getString("categoryName") ?: "Zombies"

        binding.tvCategoryHeaderTitle.text = getString(R.string.category_header_format, categoryName)

        val filteredGames = GameRepository.getGamesByCategoryTag(categoryName)

        binding.tvCategoryFilteredTitle.text = getString(R.string.category_filtered_format, categoryName, filteredGames.size)

        // Horizontal Highlights
        val highlights = filteredGames.take(6)
        binding.rvCategoryHighlights.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvCategoryHighlights.adapter = HorizontalGameAdapter(highlights) { game ->
            openGameDetail(game)
        }

        // Vertical List
        binding.rvCategoryGames.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCategoryGames.adapter = StoreGamesAdapter(filteredGames) { game ->
            openGameDetail(game)
        }

        binding.btnCategoryBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun openGameDetail(game: Game) {
        val bundle = Bundle().apply {
            putString("gameId", game.id)
            putString("gameTitle", game.title)
            putString("gamePrice", game.price)
            putString("gameReview", game.review)
            putInt("imageResId", game.imageResId)
            putString("gameDescription", game.description)
            putString("gameOriginalPrice", game.originalPrice ?: "")
            putString("gameDiscount", game.discountPercent ?: "")
        }
        findNavController().navigate(R.id.action_categoryDetailFragment_to_gameDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
