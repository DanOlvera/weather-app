package com.danielolvera.weatherappcompose.home.di.modules

import com.danielolvera.weatherappcompose.BuildConfig
import com.danielolvera.weatherappcompose.home.di.utils.ApiKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @ApiKey
    fun provideApiKey(): String = BuildConfig.API_KEY

}