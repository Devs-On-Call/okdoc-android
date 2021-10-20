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
import com.devsoncall.okdoc.activities.MainMenuActivity
import kotlinx.android.synthetic.main.confirmation_fragment.*
import java.text.DateFormatSymbols

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_hours)
        }

        buttonCancel.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }

        buttonConfirmAppointment.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_confirmed)
            // TODO
            // navigate to confirmed error fragment in case of error
            // post the scheduled appointment
//            Toast.makeText(this.context, "Confirm", Toast.LENGTH_SHORT).show()

        }
    }

    private fun formatDateString(date: String?, dayOfWeek: String?): String {

        var dateString = ""

        // Day of week string
        dateString += DateFormatSymbols().weekdays[dayOfWeek?.toInt()!!]


        // date has yyyy-mm-dd format
        // split to access year month and day separately
        val split: List<String>? = date?.split("-")

        // Day of month
        dateString += if (split?.get(2)?.startsWith("0") == true) {
            ", " + split[2].drop(1)
        } else {
            ", " + split?.get(2)
        }

        // Month string
        dateString += " " + DateFormatSymbols().months[split?.get(1)?.toInt()?.minus(1)!!]

        // Year
        dateString += " " + split?.get(0)

        return dateString
    }
}