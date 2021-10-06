package com.devsoncall.okdoc.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.LoginActivity


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
    }

    private fun logout() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(this.context)
        sharedPreferences.edit().clear().apply()

        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }
}