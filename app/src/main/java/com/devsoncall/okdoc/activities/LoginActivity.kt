package com.devsoncall.okdoc.activities

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.preference.PreferenceManager
import com.auth0.android.jwt.JWT
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.Token
import com.devsoncall.okdoc.api.Api
import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.BasicResponse
import org.json.JSONException


class LoginActivity : AppCompatActivity() {

    private lateinit var api: Api

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login)

        //Retrofit init
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://okdoc-backend.herokuapp.com/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        api = retrofit.create(Api::class.java)

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


        getStartedButton.setOnClickListener {
//            Log.i("Listener", "Listener is running");
            Toast.makeText(applicationContext, "Authenticating", Toast.LENGTH_SHORT).show()
//            createToken(amkaField.text.toString())
            login()
        }
    }

    //    @SuppressLint("SetTextI18n")
    private fun login() {
        val amka = findViewById<EditText>(R.id.editTextAmka).text.toString()

        if (amka.isEmpty()){
            Toast.makeText(applicationContext, "AMKA is required", Toast.LENGTH_LONG).show()
            return
        }

        if(amka.length != 11) {
            Toast.makeText(applicationContext, "Give a valid AMKA", Toast.LENGTH_LONG).show()
            return
        }

        val call: Call<BasicResponse>? =
            RetrofitClient().getInstance()?.getApi()?.createToken(amka)

        call!!.enqueue(object : Callback<BasicResponse> {
            override fun onResponse(
                call: Call<BasicResponse>,
                response: Response<BasicResponse>
            ) {
                if (response.code() == 200) {
                    val jwt = response.headers().get("authorization")
                    val decodedToken = jwt?.let { JWT(it) }
                    val patientId = decodedToken?.getClaim("_id")?.asString()
                    val sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(getString(R.string.auth_token), jwt)
                    editor.putString(getString(R.string.patient_id), patientId)
                    editor.commit();
                    successfulLogin()
                } else if (response.code() == 401) {
                    try {
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace();
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {}
        })


    }

    private fun successfulLogin() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

}