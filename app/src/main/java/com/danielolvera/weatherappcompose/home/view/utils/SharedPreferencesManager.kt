package com.danielolvera.weatherappcompose.home.view.utils

import android.content.Context

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("weather_prefs", Context.MODE_PRIVATE)

    fun saveLastCity(city: String) {
        sharedPreferences.edit().putString("last_city", city).apply()
    }

    fun getLastCity(): String? {
        return sharedPreferences.getString("last_city", null)
    }
}
