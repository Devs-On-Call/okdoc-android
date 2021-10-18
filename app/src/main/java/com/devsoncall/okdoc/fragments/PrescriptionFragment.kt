package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
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

    // UI Elements
    private lateinit var prescriptionTitle: TextView
    private lateinit var doctorName: TextView
    private lateinit var doctorProfession: TextView
    private lateinit var date: Button

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
        val prescriptionClicked = sharedPreferences?.getInt(getString(R.string.prescription_clicked), -1)
        val prescriptions: List<Prescription>

        if(serializedPrescriptions != null && prescriptionClicked != -1 && prescriptionClicked != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Prescription?>?>() {}.type
            prescriptions = gson.fromJson(serializedPrescriptions, type)
            val selectedPrescription = prescriptions.elementAt(prescriptionClicked)
            val medications = mutableListOf(selectedPrescription)
            setUIElements(selectedPrescription)
            setAdapter(medications)
        } else if (prescriptionClicked != -1 && prescriptionClicked != null){
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
            if(authToken != "" && patientId != "" && authToken != null && patientId != null)
                getPrescriptions(authToken, patientId, prescriptionClicked)
        }

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_prescription_list)
        }

        buttonDiagnosis.setOnClickListener {
            // TODO
            // change to diagnosis fragment
//            val add = Prescription("id","date","diagnosis","dosage","drug","duration", null)
//            medicationList.add(add)
//            adapter.notifyItemInserted(medicationList.size - 1)
        }
    }

    private fun getPrescriptions(authToken: String = "", patientId: String = "", prescriptionClicked: Int) {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetPrescriptions = ApiGetPrescriptions()
        apiGetPrescriptions.setOnDataListener(object : ApiGetPrescriptions.DataInterface {
            override fun responseData(getPrescriptionsResponse: Response<DataListResponse<Prescription>>) {
                if (getPrescriptionsResponse.code() == 200) {
                    if (getPrescriptionsResponse.body()?.data != null) {
                        val prescriptions = getPrescriptionsResponse.body()?.data!!
                        savePrescriptionsInPrefs(prescriptions)

                        val selectedPrescription = prescriptions.elementAt(prescriptionClicked)
                        val medications = mutableListOf(prescriptions.elementAt(prescriptionClicked))
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

    @SuppressLint("SetTextI18n")
    private fun setUIElements(prescription: Prescription) {
        buttonPastDiagnoses.text = prescription.date
        textViewTitle.text = prescription.diagnosis.diagnosis
        textViewDoctor.text = "${prescription.doctor?.name} ${prescription.doctor?.lastName}"
        fullNameDescription.text = prescription.doctor?.profession?.name
    }

    private fun setAdapter(medication: List<Prescription>){
        adapter = MedicationAdapter(medication)
        recyclerViewMedicationsList.adapter = adapter
    }
}