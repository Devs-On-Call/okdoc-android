package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.DiagnosesAdapter
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetDiagnoses
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Diagnosis
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.diagnoses_list_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type

class DiagnosesListFragment : Fragment(R.layout.diagnoses_list_fragment), DiagnosesAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: DiagnosesAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.diagnoses_list_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedDiagnoses = sharedPreferences?.getString(getString(R.string.serialized_diagnoses), null)
        val diagnoses: List<Diagnosis>

        if (serializedDiagnoses != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Diagnosis?>?>() {}.type
            diagnoses = gson.fromJson(serializedDiagnoses, type)
            setAdapter(diagnoses)
        } else {
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            val patientId = sharedPreferences?.getString(getString(R.string.patient_id), "")
            if(authToken != "" && patientId != "" && authToken != null && patientId != null)
                if(ApiUtils().isOnline(this.requireContext()))
                    getDiagnoses(authToken, patientId)
                else
                    Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onItemClick(diagnosis: Diagnosis, view: View) {
        saveDiagnosisIdClickedInPrefs(diagnosis._id)
        view.findNavController().navigate(R.id.navigation_diagnosis)
    }

    private fun getDiagnoses(authToken: String = "", patientId: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetDiagnoses = ApiGetDiagnoses()
        apiGetDiagnoses.setOnDataListener(object : ApiGetDiagnoses.DataInterface {
            override fun responseData(getDiagnosesResponse: Response<DataListResponse<Diagnosis>>) {
                if (getDiagnosesResponse.code() == 200) {
                    if (getDiagnosesResponse.body()?.data != null) {
                        val diagnoses = getDiagnosesResponse.body()?.data!!
                        saveDiagnosesInPrefs(diagnoses)
                        setAdapter(diagnoses)
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

            override fun failureData(t: Throwable) {
                mainMenuActivity?.loadingOverlay?.dismiss()
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
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

    private fun saveDiagnosisIdClickedInPrefs(diagnosisIdClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.diagnosis_id_clicked), diagnosisIdClicked)
        editor?.apply()
    }

    private fun setAdapter(diagnoses: List<Diagnosis>){
        adapter = DiagnosesAdapter(diagnoses, this)
        rvDiagnoses.adapter = adapter
        rvDiagnoses.layoutManager = LinearLayoutManager(this.context)
    }
}