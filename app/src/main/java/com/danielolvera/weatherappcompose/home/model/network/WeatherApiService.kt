package com.danielolvera.weatherappcompose.home.model.network

import com.example.example.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String
    ): Response<WeatherResponse>
}