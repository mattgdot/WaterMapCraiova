package com.app.water4craiova.ui.screens.report

import android.app.Application
import android.content.Context
import android.location.Location
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.water4craiova.R
import com.app.water4craiova.model.WaterSource
import com.app.water4craiova.model.service.AccountService
import com.app.water4craiova.model.service.DatabaseService
import com.app.water4craiova.model.service.LocationService
import com.app.water4craiova.retrofit.GoogleFormApi
import com.app.water4craiova.retrofit.GoogleFormClient
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val locationService: LocationService,
    private val accountService: AccountService,
    private val databaseService: DatabaseService,
    private val application: Application
): ViewModel() {
    var location by mutableStateOf<Location?>(null)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            location = locationService.getCurrentLocation()
        }
    }

    var address by mutableStateOf(LatLng(0.0, 0.0))
    var details by mutableStateOf("")
    var name by mutableStateOf("")
    var nameError by mutableStateOf("")
    var photoUri by mutableStateOf<Uri?>(null)

    var sent by mutableStateOf(false)
    var loading by mutableStateOf(false)

    var user = accountService.currentUser

    fun updateAddress(latLng: LatLng) {
        address = latLng
    }

    fun updateName(newValue: String) {
        name = newValue
        if(name.isNotBlank()){
            nameError = ""
        }
    }

    fun updateDetails(newValue: String) {
        details = newValue
    }

    fun updatePhotoUri(newValue: Uri?) {
        photoUri = newValue
    }

    val api = GoogleFormClient.getInstance().create(GoogleFormApi::class.java)

    fun sendResponse() {

        if(name.isBlank()){
            nameError = application.getString(R.string.sourceNameMissing)
            return
        } else {
            loading = true

            viewModelScope.launch(Dispatchers.IO) {
                try{
                    val call = api.sendAddFormData(name, "${address.latitude}, ${address.longitude}", details)
                    viewModelScope.launch(Dispatchers.IO) {
                        call.enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                            }
                        })
                    }
                } catch (e: Exception){

                }

                val hash = GeoFireUtils.getGeoHashForLocation(GeoLocation(address.latitude, address.longitude))
                val uri = photoUri?.let { databaseService.uploadImage(it) }?:""

                val source = WaterSource(
                    latitude = address.latitude,
                    longitude = address.longitude,
                    name = name,
                    subtitle = details,
                    geohash = hash,
                    approved = false,
                    addedBy = accountService.currentUserId,
                    images = listOf(uri)
                )

                databaseService.addSource(source){
                    if (it != null) {
                        Toast.makeText(application, application.getString(R.string.error), Toast.LENGTH_SHORT).show()
                    } else {
                        sent = true
                    }
                    loading = false
                }

            }
        }

    }
}