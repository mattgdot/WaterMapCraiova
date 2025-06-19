package com.app.water4craiova.utils

import android.text.TextUtils
import android.util.Patterns
import java.util.Locale

object Format {
    private fun truncateToFour(number: Double): String {
        return String.format(Locale.ENGLISH, "%.4f", number)
    }

    fun getUriFromCoordinates(latitude: Double, longitude: Double): String {
        return "geo:${truncateToFour(latitude)},${truncateToFour(longitude)}?q=${
            truncateToFour(
                latitude
            )
        },${truncateToFour(longitude)}"
    }

    fun isValidEmail(target: CharSequence?): Boolean {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

}