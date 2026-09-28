package com.example.appsteam

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.ConsoleMessage
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.appsteam.databinding.FragmentGamerEventsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

class GamerEventsFragment : Fragment() {

    private var _binding: FragmentGamerEventsBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false
        val coarseLocationGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false

        if (fineLocationGranted || coarseLocationGranted) {
            getUserLocation()
        } else {
            Toast.makeText(
                requireContext(),
                "Permissão de localização negada.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamerEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Configuração Obrigatória da WebView
        val webSettings = binding.webViewMap.settings
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true

        binding.webViewMap.webChromeClient = object : WebChromeClient() {
            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
                Log.d(
                    "WebViewConsole",
                    "${consoleMessage?.message()} -- Executado na linha ${consoleMessage?.lineNumber()} de ${consoleMessage?.sourceId()}"
                )
                return true
            }
        }
        binding.webViewMap.webViewClient = WebViewClient()

        // Carrega o mapa HTML local
        binding.webViewMap.loadUrl("file:///android_asset/gamer_map.html")

        binding.btnMyLocation.setOnClickListener {
            checkLocationPermissionAndFetch()
        }
    }

    private fun checkLocationPermissionAndFetch() {
        val finePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarsePermission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        if ((finePermission == PackageManager.PERMISSION_GRANTED) || (coarsePermission == PackageManager.PERMISSION_GRANTED)) {
            getUserLocation()
        } else {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        Toast.makeText(requireContext(), "Obtendo localização GPS...", Toast.LENGTH_SHORT).show()

        val cancellationTokenSource = CancellationTokenSource()
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude

                binding.tvCoordinates.text = getString(R.string.user_coordinates_format, lat, lng)

                val jsCommand = "javascript:updateLocation($lat, $lng)"
                binding.webViewMap.evaluateJavascript(jsCommand, null)

                Toast.makeText(requireContext(), "Localização atualizada no mapa!", Toast.LENGTH_SHORT).show()
            } else {
                fusedLocationClient.lastLocation.addOnSuccessListener { lastLoc ->
                    if (lastLoc != null) {
                        val lat = lastLoc.latitude
                        val lng = lastLoc.longitude
                        binding.tvCoordinates.text = getString(R.string.user_coordinates_format, lat, lng)
                        binding.webViewMap.evaluateJavascript("javascript:updateLocation($lat, $lng)", null)
                        Toast.makeText(requireContext(), "Localização atualizada!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(
                            requireContext(),
                            "Não foi possível obter a localização. Verifique se o GPS está ativado.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(
                requireContext(),
                "Erro ao obter localização: ${exception.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
