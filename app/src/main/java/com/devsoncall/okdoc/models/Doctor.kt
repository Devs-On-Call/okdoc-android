package com.devsoncall.okdoc.models

data class Doctor(
    val _id: String,
    val profession: Profession,
    val name: String,
    val lastName: String,
    val hospital: Hospital
)