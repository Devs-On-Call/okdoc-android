package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.GetPatientResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetPatient {

    private var mListener: DataInterface? = null

    fun getPatient(authToken: String = "", patientId: String = "") {
        val call: Call<GetPatientResponse>? =
            RetrofitClient().getInstance()?.getApi()?.getPatient(patientId, authToken)

        call!!.enqueue(object : Callback<GetPatientResponse> {
            override fun onResponse(
                call: Call<GetPatientResponse>,
                response: Response<GetPatientResponse>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<GetPatientResponse>, t: Throwable) {
                println(t.cause)
                mListener?.failureData(t)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getPatientResponse: Response<GetPatientResponse>)
        fun failureData(t: Throwable)
    }

}