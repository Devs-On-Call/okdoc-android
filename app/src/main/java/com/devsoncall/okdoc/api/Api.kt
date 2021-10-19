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

    @GET("/api/patients/{patientId}/appointments")
    fun getPatientAppointments(
        @Path("patientId") patientId: String,
        @Header("Authorization") token: String
    ): Call<DataListResponse<PatientAppointment>>

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

    @GET("/api/professions")
    fun getProfessions(
        @Query("professionId") professionId: String,
        @Header("Authorization") token: String
    ): Call<DataListResponse<Profession>>

    @GET("/api/hospitals")
    fun getHospitals(
        @Query("professionId") professionId: String,
        @Header("Authorization") token: String
    ): Call<DataListResponse<Hospital>>

    @GET("/api/doctors")
    fun getDoctors(
        @Query("professionId") professionId: String,
        @Query("hospitalId") hospitalId: String,
        @Header("Authorization") token: String
    ): Call<DataListResponse<Doctor>>

    @GET("/api/appointments")
    fun getDoctorAppointments(
        @Query("doctorId") doctorId: String,
        @Header("Authorization") token: String
    ): Call<DataListResponse<DoctorAppointment>>

//    @GET("/api/appointments")
//    fun getHours(
//        @Query("doctorId") doctorId: String,
//        @Header("Authorization") token: String
//    ): Call<DataListResponse<BookedHours>>


}