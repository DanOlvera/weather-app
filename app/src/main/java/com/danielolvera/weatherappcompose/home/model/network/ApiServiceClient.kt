package com.danielolvera.weatherappcompose.home.model.network

import com.danielolvera.weatherappcompose.home.model.utils.WeatherApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiServiceClient {

    private val logging = HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    private val okHttpClient = OkHttpClient.Builder().apply {
        addInterceptor(logging)
    }

    val apiService: WeatherApiService by lazy {
        Retrofit.Builder()
            .baseUrl(WeatherApi.BASE_URL)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApiService::class.java)
    }
}