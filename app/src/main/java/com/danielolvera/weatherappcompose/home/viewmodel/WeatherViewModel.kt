package com.danielolvera.weatherappcompose.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielolvera.weatherappcompose.home.model.repo.WeatherRepository
import com.danielolvera.weatherappcompose.home.model.state.WeatherState
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRepository: WeatherRepository): ViewModel() {

    private val _weatherState = MutableLiveData<WeatherState>()
    val weatherState: LiveData<WeatherState> = _weatherState

    fun fetchWeatherByCity(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                weatherRepository.getWeather(city, apiKey)
                    .collect { state -> _weatherState.value = state}
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Failed to fetch weather with the provided city")
            }
        }
    }

    fun fetchWeatherByLocation(latitud: Double, longitud: Double, apiKey: String) {
        viewModelScope.launch {
            try {
                _weatherState.value = WeatherState.Loading
                weatherRepository.getWeatherByLocation(latitud, longitud, apiKey)
                    .collect {state -> _weatherState.value = state}
            } catch (e: Exception) {
                _weatherState.value = WeatherState.Error("Failed to fetch weather with the provided location")
            }
        }
    }
}