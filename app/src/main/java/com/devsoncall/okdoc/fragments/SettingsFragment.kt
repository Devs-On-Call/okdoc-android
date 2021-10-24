package com.devsoncall.okdoc.fragments


import android.annotation.SuppressLint
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.LoginActivity
import com.devsoncall.okdoc.utils.animButtonPress
import com.devsoncall.okdoc.utils.animFadeIn
import com.devsoncall.okdoc.utils.animKeyIn
import kotlinx.android.synthetic.main.settings_fragment.*


class SettingsFragment : Fragment(R.layout.settings_fragment) {

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        return inflater.inflate(R.layout.settings_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animFadeIn(imageViewSettings, it) }
        activity?.applicationContext?.let { animKeyIn(scrollView2, it) }

        val logoutButton: Button = view.findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(logoutButton, it) }
            logout()
        }

        val creditsButton: Button = view.findViewById(R.id.creditsButton)
        creditsButton.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(creditsButton, it) }
            view.findNavController().navigate(R.id.navigation_credits)
        }

        val languagesButton:Button = view.findViewById(R.id.languagesButton)
        languagesButton.setOnClickListener{
            activity?.applicationContext?.let { animButtonPress(languagesButton, it) }
            view.findNavController().navigate(R.id.navigation_languages)
        }

    }

    private fun logout() {
        clearPrefs()

        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    private fun clearPrefs() {
        val language = sharedPreferences?.getString(getString(R.string.language), null)
        sharedPreferences?.edit()?.clear()?.apply()
        if (language != null) {
            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
            editor?.putString(getString(R.string.language), language)
            editor?.apply()
        }
    }
}