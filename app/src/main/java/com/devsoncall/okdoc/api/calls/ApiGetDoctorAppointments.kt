package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.PatientAppointment
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Doctor
import com.devsoncall.okdoc.models.DoctorAppointment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetDoctorAppointments {

    private var mListener: DataInterface? = null

    fun getAppointments(authToken: String = "", doctorId: String = "") {
        val call: Call<DataListResponse<DoctorAppointment>>? =
            RetrofitClient().getInstance()?.getApi()?.getDoctorAppointments(doctorId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<DoctorAppointment>> {
            override fun onResponse(
                call: Call<DataListResponse<DoctorAppointment>>,
                response: Response<DataListResponse<DoctorAppointment>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<DoctorAppointment>>, t: Throwable) {
                println(t.cause)
                mListener?.failureData(t)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getAppointmentsResponse: Response<DataListResponse<DoctorAppointment>>)
        fun failureData(t: Throwable)
    }

}