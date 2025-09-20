package com.example.ccred_3.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.tasks.await
import java.util.Locale
import android.location.Location


data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val address: String?
)

class LocationManager(private val context: Context) {
    private val fusedLocationClient: FusedLocationProviderClient = 
        LocationServices.getFusedLocationProviderClient(context)
    private val geocoder = Geocoder(context, Locale.getDefault())

    suspend fun getCurrentLocation(): LocationData? {
        return try {
            // Check if location permission is granted
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return null
            }

            // Get current location
            val location = fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                CancellationTokenSource().token
            ).await()

            if (location != null) {
                val address = try {
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    addresses?.firstOrNull()?.getAddressLine(0)
                } catch (e: Exception) {
                    null
                }

                LocationData(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    address = address
                )
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
}
