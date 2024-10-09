package com.danielolvera.weatherappcompose.home.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.danielolvera.weatherappcompose.R
import com.danielolvera.weatherappcompose.home.model.state.WeatherState
import com.danielolvera.weatherappcompose.home.model.utils.WeatherApi
import com.danielolvera.weatherappcompose.home.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onFetchWeather: (String) -> Unit
) {

    val weatherState by viewModel.weatherState.observeAsState()
    var city by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 48.dp, 16.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SearchBar(
            hint = stringResource(R.string.search),
            cornerShape = RoundedCornerShape(20.dp),
            onTextChange = { searchCity -> city = searchCity},
            onSearchClicked = {
                if (city.isNotEmpty()) {
                    onFetchWeather(city)
                } else {
                    Log.d("Dang", "City name is empty! ")
                }
            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        when(weatherState) {
            is WeatherState.Loading -> {
                CircularProgressIndicator()
            }
            is WeatherState.Success -> {
                val weather = (weatherState as WeatherState.Success).weatherResponse

                //First we render the image
                val iconUrl = "http://openweathermap.org/img/w/${weather.weather.firstOrNull()?.icon}.png"
                Log.d("Dang", "WeatherScreen: $iconUrl")

                Image(
                    painter = rememberAsyncImagePainter(iconUrl),
                    contentDescription = null,
                    modifier = Modifier.size(64.dp)
                )


                //Then we display the text info
                Text(text = "City: ${weather.name}")
                Text(text = "Temperature: ${weather.main?.temp}")
                Text(text = "Description: ${weather.weather.firstOrNull()?.description}")

            }
            is WeatherState.Error -> {
                Text(text = "Error: ${(weatherState as WeatherState.Error).errorMessage}")
            }
            else -> {
                Text(text = "Error: How it landed here? $weatherState")
            }
        }
    }

}