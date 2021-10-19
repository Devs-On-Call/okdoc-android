package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Doctor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetDoctors {

    private var mListener: DataInterface? = null

    fun getDoctors(authToken: String = "", professionId: String = "", hospitalId: String = "") {
        val call: Call<DataListResponse<Doctor>>? =
            RetrofitClient().getInstance()?.getApi()?.getDoctors(professionId, hospitalId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<Doctor>> {
            override fun onResponse(
                call: Call<DataListResponse<Doctor>>,
                response: Response<DataListResponse<Doctor>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<Doctor>>, t: Throwable) {
                println(t.cause)
                mListener?.failureData(t)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getDoctorsResponse: Response<DataListResponse<Doctor>>)
        fun failureData(t: Throwable)
    }

}