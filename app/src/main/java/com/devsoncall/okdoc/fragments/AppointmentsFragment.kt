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
import com.devsoncall.okdoc.adapters.AppointmentsAdapter
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetAppointments
import com.devsoncall.okdoc.models.Appointment
import com.devsoncall.okdoc.models.DataListResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.appointments_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type


class AppointmentsFragment : Fragment(R.layout.appointments_fragment), AppointmentsAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: AppointmentsAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.appointments_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedAppointments = sharedPreferences?.getString(getString(R.string.serialized_appointments), null)
        val appointments: List<Appointment>

        if (serializedAppointments != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Appointment?>?>() {}.type
            appointments = gson.fromJson(serializedAppointments, type)
            setAdapter(appointments)
        } else {
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
            if(authToken != "" && patientId != "" && authToken != null && patientId != null)
                if(ApiUtils().isOnline(this.requireContext()))
                    getAppointments(authToken, patientId)
                else
                    Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onItemClick(position: Int, view: View) {
//        Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()
    }

    private fun getAppointments(authToken: String = "", patientId: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetAppointments = ApiGetAppointments()
        apiGetAppointments.setOnDataListener(object : ApiGetAppointments.DataInterface {
            override fun responseData(getAppointmentsResponse: Response<DataListResponse<Appointment>>) {
                if (getAppointmentsResponse.code() == 200) {
                    if (getAppointmentsResponse.body()?.data != null) {
                        val appointments = getAppointmentsResponse.body()?.data!!
                        saveAppointmentsInPrefs(appointments)
                        setAdapter(appointments)
                    }
                } else if (getAppointmentsResponse.code() == 400) {
                    try {
                        val jsonObject = JSONObject(getAppointmentsResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                mainMenuActivity?.loadingOverlay?.dismiss()
            }

            override fun failureData(t: Throwable) {
                mainMenuActivity?.loadingOverlay?.dismiss()
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        })
        apiGetAppointments.getAppointments(authToken, patientId)
    }

    private fun saveAppointmentsInPrefs(appointments: List<Appointment>) {
        val gson = Gson()
        val serializedAppointments = gson.toJson(appointments)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.serialized_appointments), serializedAppointments)
        editor?.apply()
    }

    private fun setAdapter(appointments: List<Appointment>) {
        adapter = AppointmentsAdapter(appointments, this)
        rvAppointments.adapter = adapter
        rvAppointments.layoutManager = LinearLayoutManager(this.context)
    }

}