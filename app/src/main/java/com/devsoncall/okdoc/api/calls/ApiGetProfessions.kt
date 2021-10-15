package com.devsoncall.okdoc.api.calls

import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Profession
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiGetProfessions {

    private var mListener: DataInterface? = null

    fun getProfessions(authToken: String = "", professionId: String = "") {
        val call: Call<DataListResponse<Profession>>? =
            RetrofitClient().getInstance()?.getApi()?.getProfessions(professionId, authToken)

        call!!.enqueue(object : Callback<DataListResponse<Profession>> {
            override fun onResponse(
                call: Call<DataListResponse<Profession>>,
                response: Response<DataListResponse<Profession>>
            ) {
                mListener?.responseData(response)
            }

            override fun onFailure(call: Call<DataListResponse<Profession>>, t: Throwable) {
                println(t.cause)
            }
        })
    }

    fun setOnDataListener(listener: DataInterface) {
        mListener = listener
    }

    interface DataInterface {
        fun responseData(getProfessionsResponse: Response<DataListResponse<Profession>>)
    }
}