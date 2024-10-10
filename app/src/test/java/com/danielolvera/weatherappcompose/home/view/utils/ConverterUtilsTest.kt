package com.danielolvera.weatherappcompose.home.view.utils

import org.amshove.kluent.shouldBeEqualTo
import org.junit.Test


class ConverterUtilsTest {

    @Test
    fun timestampToHumanReadable_validTimeStamp() {
        val timestamp = 1726636384L
        val expected = "22:13:04 PM"

        val result = ConverterUtils.timestampToHumanReadable(timestamp)

        expected shouldBeEqualTo result
    }

    @Test
    fun timestampToHumanReadable_nullTimeStamp() {
        val timestamp = null
        val expected = "Invalid timestamp"

        val result = ConverterUtils.timestampToHumanReadable(timestamp)

        expected shouldBeEqualTo result
    }
}