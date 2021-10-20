package com.devsoncall.okdoc.fragments

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import kotlinx.android.synthetic.main.confirmation_fragment.*


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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_hours)
        }

        buttonCancel.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }

        buttonConfirmAppointment.setOnClickListener { view ->
            // TODO
            // navigate to confirmed fragment or error fragment
//            view.findNavController().navigate(R.id.navigation_confirmed)
//            Toast.makeText(this.context, "Confirm", Toast.LENGTH_SHORT).show()
        }
    }
}