package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.BasicResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiLogin {

    private var mListener: DataInterface? = null

    fun login(amka: String) {
        val call: Call<BasicResponse>? =
            RetrofitClient().getInstance()?.getApi()?.createToken(amka)

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
        fun responseData(loginResponse: Response<BasicResponse>)
        fun failureData(t: Throwable)
    }

}