package com.devsoncall.okdoc.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient() {
    private val baseUrl = "https://okdoc-backend.herokuapp.com"
    private var mInstance: RetrofitClient? = null
    private var retrofit: Retrofit? = null

    init {
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Synchronized
    fun getInstance(): RetrofitClient? {
        if (mInstance == null) {
            mInstance = RetrofitClient()
        }
        return mInstance
    }

    fun getApi(): Api? {
        return retrofit?.create(Api::class.java)
    }
}