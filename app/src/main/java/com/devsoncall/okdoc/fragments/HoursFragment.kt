package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.HoursAdapter
import com.devsoncall.okdoc.api.calls.ApiGetHours
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
            getTimeSlots(hours)
            setAdapter(hours)
        }


        view.findViewById<Button>(R.id.btBack).setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }

//    override fun onItemClick(hours: BookedHours, view: View) {
//        saveHourIdClickedInPrefs(hours._id)   //TODO FIX
//        view.findNavController().navigate(R.id.navigation_hospital_list) //TODO FIX
//    }

    override fun onItemClick(position: Int, view: View) {
        Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()
    }

    private fun getTimeSlots(hours: List<BookedHours>){

        val slots = listOf(
            setOf("09:00", "09:30"),
            setOf("09:30", "10:00"),
            setOf("10:00", "10:30"),
            setOf("10:30", "11:00"),
            setOf("11:00", "11:30"),
            setOf("11:30", "12:00"),
            setOf("12:00", "12:30"),
            setOf("12:30", "13:00"),
            setOf("13:00", "13:30"),
            setOf("13:30", "14:00"),
            setOf("14:00", "14:30"),
            setOf("14:30", "15:00"),
            setOf("15:00", "15:30"),
            setOf("15:30", "16:00"),
            setOf("16:00", "16:30"),
            setOf("16:30", "17:00")
        )

            for (pos in hours.indices) {
                var booked = hours[pos].booked_hours

                for (startTime in booked)
                    for (position in slots.indices)
                        if (slots[position].contains(startTime))
                            println("TEST OUTPUT" + slots[position])

            }


//        for (i in slots.indices)
//            slots.contains(startTime)
//            if (startTime.equals(slots.containsKeys))
//       if(startTime.any(slots::equals))



    }

    private fun saveHoursInPrefs(selected_hour: String) {
        val editor: SharedPreferences.Editor? = sharedPreferences?.edit()
        editor?.putString(getString(R.string.selected_hour), selected_hour)
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