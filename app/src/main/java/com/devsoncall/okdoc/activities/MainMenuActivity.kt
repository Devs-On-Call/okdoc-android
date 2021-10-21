package com.devsoncall.okdoc.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.devsoncall.okdoc.R
import com.mohamedabulgasem.loadingoverlay.LoadingAnimation
import com.mohamedabulgasem.loadingoverlay.LoadingOverlay


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
        setContentView(R.layout.activity_main_menu)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setupWithNavController(navController)
    }
}
