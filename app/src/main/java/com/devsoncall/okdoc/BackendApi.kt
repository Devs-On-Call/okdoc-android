package com.devsoncall.okdoc

import retrofit2.Call;
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BackendApi {
    @FormUrlEncoded
    @POST("/api/tokens")
    fun createToken(@Field("amka") amka: String): Call<Token>

}