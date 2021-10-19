package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.BookedHours
import com.devsoncall.okdoc.models.DataListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetHours {

    private var mListener: DataInterface? = null

    fun getHours(authToken: String = "", doctorId: String = "") {
        val call: Call<DataListResponse<BookedHours>>? =
            RetrofitClient().getInstance()?.getApi()?.getHours(doctorId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<BookedHours>> {
            override fun onResponse(
                call: Call<DataListResponse<BookedHours>>,
                response: Response<DataListResponse<BookedHours>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<BookedHours>>, t: Throwable) {
                println(t.cause)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getHoursResponse: Response<DataListResponse<BookedHours>>)
    }

}