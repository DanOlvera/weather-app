package com.danielolvera.weatherappcompose.home.view.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

object ConverterUtils {
    fun convertKtoF(temp: Double): String {
        return String.format(Locale.US, format = "%.1f", ((temp - 273.15) * 1.8 + 32))
    }

    fun timestampToHumanReadable(timestamp: Long?): String {
        val instant = timestamp?.let { Instant.ofEpochSecond(it) }

        val formatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
            .withZone(ZoneId.systemDefault())

        // Format the instant into a human-readable string
        return formatter.format(instant)
    }

}