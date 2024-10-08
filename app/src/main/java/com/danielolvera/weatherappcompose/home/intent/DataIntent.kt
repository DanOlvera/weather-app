package com.danielolvera.weatherappcompose.home.intent

sealed class DataIntent {
    data object FetchWeatherData : DataIntent()
}