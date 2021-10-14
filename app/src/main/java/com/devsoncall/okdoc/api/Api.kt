package com.devsoncall.okdoc.api

import com.devsoncall.okdoc.models.*
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

    @GET("/api/patients/{patientId}/diagnoses")
    fun getDiagnoses(
        @Path("patientId") patientId: String,
        @Header("Authorization") token: String
    ): Call<DataListResponse<Diagnosis>>

    @GET("/api/hospitals")
    fun getHospitals(
        @Query("professionId") professionId: String,
        @Header("Authorization") token: String
    ): Call<DataListResponse<Hospital>>

}