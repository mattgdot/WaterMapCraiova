package com.app.water4craiova.model.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun listenToLocation(): Flow<Location?>

    fun hasAccessCoarseLocationPermission():Boolean

    fun hasAccessFineLocationPermission():Boolean

    suspend fun getCurrentLocation(): Location?
}