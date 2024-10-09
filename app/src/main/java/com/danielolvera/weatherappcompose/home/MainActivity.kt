package com.danielolvera.weatherappcompose.home

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val repository = WeatherRepository(ApiServiceClient.apiService)
        val apiKey = BuildConfig.API_KEY

        val factory = ViewModelFactory { WeatherViewModel(repository, apiKey) }

        weatherViewModel = ViewModelProvider(this, factory)[WeatherViewModel::class.java]

        setContent {

            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Weather App") })
                }
            ) { _ ->
                WeatherAppComposeTheme {
                    WeatherScreen(viewModel = weatherViewModel)
                }
            }
        }
    }
}