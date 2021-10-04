package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.GetPatientResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment(R.layout.profile_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }
    var fullName: TextView? = null
    var amka: TextView? = null
    var bloodType: TextView? = null
    var familyDoctor: TextView? = null

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fullName = view.findViewById(R.id.fullName)
        amka = view.findViewById(R.id.amka)
        bloodType = view.findViewById(R.id.bloodType)
        familyDoctor = view.findViewById(R.id.familyDoctor)

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)
        val authToken = sharedPreferences.getString(getString(R.string.auth_token), "")
        val patientId = sharedPreferences.getString(getString(R.string.patient_id), "")

        if (authToken != null && patientId != null) {
            getPatient(authToken, patientId)
        }

    }

    private fun getPatient(authToken: String = "", patientId: String = "") {
        val call: Call<GetPatientResponse>? =
            RetrofitClient().getInstance()?.getApi()?.getPatient(patientId, authToken)

        call!!.enqueue(object : Callback<GetPatientResponse> {
            override fun onResponse(
                call: Call<GetPatientResponse>,
                response: Response<GetPatientResponse>
            ) {
                if (response.code() == 200) {
                    val fullNameString = response.body()?.data?.name + " " + response.body()?.data?.lastName
                    fullName?.text = fullNameString
                    amka?.text = response.body()?.data?.amka
                    bloodType?.text = response.body()?.data?.bloodType
                    val doctorFullNameString =
                        response.body()?.data?.familyDoctor?.name + " " + response.body()?.data?.familyDoctor?.lastName
                    familyDoctor?.text = doctorFullNameString
                } else if (response.code() == 400) {
                    try {
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<GetPatientResponse>, t: Throwable) {}
        })

    }
}