package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiCreateAppointment {

    private var mListener: DataInterface? = null

    fun createAppointment(patient: String, reason: String, doctor: String, hospital: String, date: String, authToken: String = "") {
        val call: Call<BasicResponse>? =
            RetrofitClient().getInstance()?.getApi()?.createAppointment(patient, reason, doctor, hospital, date, authToken)

        call!!.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {
                println(t.cause)
                mListener?.failureData(t)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(createAppointmentResponse: Response<BasicResponse>)
        fun failureData(t: Throwable)
    }
}