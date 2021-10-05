package com.devsoncall.okdoc.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
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
    }
}