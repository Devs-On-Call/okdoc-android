package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetDiagnoses
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Diagnosis
import com.devsoncall.okdoc.utils.animFadeIn
import com.devsoncall.okdoc.utils.formatDateString
import com.devsoncall.okdoc.utils.getDayOfWeek
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.diagnosis_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type


class DiagnosisFragment : Fragment(R.layout.diagnosis_fragment) {

    private var sharedPreferences: SharedPreferences? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.diagnosis_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animFadeIn(textViewDiagnosisTitle, it) }
        activity?.applicationContext?.let { animFadeIn(textViewDiagnosisDoctor, it) }
        activity?.applicationContext?.let { animFadeIn(textViewDiagnosisDoctorProfession, it) }
        activity?.applicationContext?.let { animFadeIn(textViewDiagnosisDetails, it) }
        activity?.applicationContext?.let { animFadeIn(scrollViewMedicationsList, it) }

        val serializedDiagnoses = sharedPreferences?.getString(getString(R.string.serialized_diagnoses), null)
        val diagnosisIdClicked = sharedPreferences?.getString(getString(R.string.diagnosis_id_clicked), "")
        val diagnoses: List<Diagnosis>
        var selectedDiagnosis: Diagnosis? = null

        if(serializedDiagnoses != null && diagnosisIdClicked != "" && diagnosisIdClicked != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Diagnosis?>?>() {}.type
            diagnoses = gson.fromJson(serializedDiagnoses, type)
            selectedDiagnosis = diagnoses.firstOrNull { it._id == diagnosisIdClicked }!!
            setUIElements(selectedDiagnosis)
        } else if (diagnosisIdClicked != "" && diagnosisIdClicked != null){
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
            if(authToken != "" && patientId != "" && authToken != null && patientId != null)
                if(ApiUtils().isOnline(this.requireContext()))
                    getDiagnoses(authToken, patientId, diagnosisIdClicked)
                else
                    Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()
        }

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_diagnoses_list)
        }

        buttonPrescription.setOnClickListener {
            val selectedPrescriptionId = selectedDiagnosis?.prescription
            if (selectedPrescriptionId != null)
                savePrescriptionIdClickedInPrefs(selectedPrescriptionId)

            view.findNavController().navigate(R.id.navigation_prescription)
        }

        imageButtonShareDiagnosis.setOnClickListener {
            val bundle = bundleOf("type" to getString(R.string.diagnosis))
            view.findNavController().navigate(R.id.navigation_mail, bundle)
        }
    }

    private fun getDiagnoses(authToken: String = "", patientId: String = "", diagnosisIdClicked: String) {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetDiagnoses = ApiGetDiagnoses()
        apiGetDiagnoses.setOnDataListener(object : ApiGetDiagnoses.DataInterface {
            override fun responseData(getDiagnosesResponse: Response<DataListResponse<Diagnosis>>) {
                if (getDiagnosesResponse.code() == 200) {
                    if (getDiagnosesResponse.body()?.data != null) {
                        val diagnoses = getDiagnosesResponse.body()?.data!!
                        println(diagnoses)
                        saveDiagnosesInPrefs(diagnoses)

                        val selectedPrescription = diagnoses.firstOrNull { it._id == diagnosisIdClicked }!!
                        setUIElements(selectedPrescription)
                    }
                } else if (getDiagnosesResponse.code() == 400) {
                    try {
                        val jsonObject = JSONObject(getDiagnosesResponse.errorBody()?.string())
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
        apiGetDiagnoses.getDiagnoses(authToken, patientId)
    }

    private fun saveDiagnosesInPrefs(diagnoses: List<Diagnosis>) {
        val gson = Gson()
        val serializedDiagnoses = gson.toJson(diagnoses)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.serialized_diagnoses), serializedDiagnoses)
        editor?.apply()
    }

    private fun savePrescriptionIdClickedInPrefs(prescriptionIdClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.prescription_id_clicked), prescriptionIdClicked)
        editor?.apply()
    }

    @SuppressLint("SetTextI18n")
    private fun setUIElements(diagnosis: Diagnosis) {
        val date = diagnosis.date.take(10)
        val dayOfWeek = getDayOfWeek(date).toString()
        buttonDiagnosisDate.text = formatDateString(date, dayOfWeek).replace(", ", ",\n")
        diagnosisDetails.text = diagnosis.details.replace("\\n", System.getProperty("line.separator"))
        textViewDiagnosisDoctor.text = "Dr. ${diagnosis.doctor.name} ${diagnosis.doctor.lastName}"
        textViewDiagnosisDoctorProfession.text = diagnosis.doctor.profession.name
    }
}