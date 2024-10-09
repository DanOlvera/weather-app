package com.danielolvera.weatherappcompose.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.danielolvera.weatherappcompose.BuildConfig
import com.danielolvera.weatherappcompose.home.model.network.ApiServiceClient
import com.danielolvera.weatherappcompose.home.model.repo.WeatherRepository
import com.danielolvera.weatherappcompose.home.theme.WeatherAppComposeTheme
import com.danielolvera.weatherappcompose.home.view.WeatherScreen
import com.danielolvera.weatherappcompose.home.viewmodel.ViewModelFactory
import com.danielolvera.weatherappcompose.home.viewmodel.WeatherViewModel

class MainActivity : ComponentActivity() {

    private lateinit var weatherViewModel: WeatherViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = WeatherRepository(ApiServiceClient.apiService)
        val factory = ViewModelFactory { WeatherViewModel(repository) }
        val apiKey = BuildConfig.API_KEY

        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        setContent {
            WeatherAppComposeTheme {
                WeatherScreen(viewModel = weatherViewModel) { city ->
                    weatherViewModel.fetchWeatherByCity(city, apiKey)
                }
            }
        }
    }
}