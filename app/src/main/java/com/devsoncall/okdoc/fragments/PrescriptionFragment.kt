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
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.MedicationAdapter
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetPrescriptions
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Prescription
import com.devsoncall.okdoc.utils.animButtonPress
import com.devsoncall.okdoc.utils.animFadeIn
import com.devsoncall.okdoc.utils.formatDateString
import com.devsoncall.okdoc.utils.getDayOfWeek
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.prescription_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type


class PrescriptionFragment : Fragment(R.layout.prescription_fragment), MedicationAdapter.OnItemClickListener {

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

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animFadeIn(textViewTitle, it) }
        activity?.applicationContext?.let { animFadeIn(textViewDoctor, it) }
        activity?.applicationContext?.let { animFadeIn(fullNameDescription, it) }
        activity?.applicationContext?.let { animFadeIn(textViewAssignedMedication, it) }
        activity?.applicationContext?.let { animFadeIn(recyclerViewMedicationsList, it) }

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
                if(ApiUtils().isOnline(this.requireContext()))
                    getPrescriptions(authToken, patientId, prescriptionIdClicked)
                else
                    Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()
        }

        buttonBack.setOnClickListener { view ->
            activity?.applicationContext?.let { animButtonPress(buttonBack, it) }
            view.findNavController().navigate(R.id.navigation_prescription_list)
        }

        buttonDiagnosis.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(buttonDiagnosis, it) }
            val selectedDiagnosisId = selectedPrescription?.diagnosis?._id
            if (selectedDiagnosisId != null)
                saveDiagnosisIdClickedInPrefs(selectedDiagnosisId)

            view.findNavController().navigate(R.id.navigation_diagnosis)
        }

        imageButtonShare.setOnClickListener {
            activity?.applicationContext?.let { animButtonPress(imageButtonShare, it) }
            val bundle = bundleOf("type" to getString(R.string.prescription))
            view.findNavController().navigate(R.id.navigation_mail, bundle)
        }
    }

    override fun onItemClick(prescription: Prescription, view: View) {
        activity?.applicationContext?.let { animButtonPress(view, it) }
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
        val date = prescription.date.take(10)
        val dayOfWeek = getDayOfWeek(date).toString()
        buttonPastDiagnoses.text = formatDateString(date, dayOfWeek).replace(", ", ",\n")
        textViewTitle.text = prescription.diagnosis.name
        textViewDoctor.text = "Dr. ${prescription.doctor.name} ${prescription.doctor.lastName}"
        fullNameDescription.text = prescription.doctor.profession.name
    }

    private fun setAdapter(medication: MutableList<Prescription>){
        adapter = MedicationAdapter(medication, this)
        recyclerViewMedicationsList.adapter = adapter
    }
}