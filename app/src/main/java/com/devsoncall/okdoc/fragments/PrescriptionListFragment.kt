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

//    private val loadingOverlay = (activity as MainMenuActivity).loadingOverlay
    private var sharedPreferences: SharedPreferences? = null
    private var adapter: PrescriptionsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        return inflater.inflate(R.layout.prescriptions_list_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedPrescriptions = sharedPreferences?.getString(getString(R.string.serialized_prescriptions), null)
        val prescriptions: List<Prescription>
        rvPrescriptions.layoutManager = LinearLayoutManager(this.context)

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

    override fun onItemClick(position: Int, view: View) {
        savePrescriptionClickedInPrefs(position)
        view.findNavController().navigate(R.id.navigation_prescription)
//        Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()
    }

    private fun getPrescriptions(authToken: String = "", patientId: String = "") {
        val apiGetPrescriptions = ApiGetPrescriptions()
        apiGetPrescriptions.setOnDataListener(object : ApiGetPrescriptions.DataInterface {
            override fun responseData(getPrescriptionsResponse: Response<DataListResponse<Prescription>>) {
                if (getPrescriptionsResponse.code() == 200) {
//                    loadingOverlay.dismiss()
                    if (getPrescriptionsResponse.body()?.data != null) {
                        val prescriptions = getPrescriptionsResponse.body()?.data!!
                        savePrescriptionsInPrefs(prescriptions)
                        setAdapter(prescriptions)
                    }
                } else if (getPrescriptionsResponse.code() == 400) {
                    try {
//                        loadingOverlay.dismiss()
                        val jsonObject = JSONObject(getPrescriptionsResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
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

    private fun savePrescriptionClickedInPrefs(prescriptionClicked: Int) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putInt(getString(R.string.prescription_clicked), prescriptionClicked)
        editor?.apply()
    }
    
    private fun setAdapter(prescriptions: List<Prescription>){
        adapter = PrescriptionsAdapter(prescriptions, this)
        rvPrescriptions.adapter = adapter
    }
}