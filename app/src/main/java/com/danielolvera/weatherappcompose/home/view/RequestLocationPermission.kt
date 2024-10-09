package com.danielolvera.weatherappcompose.home.view

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(onPermissionGranted: () -> Unit) {
    val finePermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    val coarsePermissionState = rememberPermissionState(Manifest.permission.ACCESS_COARSE_LOCATION)

    // Check if both permissions are granted
    if (finePermissionState.status.isGranted && coarsePermissionState.status.isGranted) {
        onPermissionGranted()
    } else {
        LaunchedEffect(finePermissionState.status, coarsePermissionState.status) {
            if (!finePermissionState.status.isGranted) {
                finePermissionState.launchPermissionRequest()
            }
            if (!coarsePermissionState.status.isGranted) {
                coarsePermissionState.launchPermissionRequest()
            }
        }

        // To inform the user if they refused permissions
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Location permissions are required to fetch weather data by your current location.")
        }
    }
}

