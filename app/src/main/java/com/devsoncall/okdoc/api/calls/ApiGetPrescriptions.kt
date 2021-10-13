package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import com.devsoncall.okdoc.models.Prescription
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetPrescriptions {

    private var mListener: DataInterface? = null

    fun getPrescriptions(authToken: String = "", patientId: String = "") {
        val call: Call<DataListResponse<Prescription>>? =
            RetrofitClient().getInstance()?.getApi()?.getPrescriptions(patientId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<Prescription>> {
            override fun onResponse(
                call: Call<DataListResponse<Prescription>>,
                response: Response<DataListResponse<Prescription>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<Prescription>>, t: Throwable) {
                println(t.cause)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getPrescriptionsResponse: Response<DataListResponse<Prescription>>)
    }
}