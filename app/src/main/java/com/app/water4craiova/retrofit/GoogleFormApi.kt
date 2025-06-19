package com.app.water4craiova.retrofit

import com.app.water4craiova.utils.Constants.ADD_FORM_ADDRESS
import com.app.water4craiova.utils.Constants.ADD_FORM_DETAILS
import com.app.water4craiova.utils.Constants.ADD_FORM_EMAIL
import com.app.water4craiova.utils.Constants.ADD_FORM_PATH
import com.app.water4craiova.utils.Constants.REPORT_FORM_PATH
import com.app.water4craiova.utils.Constants.REPORT_SOURCE_ID
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface GoogleFormApi {
    @FormUrlEncoded
    @POST(ADD_FORM_PATH)
    fun sendAddFormData(
        @Field(ADD_FORM_EMAIL) email: String,
        @Field(ADD_FORM_ADDRESS) address: String,
        @Field(ADD_FORM_DETAILS) details: String
    ): Call<Void>
}