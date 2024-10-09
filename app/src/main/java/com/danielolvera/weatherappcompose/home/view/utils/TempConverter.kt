package com.danielolvera.weatherappcompose.home.view.utils

import java.util.Locale

object TempConverter {
    fun convertKtoF(temp: Double): String {
        return String.format(Locale.US, format = "%.1f", ((temp - 273.15) * 1.8 + 32))
    }

}