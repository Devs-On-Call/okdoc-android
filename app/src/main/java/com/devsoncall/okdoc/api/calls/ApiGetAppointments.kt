package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.Appointment
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetAppointments {

    private var mListener: DataInterface? = null

    fun getAppointments(authToken: String = "", patientId: String = "") {
        val call: Call<DataListResponse<Appointment>>? =
            RetrofitClient().getInstance()?.getApi()?.getAppointments(patientId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<Appointment>> {
            override fun onResponse(
                call: Call<DataListResponse<Appointment>>,
                response: Response<DataListResponse<Appointment>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<Appointment>>, t: Throwable) {
                println(t.cause)
                mListener?.failureData(t)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getAppointmentsResponse: Response<DataListResponse<Appointment>>)
        fun failureData(t: Throwable)
    }

}