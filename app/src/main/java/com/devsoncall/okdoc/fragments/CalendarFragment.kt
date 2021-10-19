package com.devsoncall.okdoc.fragments

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.calendar_fragment.*
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetDoctorAppointments
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.DoctorAppointment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.calendar_fragment.buttonBack
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type
import java.util.*
import java.util.ArrayList

class CalendarFragment : Fragment(R.layout.calendar_fragment) {

    private var sharedPreferences: SharedPreferences? = null
    private var mainMenuActivity: MainMenuActivity? = null
    private var appointments: List<DoctorAppointment>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedAppointments = sharedPreferences?.getString(getString(R.string.serialized_doctor_appointments), null)
        var clickedDayCalendar: Calendar = Calendar.getInstance()

        if (serializedAppointments != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<DoctorAppointment?>?>() {}.type
            appointments = gson.fromJson(serializedAppointments, type)
            appointments?.let { setDisabledDays(it) }
        } else {
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val doctorId = sharedPreferences?.getString(getString(R.string.doctor_id_clicked), "")
            if(authToken != "" && doctorId != "" && authToken != null && doctorId != null) {
                if(ApiUtils().isOnline(this.requireContext()))
                    getAppointments(authToken, doctorId)
                else
                    Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()
            }
        }

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_doctors)
        }

        calendarView.setOnDayClickListener { eventDay ->
            if (eventDay.isEnabled) {
                clickedDayCalendar = eventDay.calendar
                buttonConfirm.isEnabled = true;
            } else {
                Toast.makeText(this.context, "Select a valid date", Toast.LENGTH_SHORT).show()
            }
        }

        buttonConfirm.setOnClickListener {
            val date = clickedDayCalendar.get(Calendar.YEAR).toString() + "-" +
                    String.format("%02d", clickedDayCalendar.get(Calendar.MONTH) + 1) + "-" +
                    String.format("%02d", clickedDayCalendar.get(Calendar.DAY_OF_MONTH))
            val selectedDateBookedHours = appointments?.firstOrNull { it._id == date }

            saveSelectedDateHoursInPrefs(selectedDateBookedHours, date)
//            view.findNavController().navigate(R.id.navigation_hours)
            Toast.makeText(this.context, date, Toast.LENGTH_SHORT).show()
        }

    }

    private fun getAppointments(authToken: String = "", doctorId: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetAppointments = ApiGetDoctorAppointments()
        apiGetAppointments.setOnDataListener(object : ApiGetDoctorAppointments.DataInterface {
            override fun responseData(getAppointmentsResponse: Response<DataListResponse<DoctorAppointment>>) {
                if (getAppointmentsResponse.code() == 200) {
                    if (getAppointmentsResponse.body()?.data != null) {
                        appointments = getAppointmentsResponse.body()?.data!!
                        setDisabledDays(appointments!!)
                        saveDoctorAppointmentsInPrefs(appointments!!)
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
        apiGetAppointments.getAppointments(authToken, doctorId)
    }

    private fun setDisabledDays(doctorAppointments: List<DoctorAppointment>) {
        // available dates are within one month from current date
        val min = Calendar.getInstance()
        val max = Calendar.getInstance()
        max.add(Calendar.MONTH, 1)
        calendarView.setMinimumDate(min)
        calendarView.setMaximumDate(max)

        // check further availability based on doctor appointments
        val disabledDays = ArrayList<Calendar>()

        for (date in doctorAppointments) {

            if (date.booked_times.size < 16) {
                continue
            }

            val split: List<String> = date._id.split("-")
            val day = Calendar.getInstance()
            day.set(Calendar.YEAR, split[0].toInt())
            day.set(Calendar.MONTH, split[1].toInt() - 1)
            day.set(Calendar.DAY_OF_MONTH, split[2].toInt())

            disabledDays.add(day)
        }
        calendarView.setDisabledDays(disabledDays)
    }

    private fun saveDoctorAppointmentsInPrefs(appointments: List<DoctorAppointment>) {
        val gson = Gson()
        val serializedAppointments = gson.toJson(appointments)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.serialized_doctor_appointments), serializedAppointments)
        editor?.apply()
    }

    private fun saveSelectedDateHoursInPrefs(bookedTimes: DoctorAppointment?, date:String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        if(bookedTimes != null) {
            val gson = Gson()
            val serializedAppointments = gson.toJson(bookedTimes)
            editor?.putString(getString(R.string.serialized_booked_times), serializedAppointments)
        } else {
            editor?.remove(getString(R.string.serialized_booked_times))
        }
        editor?.putString(getString(R.string.date_clicked), date)
        editor?.apply()
    }
}