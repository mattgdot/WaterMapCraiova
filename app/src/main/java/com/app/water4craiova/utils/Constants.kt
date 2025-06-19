package com.app.water4craiova.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds


object Constants {
    const val HOME_ROUTE = "home"
    const val SETTINGS_ROUTE = "settings"
    const val REPORT_ROUTE = "report"
    const val SIGN_UP_ROUTE = "singin"
    const val LOGIN_ROUTE = "login"
    const val RESET_ROUTE = "reset"
    const val WEBVIEW_ROUTE = "webview/{url}"
    const val DATA_COLLECTION = "resources"
    const val SUBMISSIONS_COLLECTION = "res2"
    const val APPROVED = "approved"
    const val LATITUDE = "latitude"
    const val LONGITUDE = "longitude"
    const val GEOHASH = "geohash"
    const val ADDED_BY = "addedBy"

    const val ABOUT_LINK = "https://watermapcraiova.github.io/despre"
    const val TERMS_LINK = "https://watermapcraiova.github.io/t&c"
    const val PRIVACY_LINK = "https://watermapcraiova.github.io/pp"
    const val INSTAGRAM_LINK = "https://instagram.com/watermap.craiova"
    const val EMAIL = "watermapcraiova@gmail.com"
    const val PLAYSTORE_LINK = "https://play.google.com/store/apps/details?id="

    const val DOCS_LINK = "https://docs.google.com/"
    const val ADD_FORM_PATH = "/forms/d/e/1FAIpQLSdfATwGGEtKdZEVSkUVeqpkQa3MnspP0j1A5XtFwvK75vkv_Q/formResponse"
    const val REPORT_FORM_PATH = "/forms/d/e/1FAIpQLScT0zOKmh8C4GkYXk94m4_OqK4YqcqdkdBwkVWgiJqE54UFbQ/viewform?usp=pp_url&entry.373717379={SRC_ID}"

    const val ADD_FORM_EMAIL = "entry.646177647"
    const val ADD_FORM_ADDRESS = "entry.1364866494"
    const val ADD_FORM_DETAILS = "entry.1149114592"
    const val REPORT_SOURCE_ID = "entry.373717379"

    const val ROMANIA_LAT = 45.943161
    const val ROMANIA_LNG = 24.96676

    const val ROMANIA_LAT_SW = 43.77336963838219
    const val ROMANIA_LNG_SW = 19.91657619862749
    const val ROMANIA_LAT_NE = 48.21253262602081
    const val ROMANIA_LNG_NE = 32.27160045849656

    val ROMANIA_LOCATION = LatLng(ROMANIA_LAT, ROMANIA_LNG)
    val ROMANIA_BOUNDS = LatLngBounds(
        LatLng(ROMANIA_LAT_SW, ROMANIA_LNG_SW), LatLng(ROMANIA_LAT_NE, ROMANIA_LNG_NE)
    )

    const val MIN_ZOOM = 9f
    const val INT_ZOOM = 14f
    const val MAX_ZOOM = 15f

    const val SHARED_PREF = "watermap"

    const val APP_THEME = "app_theme"
    const val TAB = "tab"
    const val MAP_TYPE = "map_type"
}