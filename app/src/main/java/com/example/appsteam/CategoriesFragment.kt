package com.example.appsteam

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.appsteam.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoriesAdapter: CategoriesAdapter

    private val allCategories = listOf(
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
        Category("Strategy", R.drawable.ic_gamepad),
        Category("Co-op", R.drawable.ic_gamepad),
        Category("Racing", R.drawable.ic_gamepad),
        Category("Simulation", R.drawable.ic_gamepad),
        Category("Sports", R.drawable.ic_gamepad),
        Category("Indie", R.drawable.ic_gamepad)
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        setupSpinner()
        setupFilterInput()

        binding.cardGamerEvents.setOnClickListener {
            findNavController().navigate(R.id.action_categoriesFragment_to_gamerEventsFragment)
        }
    }

    private fun setupAdapter() {
        categoriesAdapter = CategoriesAdapter(allCategories) { category ->
            openCategoryDetail(category.name)
        }
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun setupSpinner() {
        val spinnerItems = listOf("Todas as Categorias") + allCategories.map { it.name }
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            spinnerItems
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.spinnerCategories.adapter = spinnerAdapter

        binding.spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if (position > 0) {
                    val selectedCategoryName = allCategories[position - 1].name
                    openCategoryDetail(selectedCategoryName)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun setupFilterInput() {
        binding.etFilterCategories.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s?.toString()?.trim() ?: ""
                val filtered = if (query.isEmpty()) {
                    allCategories
                } else {
                    allCategories.filter { it.name.contains(query, ignoreCase = true) }
                }
                categoriesAdapter.updateList(filtered)
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun openCategoryDetail(categoryName: String) {
        val bundle = Bundle().apply {
            putString("categoryName", categoryName)
        }
        findNavController().navigate(R.id.action_categoriesFragment_to_categoryDetailFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
