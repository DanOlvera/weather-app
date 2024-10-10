package com.danielolvera.weatherappcompose.home.model.repo

import com.danielolvera.weatherappcompose.home.model.data.WeatherResponse
import com.danielolvera.weatherappcompose.home.model.network.WeatherApiService
import com.danielolvera.weatherappcompose.home.model.state.WeatherState
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.amshove.kluent.shouldBe
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldBeInstanceOf
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`


class WeatherRepositoryTest {

    private lateinit var weatherApiService: WeatherApiService
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setUp() {
        weatherApiService = mock(WeatherApiService::class.java)
        weatherRepository = WeatherRepository(weatherApiService)
    }

    @Test
    fun testGetWeatherByLocation_success() = runTest {
        // Mock
        val weatherResponseMock = WeatherResponse()
            `when`(weatherApiService.getWeatherByLocation(44.34, 10.99, "api_key"))
                .thenReturn(weatherResponseMock)

        // Collect
        val result = weatherRepository.getWeatherByLocation(44.34, 10.99, "api_key").toList()

        // Assert
        result[0].shouldBeInstanceOf<WeatherState.Loading>()

        // Assert
        result[1].shouldBeInstanceOf<WeatherState.Success>()
        (result[1] as WeatherState.Success).weatherResponse shouldBe weatherResponseMock

    }

    @Test
    fun testGetWeatherByLocation_failure() = runTest {

        // Mock
        `when`(weatherApiService.getWeatherByLocation(44.34, 10.99, "api_key"))
            .thenThrow(RuntimeException("API Error"))

        // Collect
        val result = weatherRepository.getWeatherByLocation(44.34, 10.99, "api_key").toList()

        // Assert
        result[0] shouldBe WeatherState.Loading

        // Assert
        result[1].shouldBeInstanceOf<WeatherState.Error>()
        val errorState = result[1] as WeatherState.Error

        errorState.errorMessage shouldBe "API Error"
    }

    @Test
    fun testGetWeatherByCity_success() = runTest {
        // Arrange
        val mockWeatherResponse = WeatherResponse() // A mock response object
        `when`(weatherApiService.getWeatherByCity("New York", "api_key"))
            .thenReturn(mockWeatherResponse)

        // Act
        val result = weatherRepository.getWeather("New York", "api_key").toList()

        // Assert
        result[0].shouldBeInstanceOf<WeatherState.Loading>()
        result[1].shouldBeInstanceOf<WeatherState.Success>()

        // Assert
        val successState = result[1] as WeatherState.Success
        successState.weatherResponse shouldBeEqualTo mockWeatherResponse
    }

    @Test
    fun testGetWeatherByCity_failure() = runTest {
        // Arrange
        `when`(weatherApiService.getWeatherByCity("InvalidCity", "api_key"))
            .thenThrow(RuntimeException("City not found"))

        // Act
        val result = weatherRepository.getWeather("InvalidCity", "api_key").toList()

        // Assert: Verify the emitted states
        result[0].shouldBeInstanceOf<WeatherState.Loading>()
        result[1].shouldBeInstanceOf<WeatherState.Error>()

        // Assert
        val errorState = result[1] as WeatherState.Error
        errorState.errorMessage shouldBeEqualTo "City not found"
    }


}
