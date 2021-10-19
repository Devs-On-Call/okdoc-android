package com.devsoncall.okdoc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.calendar_fragment.*
//import com.applandeo.materialcalendarview.EventDay
//
//import com.applandeo.materialcalendarview.listeners.OnDayClickListener
import kotlinx.android.synthetic.main.calendar_fragment.buttonBack
import kotlinx.android.synthetic.main.prescription_fragment.*
import java.util.*
import java.util.ArrayList


class CalendarFragment : Fragment(R.layout.calendar_fragment) {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_doctors)
        }

//        calendarView.setOnDayClickListener { eventDay ->
//            // TODO
//            // navigate to select time fragment
//
//            val clickedDayCalendar: Calendar = eventDay.calendar
//            val dayOfMonth = clickedDayCalendar.get(Calendar.DAY_OF_MONTH)
//            val month = clickedDayCalendar.get(Calendar.MONTH) + 1  // month count starts from 0
//            val year = clickedDayCalendar.get(Calendar.YEAR)
//
//            Toast.makeText(this.context, "Day: $dayOfMonth Month: $month Year: $year", Toast.LENGTH_LONG).show()
////            view.findNavController().navigate(R.id.navigation_time)
//        }
    }
}