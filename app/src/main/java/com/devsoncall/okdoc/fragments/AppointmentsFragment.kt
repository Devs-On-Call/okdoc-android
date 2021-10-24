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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.AppointmentsAdapter
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetPatientAppointments
import com.devsoncall.okdoc.models.PatientAppointment
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.utils.animButtonPress
import com.devsoncall.okdoc.utils.animKeyIn
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.appointments_fragment.*
import kotlinx.android.synthetic.main.diagnoses_list_fragment.*
import kotlinx.android.synthetic.main.error_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type


class AppointmentsFragment : Fragment(R.layout.appointments_fragment), AppointmentsAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: AppointmentsAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null

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

        activity?.applicationContext?.let { animKeyIn(swipe_appointments_container, it) }

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_appointments_container)
        mSwipeRefreshLayout!!.setOnRefreshListener(this)
        mSwipeRefreshLayout!!.setColorSchemeResources(
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        val serializedAppointments = sharedPreferences?.getString(getString(R.string.serialized_patient_appointments), null)
        val appointments: List<PatientAppointment>

        if (serializedAppointments != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<PatientAppointment?>?>() {}.type
            appointments = gson.fromJson(serializedAppointments, type)
            setAdapter(appointments)
        } else {
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
            if(authToken != "" && patientId != "" && authToken != null && patientId != null)
                if(ApiUtils().isOnline(this.requireContext()))
                    getPatientAppointments(authToken, patientId)
                else
                    Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            activity?.applicationContext?.let { animButtonPress(view, it) }
            view.findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onItemClick(position: Int, view: View) {
        activity?.applicationContext?.let { animButtonPress(view, it) }
//        Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRefresh() {
        val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
        val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
        if(authToken != "" && patientId != "" && authToken != null && patientId != null)
            if(ApiUtils().isOnline(this.requireContext())) {
                mSwipeRefreshLayout!!.isRefreshing = true
                getPatientAppointments(authToken, patientId)
                activity?.applicationContext?.let { animKeyIn(swipe_appointments_container, it) }

            } else {
                Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun getPatientAppointments(authToken: String = "", patientId: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetAppointments = ApiGetPatientAppointments()
        apiGetAppointments.setOnDataListener(object : ApiGetPatientAppointments.DataInterface {
            override fun responseData(getAppointmentsResponse: Response<DataListResponse<PatientAppointment>>) {
                if (getAppointmentsResponse.code() == 200) {
                    if (getAppointmentsResponse.body()?.data != null) {
                        val appointments = getAppointmentsResponse.body()?.data!!
                        savePatientAppointmentsInPrefs(appointments)
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
                    mainMenuActivity?.loadingOverlay?.dismiss()
                    view?.findNavController()?.navigate(R.id.navigation_error)
                }
                mSwipeRefreshLayout!!.isRefreshing = false
                mainMenuActivity?.loadingOverlay?.dismiss()
            }

            override fun failureData(t: Throwable) {
                mSwipeRefreshLayout!!.isRefreshing = false
                mainMenuActivity?.loadingOverlay?.dismiss()
                view?.findNavController()?.navigate(R.id.navigation_error)
            }
        })
        apiGetAppointments.getAppointments(authToken, patientId)
    }

    private fun savePatientAppointmentsInPrefs(appointments: List<PatientAppointment>) {
        val gson = Gson()
        val serializedAppointments = gson.toJson(appointments)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.serialized_patient_appointments), serializedAppointments)
        editor?.apply()
    }

    private fun setAdapter(appointments: List<PatientAppointment>) {
        adapter = AppointmentsAdapter(appointments, this)
        rvAppointments.adapter = adapter
        rvAppointments.layoutManager = LinearLayoutManager(this.context)
    }
}