package com.devsoncall.okdoc.fragments


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : Fragment(R.layout.home_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val homeView: View = inflater.inflate(R.layout.home_fragment, container, false)
//
//        val pastDiagnosesButton: Button = homeView.findViewById(R.id.pastDiagnosesButton) as Button
//        val prescriptionsButton: Button = homeView.findViewById(R.id.prescriptionsButton) as Button
//        val scheduleAppointmentButton: Button = homeView.findViewById(R.id.scheduleAppointmentButton) as Button
//
//        pastDiagnosesButton.setOnClickListener {
//
//        }
//        prescriptionsButton.setOnClickListener{
//            val prescriptionsFragment = PrescriptionListFragment()
//            //val transaction : FragmentTrasaction
//        }
//        scheduleAppointmentButton.setOnClickListener {
//
//        }

       return homeView
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val textView: TextView = view.findViewById(R.id.text_home)
//        textView.text = "This is home Fragment"
    }
}