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
import com.devsoncall.okdoc.adapters.ProfessionsAdapter
import com.devsoncall.okdoc.api.ApiUtils
import com.devsoncall.okdoc.api.calls.ApiGetProfessions
import com.devsoncall.okdoc.models.DataListResponse
import com.devsoncall.okdoc.models.Profession
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.professions_fragment.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.lang.reflect.Type

class ProfessionsFragment : Fragment(R.layout.professions_fragment), ProfessionsAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: ProfessionsAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.professions_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val serializedProfessions = sharedPreferences?.getString(getString(R.string.serialized_professions), null)
        val professions: List<Profession>

        if (serializedProfessions != null) {
            val gson = Gson()
            val type: Type = object : TypeToken<List<Profession?>?>() {}.type
            professions = gson.fromJson(serializedProfessions, type)
            setAdapter(professions)
        } else {
            val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")
            if(authToken != "" && authToken != null)
                if(ApiUtils().isOnline(this.requireContext()))
                    getProfessions(authToken)
                else
                    Toast.makeText(this.context, "Check your internet connection", Toast.LENGTH_SHORT).show()
        }

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onItemClick(profession: Profession, view: View) {
        saveProfessionIdClickedInPrefs(profession._id)
        view.findNavController().navigate(R.id.navigation_hospital_list)
    }

    private fun getProfessions(authToken: String = "") {
        mainMenuActivity?.loadingOverlay?.show()
        val apiGetProfessions = ApiGetProfessions()
        apiGetProfessions.setOnDataListener(object : ApiGetProfessions.DataInterface {
            override fun responseData(getProfessionsResponse: Response<DataListResponse<Profession>>) {
                if (getProfessionsResponse.code() == 200) {
                    if (getProfessionsResponse.body()?.data != null) {
                        val professions = getProfessionsResponse.body()?.data!!
                        saveProfessionsInPrefs(professions)
                        setAdapter(professions)
                    }
                } else if (getProfessionsResponse.code() == 400) {
                    try {
                        val jsonObject = JSONObject(getProfessionsResponse.errorBody()?.string())
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
        apiGetProfessions.getProfessions(authToken)
    }

    private fun saveProfessionsInPrefs(professions: List<Profession>) {
        val gson = Gson()
        val serializedProfessions = gson.toJson(professions)
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.serialized_professions), serializedProfessions)
        editor?.apply()
    }

    private fun saveProfessionIdClickedInPrefs(professionIdClicked: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.profession_id_clicked), professionIdClicked)
        editor?.apply()
    }

    private fun setAdapter(professions: List<Profession>){
        adapter = ProfessionsAdapter(professions, this)
        rvProfessions.adapter = adapter
        rvProfessions.layoutManager = LinearLayoutManager(this.context)
    }
}