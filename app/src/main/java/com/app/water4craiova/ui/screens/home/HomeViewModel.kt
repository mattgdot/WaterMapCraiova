package com.app.water4craiova.ui.screens.home

import android.location.Location
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.water4craiova.model.WaterSource
import com.app.water4craiova.model.service.LocationService
import com.app.water4craiova.model.service.DatabaseService
import com.app.water4craiova.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.lastOrNull
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val databaseService: DatabaseService,
    private val locationService: LocationService,
    private val storageService: StorageService
) : ViewModel() {

    var sources by mutableStateOf<List<WaterSource>>(listOf())
    var location by mutableStateOf<Location?>(null)

    var defaultTab  = 1

    fun loadSources(location: Location) {
        viewModelScope.launch {
            sources = databaseService.getSources(
                location.latitude,
                location.longitude
            ).toList()
        }
    }

    fun setIsLocationEnabled(bool: Boolean) {
        if (bool && location == null) {
            viewModelScope.launch(Dispatchers.IO) {
                locationService.listenToLocation().collect{
                    location = it
                    it.let { l->
                        if (l != null) {
                            loadSources(l)
                        }
                    }
                }
            }
        }
    }

    fun setMapType(mapType: Int) {
        storageService.setMapType(mapType)
    }

    fun getMapType(): Int {
        return storageService.getMapType()
    }

    fun setSelectedTab(tab: Int) {
        storageService.setSelectedTab(tab)
        defaultTab = tab
    }
}