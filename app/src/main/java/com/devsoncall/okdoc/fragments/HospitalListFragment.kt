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
import com.devsoncall.okdoc.adapters.HospitalsAdapter
import com.devsoncall.okdoc.api.calls.ApiGetDoctors
import com.devsoncall.okdoc.api.calls.ApiGetHospitals
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Hospital
import kotlinx.android.synthetic.main.hospital_list_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class HospitalListFragment : Fragment(R.layout.hospital_list_fragment), HospitalsAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: HospitalsAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        return inflater.inflate(R.layout.hospital_list_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
        val professionId = sharedPreferences?.getString(getString(R.string.profession_id_clicked), "")
        if(authToken != "" && professionId != "" && authToken != null && professionId != null)
            getHospitals(authToken, professionId)

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().popBackStack()
        }
    }

    override fun onItemClick(hospital: Hospital, view: View) {
        view.findNavController().navigate(R.id.navigation_doctors)
        saveHospitalIdClickedInPrefs(hospital._id)
    }

    private fun getHospitals(authToken: String = "", professionId: String = "") {
        val apiGetHospitals = ApiGetHospitals()
        apiGetHospitals.setOnDataListener(object : ApiGetHospitals.DataInterface {
            override fun responseData(getHospitalsResponse: Response<DataListResponse<Hospital>>) {
                if (getHospitalsResponse.code() == 200) {
//                    loadingOverlay.dismiss()
                    if (getHospitalsResponse.body()?.data != null) {
                        val hospitals = getHospitalsResponse.body()?.data!!
                        setAdapter(hospitals)
                    }
                } else if (getHospitalsResponse.code() == 400) {
                    try {
//                        loadingOverlay.dismiss()
                        val jsonObject = JSONObject(getHospitalsResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        })
        apiGetHospitals.getHospitals(authToken, professionId)
    }

    private fun saveHospitalIdClickedInPrefs(hospitalIdClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.hospital_id_clicked), hospitalIdClicked)
        editor?.apply()
    }

    private fun setAdapter(hospitals: List<Hospital>){
        adapter = HospitalsAdapter(hospitals, this)
        rvHospitals.adapter = adapter
        rvHospitals.layoutManager = LinearLayoutManager(this.context)
    }
}