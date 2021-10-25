package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiCreateAppointment
import com.devsoncall.okdoc.models.BasicResponse
import com.devsoncall.okdoc.utils.*
import kotlinx.android.synthetic.main.calendar_fragment.*
import kotlinx.android.synthetic.main.confirmation_fragment.*
import kotlinx.android.synthetic.main.confirmation_fragment.buttonBack
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class ConfirmationFragment : Fragment(R.layout.confirmation_fragment) {

    private var sharedPreferences: SharedPreferences? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.confirmation_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animKeyIn(viewConfirmationBackground, it) }
        activity?.applicationContext?.let { animFadeIn(textViewDoctorName, it) }
        activity?.applicationContext?.let { animFadeIn(textViewDoctorSpecialty, it) }
        activity?.applicationContext?.let { animFadeIn(textViewHospitalName, it) }
        activity?.applicationContext?.let { animFadeIn(textViewHospitalLocation, it) }
        activity?.applicationContext?.let { animFadeIn(textViewDate, it) }
        activity?.applicationContext?.let { animFadeIn(textViewTime, it) }
        activity?.applicationContext?.let { animFadeIn(viewBorders, it) }
        activity?.applicationContext?.let { animFadeIn(editTextReason, it) }



        val doctorName = sharedPreferences?.getString(getString(R.string.doctor_name), null)
        val doctorSpecialty = sharedPreferences?.getString(getString(R.string.profession_name_clicked), null)
        val hospitalName = sharedPreferences?.getString(getString(R.string.hospital_name), null)
        val hospitalLocation = sharedPreferences?.getString(getString(R.string.hospital_location), null)
        val date = sharedPreferences?.getString(getString(R.string.date_clicked), null)
        val dayOfWeek = sharedPreferences?.getString(getString(R.string.day_of_week_clicked), null)
        val time = sharedPreferences?.getString(getString(R.string.hour_clicked), null)

        val dateString = formatDateString(date, dayOfWeek)

        textViewDoctorName.text = doctorName
        textViewDoctorSpecialty.text = doctorSpecialty
        textViewHospitalName.text = "@ $hospitalName"
        textViewHospitalLocation.text = hospitalLocation
        textViewDate.text = dateString
        textViewTime.text = time

        buttonBack.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(buttonBack, it) }
            view.findNavController().navigate(R.id.navigation_hours)
        }

        buttonCancel.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(buttonCancel, it) }
            view.findNavController().navigate(R.id.navigation_home)
        }

        buttonConfirmAppointment.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(buttonConfirmAppointment, it) }
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
            val doctor = sharedPreferences?.getString(getString(R.string.doctor_id_clicked), null)
            val hospital = sharedPreferences?.getString(getString(R.string.hospital_id_clicked), null)
            if(authToken != "" && patientId != "" && authToken != null && patientId != null
                && date != "" && time != "" && date != null && time != null
                && doctor != "" && hospital != "" && doctor != null && hospital != null)
                if(ApiUtils().isOnline(this.requireContext()))
                    createAppointment(authToken, patientId, date, time, doctor, hospital)
                else
                    Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAppointment(authToken: String, patientId: String, date: String,
                                  time: String, doctor: String, hospital: String) {
        var reason = view?.findViewById<EditText>(R.id.editTextReason)?.text.toString()
        if (reason.isEmpty()){
            reason = "-"
        }

        mainMenuActivity?.loadingOverlay?.show()
        val apiCreateAppointment = ApiCreateAppointment()
        apiCreateAppointment.setOnDataListener(object : ApiCreateAppointment.DataInterface {
            override fun responseData(createAppointmentResponse: Response<BasicResponse>) {
                if (createAppointmentResponse.code() == 200) {
                    view?.findNavController()?.navigate(R.id.navigation_confirmed)
                } else if (createAppointmentResponse.code() == 400) {
                    try {
                        val jsonObject = JSONObject(createAppointmentResponse.errorBody()?.string())
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
        apiCreateAppointment.createAppointment(patientId, reason, doctor, hospital,
            date+"T"+time, authToken)
    }

}