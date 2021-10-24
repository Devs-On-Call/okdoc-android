package com.devsoncall.okdoc.models

data class Diagnosis(
    val _id: String,
    val date: String,
    val name: String,
    val details: String,
    val doctor: Doctor,
    val prescription: String
)