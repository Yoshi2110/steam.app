package com.example.appsteam

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.appsteam.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        // Destination listener to show/hide bottom nav and top header when viewing game detail or category detail
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.gameDetailFragment -> {
                    binding.bottomNavigationView.visibility = View.GONE
                    binding.headerLayout.visibility = View.GONE
                }
                R.id.categoryDetailFragment -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.headerLayout.visibility = View.VISIBLE
                }
                else -> {
                    binding.bottomNavigationView.visibility = View.VISIBLE
                    binding.headerLayout.visibility = View.VISIBLE
                }
            }
        }

        // Header click listeners
        binding.btnHeaderHome.setOnClickListener {
            navController.navigate(R.id.storeFragment)
        }

        binding.btnHeaderMenu.setOnClickListener {
            Toast.makeText(this, "Menu Principal Steam", Toast.LENGTH_SHORT).show()
        }

        binding.btnHeaderWishlist.setOnClickListener {
            Toast.makeText(this, "Lista de Desejos (Wishlist): 12 itens salvos", Toast.LENGTH_SHORT).show()
        }

        binding.btnHeaderWallet.setOnClickListener {
            Toast.makeText(this, "Carteira Steam: R$ 150,00 disponíveis", Toast.LENGTH_SHORT).show()
        }

        binding.btnHeaderSearch.setOnClickListener {
            navController.navigate(R.id.storeFragment)
        }
    }
}
