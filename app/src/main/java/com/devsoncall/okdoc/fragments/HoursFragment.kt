package com.devsoncall.okdoc.fragments

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.HoursAdapter
import com.devsoncall.okdoc.models.DoctorAppointment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.hours_fragment.*
import java.lang.reflect.Type

class HoursFragment : Fragment(R.layout.hours_fragment), HoursAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: HoursAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null
    private var appointments: List<DoctorAppointment>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.hours_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedHours = sharedPreferences?.getString(getString(R.string.serialized_booked_times), null)
        val dateClicked = sharedPreferences?.getString(getString(R.string.date_clicked), null)
        val doctorAppointment: DoctorAppointment

        if (dateClicked != null) {
            var bookedHours: List<String> = emptyList()
            if (serializedHours != null) {
                val gson = Gson()
                val type: Type = object : TypeToken<DoctorAppointment?>() {}.type
                doctorAppointment = gson.fromJson(serializedHours, type)
                bookedHours = doctorAppointment.booked_times
            }
            val availableHours = availableHours(bookedHours)
            setAdapter(availableHours)
        }

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_calendar)
        }
    }

    override fun onItemClick(hour: String, view: View) {
        saveHourClickedInPrefs(hour)
        view.findNavController().navigate(R.id.navigation_confirmation)
    }

    private fun availableHours(bookedHours: List<String>): List<String> {
        val timeSlots = listOf("09:00", "09:30", "10:00", "10:30", "11:00",
            "11:30", "12:00", "12:30", "13:00", "13:30", "14:00",
            "14:30", "15:00", "15:30", "16:00", "16:30")
        val sum = timeSlots + bookedHours
        return sum.groupBy { it }.filter { it.value.size == 1 }.flatMap { it.value }
    }


    private fun saveHourClickedInPrefs(hourClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.hour_clicked), hourClicked)
        editor?.apply()
    }

    private fun setAdapter(hours: List<String>){
        adapter = HoursAdapter(hours, this)
        rvHours.adapter = adapter
        rvHours.layoutManager = LinearLayoutManager(this.context)
    }

}