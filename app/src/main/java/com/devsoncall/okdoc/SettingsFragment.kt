package com.devsoncall.okdoc


import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.settings_fragment.*


class SettingsFragment : Fragment(R.layout.settings_fragment) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        val applicationContext = activity?.applicationContext
//        logoutButton.setOnClickListener {
//            Toast.makeText(applicationContext, "Logging out", Toast.LENGTH_SHORT).show()
//            activity?.finish()
//        }
    }
}