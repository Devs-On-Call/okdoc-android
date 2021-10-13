package com.devsoncall.okdoc.models

data class Diagnosis(
    val _id: String,
    val date: String,
    val diagnosis: String,
    val doctor: Doctor
)