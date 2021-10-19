package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.MedicationAdapter
import com.devsoncall.okdoc.api.calls.ApiGetPrescriptions
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Prescription
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.prescription_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type


class PrescriptionFragment : Fragment(R.layout.prescription_fragment) {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: MedicationAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.prescription_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewMedicationsList.layoutManager = LinearLayoutManager(this.context)
        val serializedPrescriptions = sharedPreferences?.getString(getString(R.string.serialized_prescriptions), null)
        val prescriptionIdClicked = sharedPreferences?.getString(getString(R.string.prescription_id_clicked), "")
        val prescriptions: List<Prescription>
        var selectedPrescription: Prescription? = null

        if(serializedPrescriptions != null && prescriptionIdClicked != "" && prescriptionIdClicked != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Prescription?>?>() {}.type
            prescriptions = gson.fromJson(serializedPrescriptions, type)
            selectedPrescription = prescriptions.firstOrNull { it._id == prescriptionIdClicked }!!
            val medications = mutableListOf(selectedPrescription)
            setUIElements(selectedPrescription)
            setAdapter(medications)
        } else if (prescriptionIdClicked != "" && prescriptionIdClicked != null){
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
            if(authToken != "" && patientId != "" && authToken != null && patientId != null)
                getPrescriptions(authToken, patientId, prescriptionIdClicked)
        }

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_prescription_list)
        }

        buttonDiagnosis.setOnClickListener {
            val selectedDiagnosisId = selectedPrescription?.diagnosis?._id
            if (selectedDiagnosisId != null)
                saveDiagnosisIdClickedInPrefs(selectedDiagnosisId)

            view.findNavController().navigate(R.id.navigation_diagnosis)
        }
    }

    private fun getPrescriptions(authToken: String = "", patientId: String = "", prescriptionIdClicked: String) {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetPrescriptions = ApiGetPrescriptions()
        apiGetPrescriptions.setOnDataListener(object : ApiGetPrescriptions.DataInterface {
            override fun responseData(getPrescriptionsResponse: Response<DataListResponse<Prescription>>) {
                if (getPrescriptionsResponse.code() == 200) {
                    if (getPrescriptionsResponse.body()?.data != null) {
                        val prescriptions = getPrescriptionsResponse.body()?.data!!
                        savePrescriptionsInPrefs(prescriptions)

                        val selectedPrescription = prescriptions.firstOrNull { it._id == prescriptionIdClicked }!!
                        val medications = mutableListOf(selectedPrescription)
                        setUIElements(selectedPrescription)
                        setAdapter(medications)
                    }
                } else if (getPrescriptionsResponse.code() == 400) {
                    try {
                        val jsonObject = JSONObject(getPrescriptionsResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                mainMenuActivity?.loadingOverlay?.dismiss()
            }
        })
        apiGetPrescriptions.getPrescriptions(authToken, patientId)
    }

    private fun savePrescriptionsInPrefs(prescriptions: List<Prescription>) {
        val gson = Gson()
        val serializedPrescriptions = gson.toJson(prescriptions)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.serialized_prescriptions), serializedPrescriptions)
        editor?.apply()
    }

    private fun saveDiagnosisIdClickedInPrefs(diagnosisIdClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.diagnosis_id_clicked), diagnosisIdClicked)
        editor?.apply()
    }

    @SuppressLint("SetTextI18n")
    private fun setUIElements(prescription: Prescription) {
        buttonPastDiagnoses.text = prescription.date
        textViewTitle.text = prescription.diagnosis.diagnosis
        textViewDoctor.text = "${prescription.doctor.name} ${prescription.doctor.lastName}"
        fullNameDescription.text = prescription.doctor.profession.name
    }

    private fun setAdapter(medication: MutableList<Prescription>){
        adapter = MedicationAdapter(medication)
        recyclerViewMedicationsList.adapter = adapter
    }
}