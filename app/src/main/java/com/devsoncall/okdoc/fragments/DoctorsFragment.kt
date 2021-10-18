package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.adapters.DoctorsAdapter
import com.devsoncall.okdoc.api.calls.ApiGetDoctors
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Doctor
import kotlinx.android.synthetic.main.doctors_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class DoctorsFragment : Fragment(R.layout.doctors_fragment), DoctorsAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: DoctorsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        return inflater.inflate(R.layout.doctors_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
        val professionId = sharedPreferences?.getString(getString(R.string.profession_id_clicked), "")
        val hospitalId = sharedPreferences?.getString(getString(R.string.hospital_id_clicked), "")

        if(authToken != "" && professionId != "" && hospitalId != "" &&
            authToken != null && professionId != null && hospitalId != null)
            getDoctors(authToken, professionId, hospitalId)

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().popBackStack()
        }
    }

    override fun onItemClick(doctor: Doctor, view: View) {
//        view.findNavController().navigate(R.id.navigation_calendar) //navigation_calendar
        saveDoctorIdClickedInPrefs(doctor._id)
    }

    private fun getDoctors(authToken: String = "", professionId: String = "", hospitalId: String = "") {
        val apiGetDoctors = ApiGetDoctors()
        apiGetDoctors.setOnDataListener(object : ApiGetDoctors.DataInterface {
            override fun responseData(getDoctorsResponse: Response<DataListResponse<Doctor>>) {
                if (getDoctorsResponse.code() == 200) {
//                    loadingOverlay.dismiss()
                    if (getDoctorsResponse.body()?.data != null) {
                        val hospitals = getDoctorsResponse.body()?.data!!
                        setAdapter(hospitals)
                    }
                } else if (getDoctorsResponse.code() == 400) {
                    try {
//                        loadingOverlay.dismiss()
                        val jsonObject = JSONObject(getDoctorsResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        })
        apiGetDoctors.getDoctors(authToken, professionId, hospitalId)
    }

    private fun saveDoctorIdClickedInPrefs(doctorIdClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.doctor_id_clicked), doctorIdClicked)
        editor?.apply()
    }

    private fun setAdapter(doctors: List<Doctor>){
        adapter = DoctorsAdapter(doctors, this)
        rvDoctors.adapter = adapter
        rvDoctors.layoutManager = LinearLayoutManager(this.context)
    }

}