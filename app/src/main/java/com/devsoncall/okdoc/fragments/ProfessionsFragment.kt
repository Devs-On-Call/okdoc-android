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
import com.devsoncall.okdoc.adapters.ProfessionsAdapter
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        return inflater.inflate(R.layout.professions_fragment, container, false)
    }

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
            val professionId = sharedPreferences?.getString(getString(R.string.profession_id_clicked), "")
            if(authToken != "" && professionId != "" && authToken != null && professionId != null)
                getProfessions(authToken, professionId)
        }

        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }

    override fun onItemClick(profession: Profession, view: View) {
       // view.findNavController().navigate(R.id.navigation_doctors) //navigation_doctors
        saveProfessionIdClickedInPrefs(profession._id)
    }

    private fun getProfessions(authToken: String = "", professionId: String = "") {
        val apiGetProfessions = ApiGetProfessions()
        apiGetProfessions.setOnDataListener(object : ApiGetProfessions.DataInterface {
            override fun responseData(getProfessionsResponse: Response<DataListResponse<Profession>>) {
                if (getProfessionsResponse.code() == 200) {
//                    loadingOverlay.dismiss()
                    if (getProfessionsResponse.body()?.data != null) {
                        val professions = getProfessionsResponse.body()?.data!!
                        saveProfessionsInPrefs(professions)
                        setAdapter(professions)
                    }
                } else if (getProfessionsResponse.code() == 400) {
                    try {
//                        loadingOverlay.dismiss()
                        val jsonObject = JSONObject(getProfessionsResponse.errorBody()?.string())
                        val errorMessage: String = jsonObject.getString("message")
                        println(errorMessage)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }
        })
        apiGetProfessions.getProfessions(authToken, professionId)
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