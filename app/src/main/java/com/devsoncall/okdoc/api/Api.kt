package com.devsoncall.okdoc.api

import com.devsoncall.okdoc.models.BasicResponse
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import com.devsoncall.okdoc.models.Prescription
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

    @GET("/api/patients/{patientId}/prescriptions")
    fun getPrescriptions(
        @Path("patientId") patientId: String,
        @Header("Authorization") token: String
    ): Call<DataListResponse<Prescription>>
}