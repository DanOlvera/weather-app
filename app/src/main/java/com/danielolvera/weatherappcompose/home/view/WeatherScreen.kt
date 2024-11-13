package com.danielolvera.weatherappcompose.home.view

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.danielolvera.weatherappcompose.R
import com.danielolvera.weatherappcompose.home.MainActivity
import com.danielolvera.weatherappcompose.home.intent.WeatherIntent
import com.danielolvera.weatherappcompose.home.model.state.WeatherState
import com.danielolvera.weatherappcompose.home.view.utils.ConverterUtils
import com.danielolvera.weatherappcompose.home.view.utils.InfoGridItem
import com.danielolvera.weatherappcompose.home.view.utils.SharedPreferencesManager
import com.danielolvera.weatherappcompose.home.viewmodel.WeatherViewModel

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel = hiltViewModel(),
    context: Context = LocalContext.current
) {

    val weatherState by viewModel.weatherState.observeAsState()
    var city by remember { mutableStateOf("") }

    // Fetch weather by location if permission is granted
    RequestLocationPermission(onPermissionGranted = {
        // Call method to request user's current location
        (context as MainActivity).requestUserLocation()
    })

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

                // Top info that includes, the icon, city and temperature
                CityTemperature(iconUrl, weather)

                // This section includes the rest of the info

                Text(text = "${weather.weather.firstOrNull()?.description?.uppercase()}",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    style = TextStyle(
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center)

                val weatherItems = listOf(
                    InfoGridItem("Feels like",
                        "${weather.main?.feelsLike?.let { ConverterUtils.convertKtoF(it)}}°"
                    ),
                    InfoGridItem("Wind", "${weather.wind?.speed.toString()} mph"),
                    InfoGridItem("Humidity", "${weather.main?.humidity} %"),
                    InfoGridItem("Pressure", "${weather.main?.pressure} inHg"),
                    InfoGridItem("Sunrise",
                        ConverterUtils.timestampToHumanReadable(weather.sys?.sunrise?.toLong())),
                    InfoGridItem("Sunset",
                        ConverterUtils.timestampToHumanReadable(weather.sys?.sunset?.toLong()))

                )

                // We show a grid with weather details. Each cell will have a different piece of weather info.
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(weatherItems) { item ->
                        GridItemView(item = item)
                    }
                }

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

@Composable
fun GridItemView(item: InfoGridItem) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = item.title, style = MaterialTheme.typography.titleSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = item.value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}