package com.app.water4craiova

import android.annotation.SuppressLint
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.app.water4craiova.ui.navigation.BottomNavigationBar
import com.app.water4craiova.ui.navigation.NavGraph
import com.app.water4craiova.ui.theme.Water4CraiovaTheme
import com.app.water4craiova.utils.Constants.LOGIN_ROUTE
import com.app.water4craiova.utils.Constants.REPORT_ROUTE
import com.app.water4craiova.utils.Constants.RESET_ROUTE
import com.app.water4craiova.utils.Constants.SIGN_UP_ROUTE
import com.app.water4craiova.utils.Constants.WEBVIEW_ROUTE
import com.app.water4craiova.utils.ThemeMode

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

val LocalSnackbarHostState = compositionLocalOf<SnackbarHostState>{
    error("No Snackbar Host State")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp() {
    val appState = rememberAppState()

    Water4CraiovaTheme(
        darkTheme = when (appState.appTheme.value) {
            ThemeMode.AUTO -> isSystemInDarkTheme()
            ThemeMode.DARK -> true
            ThemeMode.LIGHT -> false
        }
    ) {
        val navBackStackEntry by appState.navController.currentBackStackEntryAsState()
        var showBottomBar by rememberSaveable { mutableStateOf(true) }

        showBottomBar = when (navBackStackEntry?.destination?.route) {
            REPORT_ROUTE -> false
            WEBVIEW_ROUTE -> false
            SIGN_UP_ROUTE -> false
            LOGIN_ROUTE -> false
            RESET_ROUTE -> false
            else -> true
        }

        CompositionLocalProvider(value = LocalSnackbarHostState provides appState.snackbarHostState) {
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = appState.snackbarHostState)
                },
                bottomBar = {
                    if (showBottomBar) BottomNavigationBar(navController = appState.navController)
                },
                topBar = {
                    if(!showBottomBar) TopAppBar(title = {
                    }, navigationIcon = {
                        IconButton(onClick = {
                            appState.popUp()
                        }){
                            Icon(Icons.Outlined.Close,null)
                        }
                    })
                }
            ) {
                Box(
                    modifier = if(!showBottomBar) Modifier.padding(it) else Modifier.padding(bottom=it.calculateBottomPadding())
                ) {
                    NavGraph(appState)
                }
            }
        }
    }
}

@Composable
fun rememberAppState(
    viewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    appTheme: MutableState<ThemeMode> = remember {
        mutableStateOf(viewModel.getTheme())
    },
) = remember(navController, snackbarHostState, appTheme) {
    WaterMapAppState(navController, snackbarHostState, viewModel.storageService, appTheme)
}