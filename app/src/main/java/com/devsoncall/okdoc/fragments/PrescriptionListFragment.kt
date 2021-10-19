package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.PrescriptionsAdapter
import com.devsoncall.okdoc.api.calls.ApiGetPrescriptions
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Prescription
import com.google.gson.reflect.TypeToken

import com.google.gson.Gson
import kotlinx.android.synthetic.main.prescriptions_list_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type

class PrescriptionListFragment : Fragment(R.layout.prescriptions_list_fragment), PrescriptionsAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: PrescriptionsAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.prescriptions_list_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedPrescriptions = sharedPreferences?.getString(getString(R.string.serialized_prescriptions), null)
        val prescriptions: List<Prescription>

        if (serializedPrescriptions != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Prescription?>?>() {}.type
            prescriptions = gson.fromJson(serializedPrescriptions, type)
            setAdapter(prescriptions)
        } else {
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
            if(authToken != "" && patientId != "" && authToken != null && patientId != null)
                getPrescriptions(authToken, patientId)
        }

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onItemClick(prescription: Prescription, view: View) {
        savePrescriptionIdClickedInPrefs(prescription._id)
        view.findNavController().navigate(R.id.navigation_prescription)
    }

    private fun getPrescriptions(authToken: String = "", patientId: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetPrescriptions = ApiGetPrescriptions()
        apiGetPrescriptions.setOnDataListener(object : ApiGetPrescriptions.DataInterface {
            override fun responseData(getPrescriptionsResponse: Response<DataListResponse<Prescription>>) {
                if (getPrescriptionsResponse.code() == 200) {
                    if (getPrescriptionsResponse.body()?.data != null) {
                        val prescriptions = getPrescriptionsResponse.body()?.data!!
                        savePrescriptionsInPrefs(prescriptions)
                        setAdapter(prescriptions)
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

    private fun savePrescriptionIdClickedInPrefs(prescriptionIdClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.prescription_id_clicked), prescriptionIdClicked)
        editor?.apply()
    }
    
    private fun setAdapter(prescriptions: List<Prescription>){
        adapter = PrescriptionsAdapter(prescriptions, this)
        rvPrescriptions.adapter = adapter
        rvPrescriptions.layoutManager = LinearLayoutManager(this.context)
    }
}