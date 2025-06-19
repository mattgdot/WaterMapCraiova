package com.app.water4craiova

import androidx.lifecycle.ViewModel
import com.app.water4craiova.model.service.StorageService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val storageService: StorageService
): ViewModel() {
    fun getTheme() = storageService.getAppTheme()
}