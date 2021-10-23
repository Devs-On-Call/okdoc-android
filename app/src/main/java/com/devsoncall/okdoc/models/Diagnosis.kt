package com.devsoncall.okdoc.models

data class Diagnosis(
    val _id: String,
    val date: String,
    val name: String,
    val doctor: Doctor,
    val prescription: Prescription,
    val details: String
)