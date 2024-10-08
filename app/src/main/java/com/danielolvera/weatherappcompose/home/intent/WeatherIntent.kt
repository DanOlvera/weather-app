package com.danielolvera.weatherappcompose.home.intent

sealed class WeatherIntent {
    data class FetchWeatherData(val city: String) : WeatherIntent()
    data object FetchWeatherByLocation : WeatherIntent()
}