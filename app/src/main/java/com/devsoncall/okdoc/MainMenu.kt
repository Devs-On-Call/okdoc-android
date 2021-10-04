package com.devsoncall.okdoc

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.settings_fragment.*

class MainMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

//        val amka = intent.getStringExtra("EXTRA_AMKA")
//        val age = intent.getIntExtra("EXTRA_AGE", 0)
//
//        val person = intent.getSerializableExtra("EXTRA_PERSON") as Person
//
//        testText.text = person.amka
//
//        profileImage.setOnClickListener{
//            Intent(Intent.ACTION_GET_CONTENT).also {
//                it.type = "image/*"
//                startActivityForResult(it, 0)
//            }
//        }
//
//        val customList = listOf("First", "Second", "Third", "Fourth")
//        val adapter = ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, customList)
//        spinner.adapter = adapter
//        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                Toast.makeText(this@MainMenu,
//                    "You selected ${adapterView?.getItemAtPosition(position).toString()}",
//                    Toast.LENGTH_LONG).show()
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//
//            }
//        }

        val homeFragment = HomeFragment()
        val profileFragment = ProfileFragment()
        val settingsFragment = SettingsFragment()

        setCurrentFragment(homeFragment)

//        settingsFragment.logoutButton.setOnClickListener {
//            finish()
//        }

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.home -> setCurrentFragment(homeFragment)
                R.id.profile -> setCurrentFragment(profileFragment)
                R.id.settings -> setCurrentFragment(settingsFragment)
            }
            true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayoutFragment, fragment)
            commit()
        }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == Activity.RESULT_OK && requestCode == 0) {
//            val uri = data?.data
//            profileImage.setImageURI(uri)
//        }
//    }
}