package com.app.water4craiova.model.service.impl

import android.content.SharedPreferences
import com.app.water4craiova.model.service.StorageService
import com.app.water4craiova.utils.Constants.APP_THEME
import com.app.water4craiova.utils.Constants.MAP_TYPE
import com.app.water4craiova.utils.Constants.TAB
import com.app.water4craiova.utils.ThemeMode
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : StorageService {
    override fun setAppTheme(appTheme: ThemeMode) {
        sharedPreferences.edit().apply {
            putString(APP_THEME, appTheme.name)
            apply()
        }
    }

    override fun getAppTheme(): ThemeMode {
        sharedPreferences.getString(APP_THEME, ThemeMode.AUTO.name)?.let {
            return ThemeMode.valueOf(it)
        } ?: return ThemeMode.AUTO
    }

    override fun setSelectedTab(selectedTab: Int) {
        sharedPreferences.edit().apply(){
            putInt(TAB, selectedTab)
            apply()
        }
    }

    override fun getSelectedTab(): Int {
        return sharedPreferences.getInt(TAB, 1)
    }

    override fun setMapType(mapType: Int) {
        sharedPreferences.edit().apply {
            putInt(MAP_TYPE, mapType)
            apply()
        }
    }

    override fun getMapType(): Int {
        return sharedPreferences.getInt(MAP_TYPE, 0)
    }

}