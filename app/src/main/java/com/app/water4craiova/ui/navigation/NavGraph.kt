package com.app.water4craiova.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.app.water4craiova.WaterMapAppState
import com.app.water4craiova.ui.screens.home.HomeScreen
import com.app.water4craiova.ui.screens.login.LoginScreen
import com.app.water4craiova.ui.screens.report.ReportForm
import com.app.water4craiova.ui.screens.reset.ResetScreen
import com.app.water4craiova.ui.screens.settings.SettingsScreen
import com.app.water4craiova.ui.screens.signup.SignUpScreen
import com.app.water4craiova.ui.screens.webview.WebViewScreen
import com.app.water4craiova.utils.Constants.HOME_ROUTE
import com.app.water4craiova.utils.Constants.LOGIN_ROUTE
import com.app.water4craiova.utils.Constants.REPORT_ROUTE
import com.app.water4craiova.utils.Constants.RESET_ROUTE
import com.app.water4craiova.utils.Constants.SIGN_UP_ROUTE
import com.app.water4craiova.utils.Constants.WEBVIEW_ROUTE

@Composable
fun NavGraph(appState: WaterMapAppState) {
    NavHost(navController = appState.navController, startDestination = HOME_ROUTE) {

        composable(NavItem.HomeScreen.route) {
            HomeScreen(appTheme = appState.appTheme.value, openScreen = { route ->
                appState.navigate(route)
            })
        }

        composable(NavItem.SettingsScreen.route) {
            SettingsScreen(
                openScreen = { route -> appState.navigate(route) },
                appTheme = appState.appTheme.value,
                onThemeChange = {
                    appState.setAppTheme(it)
                }
            )
        }

        composable(REPORT_ROUTE) {
            ReportForm(
                goBack = {
                    appState.popUp()
                },
                openScreen = {
                    appState.navigateAndPopUp(it, REPORT_ROUTE)
                },
                appTheme = appState.appTheme.value
            )
        }

        composable(WEBVIEW_ROUTE) {
            val url = it.arguments?.getString("url") ?: ""
            WebViewScreen(
                url = url
            )
        }

        composable(SIGN_UP_ROUTE) {
            SignUpScreen(
                onFinish = {
                    appState.popUp()
                },
                openScreen = {
                    appState.navigateAndPopUp(it, SIGN_UP_ROUTE)
                }
            )
        }

        composable(LOGIN_ROUTE) {
            LoginScreen(
                onFinish = {
                    appState.popUp()
                },
                openScreen = { route,popup ->
                    if(popup) appState.navigateAndPopUp(route, LOGIN_ROUTE)
                    else appState.navigate(route)
                }
            )
        }

        composable(RESET_ROUTE){
            ResetScreen(onFinish = { appState.popUp()})
        }
    }
}