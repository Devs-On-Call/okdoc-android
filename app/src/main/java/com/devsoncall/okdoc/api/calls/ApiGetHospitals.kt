package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Hospital
import com.devsoncall.okdoc.models.Prescription
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetHospitals {

    private var mListener: DataInterface? = null

    fun getHospitals(authToken: String = "", professionId: String = "") {
        val call: Call<DataListResponse<Hospital>>? =
            RetrofitClient().getInstance()?.getApi()?.getHospitals(professionId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<Hospital>> {
            override fun onResponse(
                call: Call<DataListResponse<Hospital>>,
                response: Response<DataListResponse<Hospital>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<Hospital>>, t: Throwable) {
                println(t.cause)
                mListener?.failureData(t)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getHospitalsResponse: Response<DataListResponse<Hospital>>)
        fun failureData(t: Throwable)
    }
}