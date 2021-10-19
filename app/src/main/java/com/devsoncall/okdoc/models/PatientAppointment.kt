package com.devsoncall.okdoc.models

data class PatientAppointment(
    val _id: String,
    val date: String,
    val reason: String,
    val doctor: Doctor,
    val hospital: Hospital
)
