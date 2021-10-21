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
import com.devsoncall.okdoc.adapters.HospitalsAdapter
import com.devsoncall.okdoc.api.ApiUtils
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
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.hospital_list_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
        val professionId = sharedPreferences?.getString(getString(R.string.profession_id_clicked), "")
        if(authToken != "" && professionId != "" && authToken != null && professionId != null)
            if(ApiUtils().isOnline(this.requireContext()))
                getHospitals(authToken, professionId)
            else
                Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_professions)
        }
    }

    override fun onItemClick(hospital: Hospital, view: View) {
        view.findNavController().navigate(R.id.navigation_doctors)
        saveHospitalIdClickedInPrefs(hospital._id, hospital.name, hospital.address)

    }

    private fun getHospitals(authToken: String = "", professionId: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetHospitals = ApiGetHospitals()
        apiGetHospitals.setOnDataListener(object : ApiGetHospitals.DataInterface {
            override fun responseData(getHospitalsResponse: Response<DataListResponse<Hospital>>) {
                if (getHospitalsResponse.code() == 200) {
                    if (getHospitalsResponse.body()?.data != null) {
                        val hospitals = getHospitalsResponse.body()?.data!!
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
        apiGetHospitals.getHospitals(authToken, professionId)
    }

    private fun saveHospitalIdClickedInPrefs(
        hospitalIdClicked: String, hospitalNameClicked: String,
        hospitalLocationClicked: String?
    ) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.hospital_id_clicked), hospitalIdClicked)
        editor?.putString(getString(R.string.hospital_name), hospitalNameClicked)
        editor?.putString(getString(R.string.hospital_location), hospitalLocationClicked)
        editor?.apply()
    }

    private fun setAdapter(hospitals: List<Hospital>){
        adapter = HospitalsAdapter(hospitals, this)
        rvHospitals.adapter = adapter
        rvHospitals.layoutManager = LinearLayoutManager(this.context)
    }
}