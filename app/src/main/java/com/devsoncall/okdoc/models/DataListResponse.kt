package com.devsoncall.okdoc.models

class DataListResponse<T: Any> {
    val success: Boolean = false
    val message: String = ""
    val data: List<T>? = null
}
