package com.app.water4craiova.model.service

import com.app.water4craiova.utils.ThemeMode

interface StorageService {
    fun setAppTheme(appTheme: ThemeMode)

    fun getAppTheme(): ThemeMode

    fun setSelectedTab(selectedTab: Int)

    fun getSelectedTab(): Int

    fun setMapType(mapType: Int)

    fun getMapType(): Int
}