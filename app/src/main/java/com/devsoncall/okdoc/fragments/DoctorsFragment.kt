package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.DoctorsAdapter
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetDoctors
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Doctor
import com.devsoncall.okdoc.utils.animButtonPress
import com.devsoncall.okdoc.utils.animKeyIn
import kotlinx.android.synthetic.main.doctors_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class DoctorsFragment : Fragment(R.layout.doctors_fragment), DoctorsAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: DoctorsAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.doctors_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animKeyIn(rvDoctors, it) }

        val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
        val professionId = sharedPreferences?.getString(getString(R.string.profession_id_clicked), "")
        val hospitalId = sharedPreferences?.getString(getString(R.string.hospital_id_clicked), "")

        if(authToken != "" && professionId != "" && hospitalId != "" &&
            authToken != null && professionId != null && hospitalId != null)
            if(ApiUtils().isOnline(this.requireContext()))
                getDoctors(authToken, professionId, hospitalId)
            else
                Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            activity?.applicationContext?.let { animButtonPress(view, it) }
            view.findNavController().navigate(R.id.navigation_hospital_list)
        }
    }

    override fun onItemClick(doctor: Doctor, view: View) {
        activity?.applicationContext?.let { animButtonPress(view, it) }
        val pastDoctorId = sharedPreferences?.getString(getString(R.string.doctor_id_clicked), "")
        if (pastDoctorId!= "" && pastDoctorId != doctor._id)
            clearDoctorAppointmentsFromSharedPrefs()
        saveDoctorClickedInPrefs(doctor._id, doctor.name + " " + doctor.lastName)
        view.findNavController().navigate(R.id.navigation_calendar)
    }

    private fun getDoctors(authToken: String = "", professionId: String = "", hospitalId: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetDoctors = ApiGetDoctors()
        apiGetDoctors.setOnDataListener(object : ApiGetDoctors.DataInterface {
            override fun responseData(getDoctorsResponse: Response<DataListResponse<Doctor>>) {
                if (getDoctorsResponse.code() == 200) {
                    if (getDoctorsResponse.body()?.data != null) {
                        val doctors = getDoctorsResponse.body()?.data!!
                        setAdapter(doctors)
                    }
                } else if (getDoctorsResponse.code() == 400) {
                    try {
                        val jsonObject = JSONObject(getDoctorsResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    mainMenuActivity?.loadingOverlay?.dismiss()
                    view?.findNavController()?.navigate(R.id.navigation_error)
                }
                mainMenuActivity?.loadingOverlay?.dismiss()
            }

            override fun failureData(t: Throwable) {
                mainMenuActivity?.loadingOverlay?.dismiss()
                view?.findNavController()?.navigate(R.id.navigation_error)
            }
        })
        apiGetDoctors.getDoctors(authToken, professionId, hospitalId)
    }

    private fun saveDoctorClickedInPrefs(doctorIdClicked: String, doctorNameClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.doctor_id_clicked), doctorIdClicked)
        editor?.putString(getString(R.string.doctor_name), doctorNameClicked)
        editor?.apply()
    }

    private fun setAdapter(doctors: List<Doctor>){
        adapter = DoctorsAdapter(doctors, this)
        rvDoctors.adapter = adapter
        rvDoctors.layoutManager = LinearLayoutManager(this.context)
    }

    private fun clearDoctorAppointmentsFromSharedPrefs() {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.remove(getString(R.string.serialized_doctor_appointments))
        editor?.apply()
    }

}