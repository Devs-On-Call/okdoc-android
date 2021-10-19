package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import com.devsoncall.okdoc.models.Diagnosis
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetDiagnoses {

    private var mListener: DataInterface? = null

    fun getDiagnoses(authToken: String = "", patientId: String = "") {
        val call: Call<DataListResponse<Diagnosis>>? =
            RetrofitClient().getInstance()?.getApi()?.getDiagnoses(patientId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<Diagnosis>> {
            override fun onResponse(
                call: Call<DataListResponse<Diagnosis>>,
                response: Response<DataListResponse<Diagnosis>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<Diagnosis>>, t: Throwable) {
                println(t.cause)
                mListener?.failureData(t)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getDiagnosesResponse: Response<DataListResponse<Diagnosis>>)
        fun failureData(t: Throwable)
    }
}