package com.devsoncall.okdoc.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.LoginActivity
import kotlinx.android.synthetic.main.settings_fragment.*


class SettingsFragment : Fragment(R.layout.settings_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            logout()
        }

        val creditsButton: Button = view.findViewById(R.id.creditsButton)
        creditsButton.setOnClickListener {
            view.findNavController().navigate(R.id.navigation_credits)
        }

        val languagesButton:Button = view.findViewById(R.id.languagesButton)
        languagesButton.setOnClickListener{
            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(intent)
        }

    }

    private fun logout() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }
}