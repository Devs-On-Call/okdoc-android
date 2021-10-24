package com.devsoncall.okdoc.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.MailsAdapter
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetHospitals
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Diagnosis
import com.devsoncall.okdoc.models.Hospital
import com.devsoncall.okdoc.models.Prescription
import com.devsoncall.okdoc.utils.animFadeIn
import com.devsoncall.okdoc.utils.animKeyIn
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.mail_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type

class MailFragment : Fragment(R.layout.mail_fragment), MailsAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: MailsAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.mail_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animKeyIn(rvHospitalMails, it) }
        activity?.applicationContext?.let { animFadeIn(tvOr, it) }
        activity?.applicationContext?.let { animFadeIn(btCustomMail, it) }


        val serializedHospitals = sharedPreferences?.getString(getString(R.string.serialized_all_hospitals), null)
        val hospitals: List<Hospital>
        val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")

        if(serializedHospitals != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Hospital?>?>() {}.type
            hospitals = gson.fromJson(serializedHospitals, type)
            setAdapter(hospitals)
        } else if (authToken != "" && authToken != null) {
                if (ApiUtils().isOnline(this.requireContext()))
                    getHospitals(authToken)
                else
                    Toast.makeText(this.context,"Check your internet connection", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btMailBack).setOnClickListener {
            view.findNavController().popBackStack()
        }

        view.findViewById<Button>(R.id.btCustomMail).setOnClickListener {
            startShare("")
        }
    }

    override fun onItemClick(hospital: Hospital, view: View) {
        startShare(hospital.email)
    }

    private fun startShare(email: String) {
        val gson = Gson()
        val mailType = arguments?.getString("type")
        if(mailType != null && mailType != "0") {
            if(mailType == getString(R.string.prescription)) {
                val type: Type = object : TypeToken<List<Prescription?>?>() {}.type
                val serializedPrescriptions = sharedPreferences?.getString(getString(R.string.serialized_prescriptions), null)
                val prescriptionIdClicked = sharedPreferences?.getString(getString(R.string.prescription_id_clicked), "")
                val prescriptions: List<Prescription> = gson.fromJson(serializedPrescriptions, type)
                val selectedPrescription = prescriptions.firstOrNull { it._id == prescriptionIdClicked }!!
                preparePrescriptionMail(selectedPrescription, email)
            } else if(mailType == getString(R.string.diagnosis)) {
                val type: Type = object : TypeToken<List<Diagnosis?>?>() {}.type
                val serializedDiagnoses = sharedPreferences?.getString(getString(R.string.serialized_diagnoses), null)
                val diagnosisIdClicked = sharedPreferences?.getString(getString(R.string.diagnosis_id_clicked), "")
                val diagnoses: List<Diagnosis> = gson.fromJson(serializedDiagnoses, type)
                val selectedDiagnosis = diagnoses.firstOrNull { it._id == diagnosisIdClicked }!!
                prepareDiagnosisMail(selectedDiagnosis, email)
            }
        }
    }

    private fun preparePrescriptionMail(prescription: Prescription, email: String) {
        val patientName = sharedPreferences?.getString(getString(R.string.patient_name), "")
        val patientLastName = sharedPreferences?.getString(getString(R.string.patient_last_name), "")

        val fullName = "$patientName $patientLastName"
        val amka = sharedPreferences?.getString(getString(R.string.patient_amka), "")

        val subject = "$fullName ($amka) - Prescription"

        // prepare string template and populate it
        val body = getString(
            R.string.prescription_template,
            prescription.drug,
            prescription.date,
            prescription.diagnosis.name,
            prescription.dosage,
            prescription.duration,
            "Dr. ${prescription.doctor.name} ${prescription.doctor.lastName}"
        )
        sendEmail(email, subject, body)
    }

    private fun prepareDiagnosisMail(diagnosis: Diagnosis, email: String) {
        val patientName = sharedPreferences?.getString(getString(R.string.patient_name), "")
        val patientLastName = sharedPreferences?.getString(getString(R.string.patient_last_name), "")

        val fullName = "$patientName $patientLastName"
        val amka = sharedPreferences?.getString(getString(R.string.patient_amka), "")

        val subject = "$fullName ($amka) - Diagnosis"

        // prepare string template and populate it
        val body = getString(
            R.string.diagnosis_template,
            diagnosis.name,
            diagnosis.date,
            diagnosis.details.replace("\\n", System.getProperty("line.separator")),
            "Dr. ${diagnosis.doctor.name} ${diagnosis.doctor.lastName}"
        )
        sendEmail(email, subject, body)
    }

    private fun sendEmail(emailAddress: String, subject: String?, body: String?) {

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
//            emailIntent.type = "text/plain"

        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email"))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                activity?.applicationContext,
                "There is no email client installed.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getHospitals(authToken: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetHospitals = ApiGetHospitals()
        apiGetHospitals.setOnDataListener(object : ApiGetHospitals.DataInterface {
            override fun responseData(getHospitalsResponse: Response<DataListResponse<Hospital>>) {
                if (getHospitalsResponse.code() == 200) {
                    if (getHospitalsResponse.body()?.data != null) {
                        val hospitals = getHospitalsResponse.body()?.data!!
                        println(hospitals)
                        setAdapter(hospitals)
                    }
                } else if (getHospitalsResponse.code() == 400) {
                    try {
                        val jsonObject = JSONObject(getHospitalsResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    mainMenuActivity?.loadingOverlay?.dismiss()
                    view?.findNavController()?.navigate(R.id.navigation_error)
                }
                mainMenuActivity?.loadingOverlay?.dismiss()
            }

            override fun failureData(t: Throwable) {
                mainMenuActivity?.loadingOverlay?.dismiss()
                view?.findNavController()?.navigate(R.id.navigation_error)
            }
        })
        apiGetHospitals.getHospitals(authToken, null)
    }

    private fun setAdapter(hospitals: List<Hospital>){
        adapter = MailsAdapter(hospitals, this)
        rvHospitalMails.adapter = adapter
        rvHospitalMails.layoutManager = LinearLayoutManager(this.context)
    }
}