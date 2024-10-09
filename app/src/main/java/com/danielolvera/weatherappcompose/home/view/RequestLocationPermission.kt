package com.danielolvera.weatherappcompose.home.view

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    val finePermissionState = rememberPermissionState(android.Manifest.permission.ACCESS_FINE_LOCATION)

    if (finePermissionState.status.isGranted) {
        onPermissionGranted()
    } else {
        Column {
            Text("Location permission is required to get weather by location.")
            Button(onClick = { finePermissionState.launchPermissionRequest() }) {
                Text("Grant Permission")
            }
        }
    }
}
