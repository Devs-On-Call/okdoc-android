package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.utils.animKeyIn
import com.devsoncall.okdoc.utils.animPushUpIn
import com.devsoncall.okdoc.utils.setAvatar
import kotlinx.android.synthetic.main.profile_fragment.*


class ProfileFragment : Fragment(R.layout.profile_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animPushUpIn(scrollViewProfile, it) }
        activity?.applicationContext?.let { animKeyIn(imageViewAvatar, it) }

        val sharedPreferences: SharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)

        val amkaString = sharedPreferences.getString(getString(R.string.patient_amka), "")
        setAvatar(amkaString, imageViewAvatar)

        val fullName: TextView = view.findViewById(R.id.fullName)
        val amka: TextView = view.findViewById(R.id.amka)
        val bloodType: TextView = view.findViewById(R.id.bloodType)
        val familyDoctor: TextView = view.findViewById(R.id.familyDoctor)

        val patientName = sharedPreferences.getString(getString(R.string.patient_name), "")
        val patientLastName = sharedPreferences.getString(getString(R.string.patient_last_name), "")

        fullName.text = "$patientName $patientLastName"
        amka.text = sharedPreferences.getString(getString(R.string.patient_amka), "")
        bloodType.text = sharedPreferences.getString(getString(R.string.patient_blood_type), "")
        familyDoctor.text = sharedPreferences.getString(getString(R.string.patient_doctor), "")
    }
}