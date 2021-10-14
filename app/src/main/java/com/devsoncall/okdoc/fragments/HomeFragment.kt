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


class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)
        val patientName = sharedPreferences.getString(getString(R.string.patient_name), "")

        val hiField: TextView = view.findViewById<EditText>(R.id.textViewHi)
        hiField.text = getString(R.string.default_home_greeting) + " " + patientName + "!"

        val buttonPrescriptions = view.findViewById<Button>(R.id.buttonPrescriptions)
        buttonPrescriptions.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_prescription_list)
        }

        val buttonDiagnoses = view.findViewById<Button>(R.id.buttonPastDiagnoses)
        buttonDiagnoses.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_diagnoses_list)
        }

        val buttonScheduleAppointment = view.findViewById<Button>(R.id.buttonScheduleAppointment)
        buttonScheduleAppointment.setOnClickListener { view ->
            // TODO
            // add dummy professionId so that hospitals screen work
            // remove it when profession screen is done
            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
            editor?.putString(getString(R.string.profession_id_clicked), "615c425614da612a4f026f6a")
            editor?.apply()
            view.findNavController().navigate(R.id.navigation_hospital_list)
        }




    }
}