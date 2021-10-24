package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.utils.animButtonPress
import com.devsoncall.okdoc.utils.animFadeIn
import com.devsoncall.okdoc.utils.animKeyIn
import kotlinx.android.synthetic.main.languages_fragment.*

class LanguagesFragment : Fragment(R.layout.languages_fragment) {
    private var mainMenuActivity: MainMenuActivity? = null
    private var sharedPreferences: SharedPreferences? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.languages_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animFadeIn(imageViewLanguages, it) }
        activity?.applicationContext?.let { animKeyIn(scrollViewLanguages, it) }

        val buttonEnglish: Button = view.findViewById(R.id.buttonEnglish)
        buttonEnglish.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(buttonEnglish, it) }
            changeLanguage("en")
        }

        val buttonGreek: Button = view.findViewById(R.id.buttonGreek)
        buttonGreek.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(buttonGreek, it) }
            changeLanguage("el")
        }

        val backButton: Button = view.findViewById(R.id.btBack)
        backButton.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(backButton, it) }
            view.findNavController().navigate(R.id.navigation_settings)
        }
    }

    private fun changeLanguage(language: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.language), language)
        editor?.apply()
        mainMenuActivity?.setLocale(language)
    }
}