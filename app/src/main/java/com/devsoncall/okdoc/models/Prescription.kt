package com.devsoncall.okdoc.models

data class Prescription(
    val _id: String,
    val date: String,
//    val diagnosis: Diagnosis,
    val dosage: String,
    val drug: String,
    val duration: String,
    val doctor: Doctor
)