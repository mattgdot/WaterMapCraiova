package com.app.water4craiova.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.app.water4craiova.R
import com.app.water4craiova.utils.Constants.HOME_ROUTE
import com.app.water4craiova.utils.Constants.SETTINGS_ROUTE

sealed class NavItem(
    val name: Int, val route:String, val icon: ImageVector, val iconFilled: ImageVector
) {
    data object HomeScreen : NavItem(
        R.string.home,
        HOME_ROUTE,
        Icons.Outlined.LocationOn,
        Icons.Filled.LocationOn
    )

    data object SettingsScreen : NavItem(
        R.string.settings,
        SETTINGS_ROUTE,
        Icons.Outlined.Settings,
        Icons.Filled.Settings
    )
}