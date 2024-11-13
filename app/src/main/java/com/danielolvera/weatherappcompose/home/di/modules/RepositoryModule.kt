package com.danielolvera.weatherappcompose.home.di.modules

import com.danielolvera.weatherappcompose.home.model.network.WeatherApiService
import com.danielolvera.weatherappcompose.home.model.repo.WeatherRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providesWeatherRepository(
        weatherApiService: WeatherApiService
    ): WeatherRepository {
        return WeatherRepository(weatherApiService)
    }
}