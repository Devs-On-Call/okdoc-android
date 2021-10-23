package com.devsoncall.okdoc.activities

import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceManager
import com.auth0.android.jwt.JWT
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetPatient
import com.devsoncall.okdoc.api.calls.ApiLogin
import com.devsoncall.okdoc.models.BasicResponse
import com.devsoncall.okdoc.models.GetPatientResponse
import com.devsoncall.okdoc.models.Patient
import com.mohamedabulgasem.loadingoverlay.LoadingAnimation
import com.mohamedabulgasem.loadingoverlay.LoadingOverlay
import kotlinx.android.synthetic.main.activity_login.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.*
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent.setEventListener
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.devsoncall.okdoc.utils.*
import kotlinx.android.synthetic.main.credits_fragment.*
import java.util.*


class LoginActivity : AppCompatActivity() {

    private val loadingOverlay: LoadingOverlay by lazy {
        LoadingOverlay.with(
            context = this@LoginActivity,
            animation = LoadingAnimation.FADING_PROGRESS
        )
    }

    private var sharedPreferences: SharedPreferences? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
        val language = sharedPreferences?.getString(getString(R.string.language), null)
        if(language != null) setLocale(language)

        setContentView(R.layout.activity_login)

        clearPrefs()

        findViewById<Button>(R.id.buttonGetStarted).setOnClickListener{
            if(ApiUtils().isOnline(this@LoginActivity))
                login()
            else
                Toast.makeText(applicationContext, "Check your internet connection", Toast.LENGTH_LONG).show()
        }

        setEventListener(
            this,
            KeyboardVisibilityEventListener {
                // Soft Keyboard Active
                if (it) {
                    imageViewDoctors.visibility = View.INVISIBLE
                    animKeyIn(editTextAmka, this)
                    animKeyIn(buttonGetStarted, this)
                    animKeyIn(textViewWelcomeTo, this)
                    animKeyIn(textViewOkDoc, this)

                    // adjust constraints
                    val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_root)
                    val constraintSet = ConstraintSet()
                    constraintSet.clear(R.id.imageViewDoctors)
                    constraintSet.clone(constraintLayout)
                    constraintSet.connect(
                        R.id.textViewOkDoc,
                        ConstraintSet.BOTTOM,
                        R.id.editTextAmka,
                        ConstraintSet.TOP,
                        300
                    )
                    constraintSet.connect(
                        R.id.buttonGetStarted,
                        ConstraintSet.BOTTOM,
                        R.id.activity_root,
                        ConstraintSet.BOTTOM,
                        800
                    )
                    constraintSet.applyTo(constraintLayout)

                // Soft Keyboard Inactive
                } else {
                    animFadeIn(imageViewDoctors, this)
                    imageViewDoctors.visibility = View.VISIBLE
                    animKeyIn(editTextAmka, this)
                    animKeyIn(buttonGetStarted, this)
                    animKeyIn(textViewWelcomeTo, this)
                    animKeyIn(textViewOkDoc, this)

                    // adjust constraints
                    val constraintLayout = findViewById<ConstraintLayout>(R.id.activity_root)
                    val constraintSet = ConstraintSet()
                    constraintSet.clone(constraintLayout)
                    constraintSet.connect(
                        R.id.buttonGetStarted,
                        ConstraintSet.BOTTOM,
                        R.id.activity_root,
                        ConstraintSet.BOTTOM,
                        dpToPx(120, this)
                    )
                    constraintSet.connect(
                        R.id.imageViewDoctors,
                        ConstraintSet.BOTTOM,
                        R.id.editTextAmka,
                        ConstraintSet.TOP,
                        dpToPx(80, this)
                    )
                    constraintSet.connect(
                        R.id.imageViewDoctors,
                        ConstraintSet.START,
                        R.id.activity_root,
                        ConstraintSet.START,
                        0
                    )
                    constraintSet.connect(
                        R.id.imageViewDoctors,
                        ConstraintSet.END,
                        R.id.activity_root,
                        ConstraintSet.END,
                        0
                    )
                    constraintSet.connect(
                        R.id.textViewOkDoc,
                        ConstraintSet.BOTTOM,
                        R.id.imageViewDoctors,
                        ConstraintSet.TOP,
                        dpToPx(20, this)
                    )
                    constraintSet.applyTo(constraintLayout)
                }
            })
    }

    private fun clearPrefs(){
        val language = sharedPreferences?.getString(getString(R.string.language), null)
        sharedPreferences?.edit()?.clear()?.apply()
        if (language != null) {
            val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
            editor?.putString(getString(R.string.language), language)
            editor?.apply()
        }
    }

    private fun setLocale(languageCode: String) {
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

        loadingOverlay.show()
        val apiLogin = ApiLogin()
        apiLogin.setOnDataListener(object : ApiLogin.DataInterface {
            @RequiresApi(Build.VERSION_CODES.M)
            override fun responseData(loginResponse: Response<BasicResponse>) {
                if (loginResponse.code() == 200) {
                    val jwt = loginResponse.headers().get("authorization")
                    val decodedToken = jwt?.let { JWT(it) }
                    val patientId = decodedToken?.getClaim("_id")?.asString()

                    if (jwt != null && patientId != null) {
                        saveAuthInfoInPrefs(jwt, patientId)
                        if(ApiUtils().isOnline(this@LoginActivity))
                            getPatient(jwt, patientId)
                        else
                            Toast.makeText(applicationContext, "Check your internet connection", Toast.LENGTH_LONG).show()
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

            override fun failureData(t: Throwable) {
                loadingOverlay.dismiss()
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
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

            override fun failureData(t: Throwable) {
                loadingOverlay.dismiss()
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_LONG).show()
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