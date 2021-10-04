package com.devsoncall.okdoc.api

import com.devsoncall.okdoc.models.BasicResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import retrofit2.Call;
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("/api/tokens")
    fun createToken(
        @Field("amka") amka: String
    ): Call<BasicResponse>

    @GET("/api/patients/{patientId}")
    fun getPatient(
        @Path("patientId") patientId: String,
        @Header("Authorization") token: String
    ): Call<GetPatientResponse>
}