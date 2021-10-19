package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.api.calls.ApiGetDiagnoses
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Diagnosis
import com.devsoncall.okdoc.models.Prescription
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.diagnosis_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.prescription_fragment.*
import kotlinx.android.synthetic.main.prescription_fragment.buttonBack
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                getDiagnoses(authToken, patientId, diagnosisIdClicked)
        }

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_diagnoses_list)
        }

        buttonPrescription.setOnClickListener {
//            val selectedPrescriptionId = selectedDiagnosis?.prescription?._id
//            if (selectedPrescriptionId != null)
//                savePrescriptionIdClickedInPrefs(selectedPrescriptionId)
//
//            view.findNavController().navigate(R.id.navigation_diagnosis)
        }

        imageButtonShareDiagnosis.setOnClickListener {
            // TODO
            // share diagnosis
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
                }
                mainMenuActivity?.loadingOverlay?.dismiss()
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
        buttonDiagnosisDate.text = diagnosis.date
        diagnosisDetails.text = diagnosis.diagnosis
        textViewDiagnosisDoctor.text = "Dr. ${diagnosis.doctor.name} ${diagnosis.doctor.lastName}"
        textViewDiagnosisDoctorProfession.text = diagnosis.doctor.profession.name
    }
}