package com.devsoncall.okdoc.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.auth0.android.jwt.JWT
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.api.RetrofitClient
import com.devsoncall.okdoc.models.BasicResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import org.json.JSONException
import org.json.JSONObject
import retrofit2.*


class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_login)

        val getStartedButton = findViewById<Button>(R.id.getStartedButton)
//        val loginImage = findViewById<View>(R.id.doctors_image)

//        amkaField.setOnFocusChangeListener { v, hasFocus ->
//            Log.i("hasFocus", hasFocus.toString());
//            if (hasFocus) {
//                loginImage.visibility = View.GONE
//            } else {
//                loginImage.visibility = View.VISIBLE
//            }
//        }

        getStartedButton.setOnClickListener {
            Toast.makeText(applicationContext, "Authenticating", Toast.LENGTH_SHORT).show()
            // loader.visibility = View.VISIBLE
            login()
        }
    }

    private fun login() {
        val amka = findViewById<EditText>(R.id.editTextAmka).text.toString()

        if (amka.isEmpty()){
            Toast.makeText(applicationContext, "AMKA is required", Toast.LENGTH_LONG).show()
            // loader.visibility = View.GONE
            return
        }

        if(amka.length != 11) {
            Toast.makeText(applicationContext, "Give a valid AMKA", Toast.LENGTH_LONG).show()
            // loader.visibility = View.GONE
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
                    editor.apply()
                    if (jwt != null && patientId != null) getPatient(jwt, patientId)
                } else if (response.code() == 401) {
                    try {
                        // loader.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<BasicResponse>, t: Throwable) {}
        })
    }

    private fun getPatient(authToken: String = "", patientId: String = "") {
        val call: Call<GetPatientResponse>? =
            RetrofitClient().getInstance()?.getApi()?.getPatient(patientId, authToken)

        call!!.enqueue(object : Callback<GetPatientResponse> {
            override fun onResponse(
                call: Call<GetPatientResponse>,
                response: Response<GetPatientResponse>
            ) {
                if (response.code() == 200) {
                    // loader.visibility = View.GONE

                    // saving in shared preferences
                    val doctorFullNameString =
                        response.body()?.data?.familyDoctor?.name + " " + response.body()?.data?.familyDoctor?.lastName

                    val sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                    val editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putString(getString(R.string.patient_name), response.body()?.data?.name)
                    editor.putString(getString(R.string.patient_last_name), response.body()?.data?.lastName)
                    editor.putString(getString(R.string.patient_amka), response.body()?.data?.amka)
                    editor.putString(getString(R.string.patient_blood_type), response.body()?.data?.bloodType)
                    editor.putString(getString(R.string.patient_doctor), doctorFullNameString)
                    editor.apply()

                    successfulLogin()
                } else if (response.code() == 400) {
                    try {
                        // loader.visibility = View.GONE
                        val jsonObject = JSONObject(response.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailure(call: Call<GetPatientResponse>, t: Throwable) {}
        })

    }

    private fun successfulLogin() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

}