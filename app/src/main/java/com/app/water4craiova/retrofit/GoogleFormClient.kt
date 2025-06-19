package com.app.water4craiova.retrofit

import com.app.water4craiova.utils.Constants.DOCS_LINK
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GoogleFormClient {
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(DOCS_LINK)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}