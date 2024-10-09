package com.danielolvera.weatherappcompose.home.model.repo

import com.danielolvera.weatherappcompose.home.model.network.WeatherApiService
import com.danielolvera.weatherappcompose.home.model.state.WeatherState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class WeatherRepository (private val weatherApiService: WeatherApiService) {

    fun getWeather(city: String, apiKey: String): Flow<WeatherState> = flow {
        emit(WeatherState.Loading)
        try {
            val response = weatherApiService.getWeatherByCity(city, apiKey)
            emit(WeatherState.Success(response))
        } catch (e: Exception) {
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