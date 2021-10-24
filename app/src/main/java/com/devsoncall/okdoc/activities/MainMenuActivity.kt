package com.devsoncall.okdoc.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.devsoncall.okdoc.R
import com.mohamedabulgasem.loadingoverlay.LoadingAnimation
import com.mohamedabulgasem.loadingoverlay.LoadingOverlay
import android.content.res.Configuration
import android.content.res.Resources
import androidx.preference.PreferenceManager
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import java.util.*


class MainMenuActivity : AppCompatActivity() {

    val loadingOverlay: LoadingOverlay by lazy {
        LoadingOverlay.with(
            context = this@MainMenuActivity,
            animation = LoadingAnimation.FADING_PROGRESS
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_OkDoc)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@MainMenuActivity)
        val language = sharedPreferences?.getString(getString(R.string.language), null)
        if(language != null) setLocale(language)

        setContentView(R.layout.activity_main_menu)

        val bottomNavigation: MeowBottomNavigation = findViewById(R.id.bottomNavigationView)

        bottomNavigation.add(MeowBottomNavigation.Model(R.id.navigation_profile, R.drawable.ic_profile))
        bottomNavigation.add(MeowBottomNavigation.Model(R.id.navigation_home, R.drawable.ic_home_image))
        bottomNavigation.add(MeowBottomNavigation.Model(R.id.navigation_settings, R.drawable.ic_settings_image))

        bottomNavigation.setOnClickMenuListener {
            when (it.id) {
                R.id.navigation_profile -> findNavController(R.id.nav_host_fragment).navigate(it.id)
                R.id.navigation_home -> findNavController(R.id.nav_host_fragment).navigate(it.id)
                R.id.navigation_settings -> findNavController(R.id.nav_host_fragment).navigate(it.id)
            }
        }

        bottomNavigation.setOnReselectListener {
            when (it.id) {
                R.id.navigation_profile -> findNavController(R.id.nav_host_fragment).navigate(it.id)
                R.id.navigation_home -> findNavController(R.id.nav_host_fragment).navigate(it.id)
                R.id.navigation_settings -> findNavController(R.id.nav_host_fragment).navigate(it.id)
            }
        }

        bottomNavigation.setOnShowListener {


        }

        bottomNavigation.show(R.id.navigation_home, true)
        findNavController(R.id.nav_host_fragment).navigate(R.id.navigation_home)

        // OLD BOTTOM NAV
//        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
//        val navController = findNavController(R.id.nav_host_fragment)
//        navView.setupWithNavController(navController)
    }

    fun setLocale(languageCode: String) {
        val currentLanguage = Locale.getDefault().language
        if (languageCode == currentLanguage) return

        // change language
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val resources: Resources = this.resources
        val config: Configuration = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // reload activity
        this.recreate()
    }
}
