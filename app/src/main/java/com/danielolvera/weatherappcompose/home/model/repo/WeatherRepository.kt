package com.danielolvera.weatherappcompose.home.model.repo

import com.danielolvera.weatherappcompose.home.model.network.WeatherApiService
import com.danielolvera.weatherappcompose.home.model.state.WeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherApiService: WeatherApiService) {

    fun getWeather(city: String, apiKey: String): Flow<WeatherState> = flow {
        // We emit the loading state first to indicate data fetch is in progress.
        emit(WeatherState.Loading)
        // Try-catch block for safe API call, to make sure everything runs smoothly.
        try {
            val response = weatherApiService.getWeatherByCity(city, apiKey)
            emit(WeatherState.Success(response))
        } catch (e: Exception) {
            // Uh-oh, something broke. We emit the error state with a descriptive message.
            emit(WeatherState.Error(e.message ?: "Unknown error. How?"))
        }
    }.flowOn(Dispatchers.IO)

    fun getWeatherByLocation(latitude: Double, longitude: Double, apiKey: String): Flow<WeatherState> = flow {
        emit(WeatherState.Loading)
        try {
            val response = weatherApiService.getWeatherByLocation(latitude, longitude, apiKey)
            emit(WeatherState.Success(response))
        } catch (e: Exception) {
            emit(WeatherState.Error(e.message ?: "Unknown error. How?"))
        }
    }.flowOn(Dispatchers.IO)
}