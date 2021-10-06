package com.devsoncall.okdoc.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.fragments.HomeFragment
import com.devsoncall.okdoc.fragments.PrescriptionFragment
import com.devsoncall.okdoc.fragments.ProfileFragment
import com.devsoncall.okdoc.fragments.SettingsFragment
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.prescription_fragment.*

class MainMenuActivity : AppCompatActivity() {





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val settingsFragment = SettingsFragment()
        val prescriptionFragment = PrescriptionFragment()


        // default fragment on activity creation
        setCurrentFragment(prescriptionFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
                R.id.settings -> setCurrentFragment(settingsFragment)
            }
            true
        }

//        homeFragment.buttonPrescriptions.setOnClickListener {
////            setCurrentFragment(prescriptionFragment)
//        }
    }

    fun test() {
        Toast.makeText(this, "yay", Toast.LENGTH_LONG).show()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, fragment)
            addToBackStack(null)
            commit()
        }

}

