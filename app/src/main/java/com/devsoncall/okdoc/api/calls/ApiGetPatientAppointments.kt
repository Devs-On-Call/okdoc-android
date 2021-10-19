package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.PatientAppointment
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetPatientAppointments {

    private var mListener: DataInterface? = null

    fun getAppointments(authToken: String = "", patientId: String = "") {
        val call: Call<DataListResponse<PatientAppointment>>? =
            RetrofitClient().getInstance()?.getApi()?.getPatientAppointments(patientId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<PatientAppointment>> {
            override fun onResponse(
                call: Call<DataListResponse<PatientAppointment>>,
                response: Response<DataListResponse<PatientAppointment>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<PatientAppointment>>, t: Throwable) {
                println(t.cause)
                mListener?.failureData(t)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getAppointmentsResponse: Response<DataListResponse<PatientAppointment>>)
        fun failureData(t: Throwable)
    }

}