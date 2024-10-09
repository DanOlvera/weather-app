package com.danielolvera.weatherappcompose.home.view

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.danielolvera.weatherappcompose.R
import com.danielolvera.weatherappcompose.home.intent.WeatherIntent
import com.danielolvera.weatherappcompose.home.model.state.WeatherState
import com.danielolvera.weatherappcompose.home.view.utils.SharedPreferencesManager
import com.danielolvera.weatherappcompose.home.view.utils.TempConverter
import com.danielolvera.weatherappcompose.home.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    context: Context
) {

    val weatherState by viewModel.weatherState.observeAsState()
    var city by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 115.dp, 16.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        SearchBar(
            hint = stringResource(R.string.search),
            cornerShape = RoundedCornerShape(20.dp),
            onTextChange = { searchCity -> city = searchCity},
            onSearchClicked = {
                if (city.isNotEmpty()) {
                    viewModel.handleIntent(WeatherIntent.FetchWeatherData(city))
                    SharedPreferencesManager(context = context).saveLastCity(city)
                } else {
                    Log.e("WeatherScreen", "City name is empty! ")
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
                val iconUrl =
                    weather.weather.firstOrNull()?.icon?.let {
                        stringResource(R.string.icon_url,
                            it
                        )
                    }

                // Use of Coil to load the image
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(iconUrl)
                            .build()
                    ),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )


                //Then we display the text info
                Text(text = "City: ${weather.name}")
                Text(text = "Temperature: ${weather.main?.temp?.let { TempConverter.convertKtoF(it) }}")
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