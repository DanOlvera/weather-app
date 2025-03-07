package com.danielolvera.weatherappcompose.home.model.state

import com.danielolvera.weatherappcompose.home.model.data.WeatherResponse

sealed class WeatherState {
    data object Loading : WeatherState()
    data class Success(val weatherResponse: WeatherResponse) : WeatherState()
    data class Error(val errorMessage: String) : WeatherState()
}