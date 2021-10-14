package com.devsoncall.okdoc.models

data class Patient(
    val name: String,
    val lastName: String,
    val amka: String,
    val bloodType: String,
    val familyDoctor: Doctor
)