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
import com.example.appsteam.databinding.FragmentLibraryBinding

class LibraryFragment : Fragment() {

    private var _binding: FragmentLibraryBinding? = null
    private val binding get() = _binding!!

    private lateinit var libraryGamesAdapter: LibraryGamesAdapter
    private var ownedGamesList: List<Game> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ownedGamesList = GameRepository.getOwnedGames()

        binding.tvLibraryCountTitle.text = getString(R.string.library_count_format, ownedGamesList.size)

        // Setup horizontal recent games
        val recentGames = ownedGamesList.take(6)
        binding.rvRecentLibraryGames.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvRecentLibraryGames.adapter = HorizontalGameAdapter(recentGames) { game ->
            openGameDetail(game)
        }

        // Setup vertical all owned games list
        libraryGamesAdapter = LibraryGamesAdapter(ownedGamesList) { game ->
            openGameDetail(game)
        }
        binding.rvLibraryGames.layoutManager = LinearLayoutManager(requireContext())
        binding.rvLibraryGames.adapter = libraryGamesAdapter

        // Setup search filter
        binding.etSearchLibrary.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim() ?: ""
                val filtered = if (query.isEmpty()) {
                    ownedGamesList
                } else {
                    ownedGamesList.filter { game ->
                        game.title.contains(query, ignoreCase = true) ||
                                game.tags.any { it.contains(query, ignoreCase = true) }
                    }
                }
                libraryGamesAdapter.updateList(filtered)
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
        findNavController().navigate(R.id.action_libraryFragment_to_gameDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
