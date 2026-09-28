package com.example.appsteam

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appsteam.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {

    private var _binding: FragmentStoreBinding? = null
    private val binding get() = _binding!!

    private lateinit var storeGamesAdapter: StoreGamesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupCarousel()
        setupFeaturedRecommended()
        setupDiscountsEvents()
        setupBecauseYouPlayed()
        setupBrowseCategories()
        setupPopularGamesList()
        setupSearchFilter()
    }

    private fun setupCarousel() {
        val featuredCarouselGames = GameRepository.getFeaturedGames()
        val adapter = FeaturedCarouselAdapter(featuredCarouselGames) { game ->
            openGameDetail(game)
        }
        binding.vpFeaturedCarousel.adapter = adapter
    }

    private fun setupFeaturedRecommended() {
        val games = GameRepository.getFeaturedGames()
        binding.rvFeaturedRecommended.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvFeaturedRecommended.adapter = HorizontalGameAdapter(games) { game ->
            openGameDetail(game)
        }
    }

    private fun setupDiscountsEvents() {
        val games = GameRepository.getDiscountedGames()
        binding.rvDiscountsEvents.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvDiscountsEvents.adapter = HorizontalGameAdapter(games) { game ->
            openGameDetail(game)
        }
    }

    private fun setupBecauseYouPlayed() {
        val games = GameRepository.getBecausePlayedGames()
        binding.rvBecauseYouPlayed.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBecauseYouPlayed.adapter = HorizontalGameAdapter(games) { game ->
            openGameDetail(game)
        }
    }

    private fun setupBrowseCategories() {
        val categoryList = listOf(
            Category("Zombies", R.drawable.ic_gamepad),
            Category("Team-Based", R.drawable.ic_gamepad),
            Category("Fighting", R.drawable.ic_gamepad),
            Category("Competitive", R.drawable.ic_gamepad),
            Category("Action", R.drawable.ic_gamepad),
            Category("RPG", R.drawable.ic_gamepad),
            Category("Horror", R.drawable.ic_gamepad),
            Category("Survival", R.drawable.ic_gamepad),
            Category("FPS", R.drawable.ic_gamepad),
            Category("Open World", R.drawable.ic_gamepad),
            Category("Strategy", R.drawable.ic_gamepad)
        )

        binding.rvBrowseCategories.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvBrowseCategories.adapter = CategoryChipAdapter(categoryList) { category ->
            openCategoryDetail(category.name)
        }
    }

    private fun setupPopularGamesList() {
        val allGames = GameRepository.allGames
        storeGamesAdapter = StoreGamesAdapter(allGames) { game ->
            openGameDetail(game)
        }
        binding.rvPopularGames.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPopularGames.adapter = storeGamesAdapter
    }

    private fun setupSearchFilter() {
        binding.etSearchStore.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim() ?: ""
                val filtered = if (query.isEmpty()) {
                    GameRepository.allGames
                } else {
                    GameRepository.allGames.filter { game ->
                        game.title.contains(query, ignoreCase = true) ||
                                game.tags.any { it.contains(query, ignoreCase = true) }
                    }
                }
                storeGamesAdapter.updateList(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
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
        findNavController().navigate(R.id.action_storeFragment_to_gameDetailFragment, bundle)
    }

    private fun openCategoryDetail(categoryName: String) {
        val bundle = Bundle().apply {
            putString("categoryName", categoryName)
        }
        findNavController().navigate(R.id.action_storeFragment_to_categoryDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
