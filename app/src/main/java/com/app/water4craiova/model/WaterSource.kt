package com.app.water4craiova.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.DocumentId
import com.google.maps.android.clustering.ClusterItem

data class WaterSource(
    @DocumentId
    val id: String="",
    val latitude: Double=0.0,
    val longitude: Double=0.0,
    val name: String="",
    val subtitle: String="",
    val type: Int=0,
    val geohash: String="",
    val approved: Boolean=true,
    val addedBy: String="",
    val images: List<String> = emptyList(),
)