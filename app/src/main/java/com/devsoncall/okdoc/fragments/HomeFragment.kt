package com.devsoncall.okdoc.fragments


import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.utils.*
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment(R.layout.home_fragment) {

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        activity?.applicationContext?.let { animPushUpIn(scrollViewHome, it) }
        activity?.applicationContext?.let { animKeyIn(scrollViewHome, it) }
        activity?.applicationContext?.let { animKeyIn(textViewHi, it) }
        activity?.applicationContext?.let { animKeyIn(imageViewAvatar, it) }

        clearSharedPrefsFromAppointmentCreation()

        val patientName = sharedPreferences?.getString(getString(R.string.patient_name), "")
        val hiField: TextView = view.findViewById<EditText>(R.id.textViewHi)
        hiField.text = getString(R.string.default_home_greeting) + ", " + "\n     " +patientName + "!"

        val amka = sharedPreferences?.getString(getString(R.string.patient_amka), "")
        setAvatar(amka, imageViewAvatar)

        val buttonPrescriptions = view.findViewById<Button>(R.id.buttonPrescriptions)
        buttonPrescriptions.setOnClickListener { view ->
            activity?.applicationContext?.let { animButtonPress(view, it) }
            view.findNavController().navigate(R.id.action_navigation_home_to_prescription_list)
        }

        val buttonDiagnoses = view.findViewById<Button>(R.id.buttonPastDiagnoses)
        buttonDiagnoses.setOnClickListener { view ->
            activity?.applicationContext?.let { animButtonPress(view, it) }
            view.findNavController().navigate(R.id.action_navigation_home_to_diagnoses_list)
        }

        val buttonAppointments = view.findViewById<Button>(R.id.buttonViewAppointments)
        buttonAppointments.setOnClickListener { view ->
            activity?.applicationContext?.let { animButtonPress(view, it) }
            view.findNavController().navigate(R.id.action_navigation_home_to_appointments)

        }

        val buttonScheduleAppointment = view.findViewById<Button>(R.id.buttonScheduleAppointment)
        buttonScheduleAppointment.setOnClickListener { view ->
            activity?.applicationContext?.let { animButtonPress(view, it) }
            view.findNavController().navigate(R.id.action_navigation_home_to_professions)

        }
    }

    private fun clearSharedPrefsFromAppointmentCreation() {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.remove(getString(R.string.serialized_booked_times))
        editor?.remove(getString(R.string.serialized_doctor_appointments))
        editor?.remove(getString(R.string.hospital_id_clicked))
        editor?.remove(getString(R.string.doctor_id_clicked))
        editor?.remove(getString(R.string.date_clicked))
        editor?.remove(getString(R.string.hour_clicked))
        editor?.remove(getString(R.string.day_of_week_clicked))
        editor?.apply()
    }
}