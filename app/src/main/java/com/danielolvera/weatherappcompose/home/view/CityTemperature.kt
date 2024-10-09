package com.danielolvera.weatherappcompose.home.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.rememberAsyncImagePainter
import coil3.request.ImageRequest
import com.danielolvera.weatherappcompose.home.model.data.WeatherResponse
import com.danielolvera.weatherappcompose.home.view.utils.ConverterUtils

@Composable
internal fun CityTemperature(
    iconUrl: String?,
    weather: WeatherResponse
) {
    Row {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
        }

        Column(
            verticalArrangement = Arrangement.Center
        ) {
            //Then we display the text info
            Text(
                text = "${weather.name}",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 32.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
            Text(
                text = "${weather.main?.temp?.let { ConverterUtils.convertKtoF(it) }}°",
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                style = TextStyle(
                    fontSize = 72.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}