package com.app.water4craiova

import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.MutableState
import androidx.navigation.NavHostController
import com.app.water4craiova.model.service.StorageService
import com.app.water4craiova.utils.ThemeMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

class WaterMapAppState(
    var navController: NavHostController,
    var snackbarHostState: SnackbarHostState,
    val storageService: StorageService,
    var appTheme: MutableState<ThemeMode>
) {

    fun setAppTheme(themeMode: ThemeMode){
        appTheme.value = themeMode
        storageService.setAppTheme(themeMode)
    }
    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
}