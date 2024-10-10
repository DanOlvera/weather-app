package com.danielolvera.weatherappcompose.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielolvera.weatherappcompose.home.intent.WeatherIntent
import com.danielolvera.weatherappcompose.home.model.repo.WeatherRepository
import com.danielolvera.weatherappcompose.home.model.state.WeatherState
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val apiKey: String): ViewModel() {

    private val _weatherState = MutableLiveData<WeatherState>()
    val weatherState: LiveData<WeatherState> = _weatherState

    fun handleIntent(intent: WeatherIntent) {
        when(intent) {
            is WeatherIntent.FetchWeatherData -> {
                fetchWeatherByCity(intent.city, apiKey)
            }
            is WeatherIntent.FetchWeatherByLocation -> {
                fetchWeatherByLocation(intent.lat, intent.lon, apiKey)
            }
        }
    }

    // Trigger the weather fetch when the city is updated.
    private fun fetchWeatherByCity(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                weatherRepository.getWeather(city, apiKey)
                    .collect { state -> _weatherState.value = state}
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Failed to fetch weather with the provided city")
            }
        }
    }

    // Trigger the weather fetch when the app is launched.
    private fun fetchWeatherByLocation(latitude: Double, longitude: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                _weatherState.value = WeatherState.Loading
                weatherRepository.getWeatherByLocation(latitude, longitude, apiKey)
                    .collect {state -> _weatherState.value = state}
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Failed to fetch weather with the provided location")
            }
        }
    }
}