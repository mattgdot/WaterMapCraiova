package com.app.water4craiova.ui.screens.settings

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.water4craiova.R
import com.app.water4craiova.model.User
import com.app.water4craiova.model.WaterSource
import com.app.water4craiova.model.service.AccountService
import com.app.water4craiova.model.service.DatabaseService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val accountService: AccountService,
    private val application: Application,
    private val databaseService: DatabaseService
) : ViewModel() {
    var user by mutableStateOf<User?>(null)
    var userSources by mutableStateOf(emptyList<WaterSource>())

    init {
        listenToUser()
    }

    private fun listenToUser() {
        viewModelScope.launch {
            accountService.currentUser.collect {
                user = it.also {
                    listenToSources(it.id)
                }
            }
        }
    }

    private fun listenToSources(userId: String) {
        viewModelScope.launch {
            databaseService.getMySources(userId).collect {
                userSources = it
            }
        }
    }

    fun signOut() {
        accountService.signOut()
        Toast.makeText(application, application.getString(R.string.loggedOut), Toast.LENGTH_SHORT)
            .show()
    }

}