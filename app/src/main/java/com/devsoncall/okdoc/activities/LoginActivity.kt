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
import com.devsoncall.okdoc.api.calls.ApiGetPatient
import com.devsoncall.okdoc.api.calls.ApiLogin
import com.devsoncall.okdoc.models.BasicResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import com.devsoncall.okdoc.models.Patient
import com.mohamedabulgasem.loadingoverlay.LoadingAnimation
import com.mohamedabulgasem.loadingoverlay.LoadingOverlay
import org.json.JSONException
import org.json.JSONObject
import retrofit2.*


class LoginActivity : AppCompatActivity() {

    private val loadingOverlay: LoadingOverlay by lazy {
        LoadingOverlay.with(
            context = this@LoginActivity,
            animation = LoadingAnimation.FADING_PROGRESS
        )
    }

    private var sharedPreferences: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_login)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
        sharedPreferences?.edit()?.clear()?.apply()
        findViewById<Button>(R.id.getStartedButton).setOnClickListener{
            loadingOverlay.show()
            login()
        }
    }

    private fun login() {
        val amka = findViewById<EditText>(R.id.editTextAmka).text.toString()

        if (amka.isEmpty()){
            Toast.makeText(applicationContext, "AMKA is required", Toast.LENGTH_LONG).show()
            loadingOverlay.dismiss()
            return
        }

        if(amka.length != 11) {
            Toast.makeText(applicationContext, "Give a valid AMKA", Toast.LENGTH_LONG).show()
            loadingOverlay.dismiss()
            return
        }

        val apiLogin = ApiLogin()
        apiLogin.setOnDataListener(object : ApiLogin.DataInterface {
            override fun responseData(loginResponse: Response<BasicResponse>) {
                if (loginResponse.code() == 200) {
                    val jwt = loginResponse.headers().get("authorization")
                    val decodedToken = jwt?.let { JWT(it) }
                    val patientId = decodedToken?.getClaim("_id")?.asString()

                    if (jwt != null && patientId != null) {
                        saveAuthInfoInPrefs(jwt, patientId)
                        getPatient(jwt, patientId)
                    }
                } else if (loginResponse.code() == 401) {
                    try {
                        loadingOverlay.dismiss()
                        val jsonObject = JSONObject(loginResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_LONG).show()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        })
        apiLogin.login(amka)
    }

    private fun getPatient(authToken: String = "", patientId: String = "") {
        val apiGetPatient = ApiGetPatient()
        apiGetPatient.setOnDataListener(object : ApiGetPatient.DataInterface {
            override fun responseData(getPatientResponse: Response<GetPatientResponse>) {
                if (getPatientResponse.code() == 200) {
                    loadingOverlay.dismiss()
                    if (getPatientResponse.body()?.data != null) {
                        savePatientInPrefs(getPatientResponse.body()?.data!!)
                        successfulLogin()
                    }
                } else if (getPatientResponse.code() == 400) {
                    try {
                        loadingOverlay.dismiss()
                        val jsonObject = JSONObject(getPatientResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        })
        apiGetPatient.getPatient(authToken, patientId)
    }

    private fun successfulLogin() {
        val intent = Intent(this, MainMenuActivity::class.java)
        startActivity(intent)
    }

    private fun savePatientInPrefs(patient: Patient) {
        val doctorFullNameString = patient.familyDoctor.name + " " + patient.familyDoctor.lastName
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.patient_name), patient.name)
        editor?.putString(getString(R.string.patient_last_name), patient.lastName)
        editor?.putString(getString(R.string.patient_amka), patient.amka)
        editor?.putString(getString(R.string.patient_blood_type), patient.bloodType)
        editor?.putString(getString(R.string.patient_doctor), doctorFullNameString)
        editor?.apply()
    }

    private fun saveAuthInfoInPrefs(authToken: String = "", patientId: String = "") {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.auth_token), authToken)
        editor?.putString(getString(R.string.patient_id), patientId)
        editor?.apply()
    }
}