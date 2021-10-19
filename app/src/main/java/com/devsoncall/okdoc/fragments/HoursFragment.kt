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
import com.devsoncall.okdoc.adapters.HoursAdapter
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.BookedHours
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.hours_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type

class HoursFragment : Fragment(R.layout.hours_fragment), HoursAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: HoursAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.hours_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedHours = sharedPreferences?.getString(getString(R.string.serialized_hours), null)
        val hours: List<BookedHours>

        if (serializedHours != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<BookedHours?>?>() {}.type
            hours = gson.fromJson(serializedHours, type)
            setAdapter(hours)
        } else {
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            if(authToken != "" && authToken != null)
                getHours(authToken)
        }

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onItemClick(hours: BookedHours, view: View) {
        saveHourIdClickedInPrefs(hours._id)   //TODO FIX
        //view.findNavController().navigate(R.id.navigation_hospital_list) //TODO FIX
    }

    private fun getHours(authToken: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetHours = ApiGetHours()
        apiGetHours.setOnDataListener(object : ApiGetHours.DataInterface {
            override fun responseData(getHoursResponse: Response<DataListResponse<BookedHours>>) {
                if (getHoursResponse.code() == 200) {
                    if (getHoursResponse.body()?.data != null) {
                        val hours = getHoursResponse.body()?.data!!
                        saveHoursInPrefs(hours)
                        setAdapter(hours)
                    }
                } else if (getHoursResponse.code() == 400) {
                    try {
                        val jsonObject = JSONObject(getHoursResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
                mainMenuActivity?.loadingOverlay?.dismiss()
            }
        })
        apiGetHours.getHours(authToken)
    }

    private fun saveHoursInPrefs(hours: List<BookedHours>) {
        val gson = Gson()
        val serializedHours= gson.toJson(hours)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.serialized_hours), serializedHours)
        editor?.apply()
    }

    private fun saveHourIdClickedInPrefs(hourIdClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.hour_id_clicked), hourIdClicked)
        editor?.apply()
    }

    private fun setAdapter(hours: List<BookedHours>){
        adapter = HoursAdapter(hours, this)
        rvHours.adapter = adapter
        rvHours.layoutManager = LinearLayoutManager(this.context)
    }
}