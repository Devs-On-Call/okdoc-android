package com.devsoncall.okdoc.models

data class Appointment(
    val _id: String,
    val date: String,
    val reason: String,
    val doctor: Doctor,
    val hospital: Hospital
)
