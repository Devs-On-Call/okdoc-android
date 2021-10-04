package com.devsoncall.okdoc

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.android.synthetic.main.activity_login.*
import android.widget.Toast
import androidx.core.app.ActivityCompat


class Login : AppCompatActivity() {

    private lateinit var backendApi: BackendApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login)

        //Retrofit init
        val retrofit = Retrofit.Builder()
            .baseUrl("https://okdoc-backend.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        backendApi = retrofit.create(BackendApi::class.java)

        val amkaField = findViewById<EditText>(R.id.editTextAmka)
        val getStartedButton = findViewById<Button>(R.id.getStartedButton)
        val loginImage = findViewById<View>(R.id.doctors_image)
        val activityRoot = findViewById<ConstraintLayout>(R.id.activity_root)

//        amkaField.setOnFocusChangeListener { v, hasFocus ->
//            Log.i("hasFocus", hasFocus.toString());
//            if (hasFocus) {
//                loginImage.visibility = View.GONE
//            } else {
//                loginImage.visibility = View.VISIBLE
//            }
//        }

        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Confirmation")
            .setMessage("Are you sure?")
            .setIcon(R.drawable.ic_login)
            .setPositiveButton("Yes") { _, i ->
                Toast.makeText(this, "Confirmed", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("No") { _, i ->
                Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
            }.create()

        val options = arrayOf("First Item", "Second Item", "Third item")
        val choiceDialog = AlertDialog.Builder(this)
            .setTitle("Select one option")
//            .setSingleChoiceItems(options, 0, ) {dialogInterface, i ->
//                Toast.makeText(this, "You clicked on ${options[i]}", Toast.LENGTH_SHORT).show()
//            }
            .setMultiChoiceItems(options, booleanArrayOf(false, false, false)) {_, i, isChecked ->
                if (isChecked) {
                    Toast.makeText(this, "You checked ${options[i]}", Toast.LENGTH_SHORT).show()
            } else {
                    Toast.makeText(this, "You unchecked ${options[i]}", Toast.LENGTH_SHORT).show()
                }
        }
            .setPositiveButton("Accept") { _, i ->
                Toast.makeText(this, "You accepted", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel") { _, i ->
                Toast.makeText(this, "You declined", Toast.LENGTH_SHORT).show()
            }.create()

        getStartedButton.setOnClickListener {
            Log.i("Listener", "Listener is running");
            Toast.makeText(applicationContext, "Authenticating", Toast.LENGTH_SHORT).show()
//            createToken(amkaField.text.toString())


//            alertDialog.show()
//            choiceDialog.show()
//            requestPermissions()
//            Intent(this, FragmentActivity::class.java).also { startActivity(it) }


            val amka = amkaField.text.toString()
            val age = 1
            val person = Person(amka, age)
            Intent(this, MainMenu::class.java).also {
//                it.putExtra("EXTRA_AMKA", amka)
//                it.putExtra("EXTRA_AGE", age)
                it.putExtra("EXTRA_PERSON", person)
                startActivity(it)
            }

        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun hasInternetAccess() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED

    private fun hasLocationForegroundPermission() =
        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun requestPermissions() {
        var permissionsToRequest = mutableListOf<String>()
        if (!hasInternetAccess()) {
            permissionsToRequest.add(Manifest.permission.INTERNET)
        }
        if (!hasLocationForegroundPermission()) {
            permissionsToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (permissionsToRequest.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toTypedArray(), 0)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                 if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                     Log.d("PermissionsRequest", "${permissions[i]} granted.")
                 }
            }
        }
    }

    private fun createToken(amka: String) {
        val call: Call<Token> = backendApi.createToken(amka)
        Log.i("Listener", "call has been created for amka $amka")
        call.enqueue(
            object : Callback<Token> {
                override fun onResponse( call: Call<Token>, response: Response<Token>) {
                    Log.i("Response", "Response received");

                    if (!response.isSuccessful) {
                        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
                        Toast.makeText(applicationContext, jsonObj.getString("message"), Toast.LENGTH_LONG).show()
                        Log.i("Response", "Response not successful");
                        return
                    }

                    val token: Token? = response.body()

                    Log.i("Response", response.message());
                    Toast.makeText(applicationContext, token?.message, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Log.i("Response", "Response failed");
                    Toast.makeText(applicationContext, t.localizedMessage, Toast.LENGTH_LONG).show()
                }
            }
        )
    }
}