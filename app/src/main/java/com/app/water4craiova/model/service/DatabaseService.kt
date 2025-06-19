package com.app.water4craiova.model.service

import android.net.Uri
import com.app.water4craiova.model.WaterSource
import kotlinx.coroutines.flow.Flow

interface DatabaseService {
    suspend fun getSources(
        latitude: Double,
        longitude: Double,
    ): List<WaterSource>

    suspend fun addSource(
        source: WaterSource,
        onResult: (Throwable?) -> Unit
    )

    fun getMySources(
        userId: String
    ): Flow<List<WaterSource>>

    suspend fun uploadImage(
        uri: Uri
    ): String

}