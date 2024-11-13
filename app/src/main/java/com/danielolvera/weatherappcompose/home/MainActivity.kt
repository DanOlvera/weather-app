package com.danielolvera.weatherappcompose.home

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.danielolvera.weatherappcompose.BuildConfig
import com.danielolvera.weatherappcompose.home.intent.WeatherIntent
import com.danielolvera.weatherappcompose.home.model.network.ApiServiceClient
import com.danielolvera.weatherappcompose.home.model.repo.WeatherRepository
import com.danielolvera.weatherappcompose.home.theme.WeatherAppComposeTheme
import com.danielolvera.weatherappcompose.home.view.WeatherScreen
import com.danielolvera.weatherappcompose.home.view.utils.SharedPreferencesManager
import com.danielolvera.weatherappcompose.home.viewmodel.ViewModelFactory
import com.danielolvera.weatherappcompose.home.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherViewModel by viewModels()

    @Inject lateinit var fusedLocation: FusedLocationProviderClient
    @Inject lateinit var sharedPreference: SharedPreferencesManager

    private var isWeatherReceived = false

    companion object {
        const val TAG = "MainActivity"
    }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Weather App") })
                }
            ) { _ ->
                WeatherAppComposeTheme {
                    WeatherScreen()
                }
            }
        }

        sharedPreference.getLastCity()?.let { lastCity ->
            weatherViewModel.handleIntent(WeatherIntent.FetchWeatherData(lastCity))
        }
    }

    fun requestUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocation.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    if (!isWeatherReceived) {
                        val latitude = it.latitude
                        val longitude = it.longitude

                        weatherViewModel.handleIntent(WeatherIntent.FetchWeatherByLocation(latitude, longitude))
                        isWeatherReceived = true
                    }
                } ?: run {
                    Log.e(TAG, "Location is null, fetching last searched city.")


                    fallbackToLastSearchedCity()
                }
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to get location: ${e.message}")
                fallbackToLastSearchedCity()
            }
        } else {
            Log.e(TAG, "Location permissions are not granted")
            fallbackToLastSearchedCity()
        }
    }

    private fun fallbackToLastSearchedCity() {
        val lastCity = SharedPreferencesManager(this).getLastCity()
        if (lastCity != null) {
            weatherViewModel.handleIntent(WeatherIntent.FetchWeatherData(lastCity))
        } else {
            Log.e(TAG, "There is no last searched city available.")
        }
    }
}