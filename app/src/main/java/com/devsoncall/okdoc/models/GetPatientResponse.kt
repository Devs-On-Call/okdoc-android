package com.devsoncall.okdoc.models

data class GetPatientResponse(
    val success: Boolean,
    val message: String,
    val data: Patient
)