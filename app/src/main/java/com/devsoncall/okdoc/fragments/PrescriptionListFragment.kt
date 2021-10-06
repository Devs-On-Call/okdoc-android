package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.adapters.PrescriptionsAdapter
import com.devsoncall.okdoc.adapters.PrescriptionsElements
import kotlinx.android.synthetic.main.item_prescription.*
import kotlinx.android.synthetic.main.prescriptions_list_fragment.*

class PrescriptionListFragment : Fragment(R.layout.prescriptions_list_fragment) {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            var prescriptionsList = mutableListOf(
                PrescriptionsElements("25/09/2021", "Dr. Stella Sotiriou - Pathologist"),
                PrescriptionsElements("05/03/2021", "Dr. Michael Dragoumis - Dermatologist"),
                PrescriptionsElements("12/12/2020", "Dr. Giannis Papadoloulos - Obstetrics and Gynaecology"),
                PrescriptionsElements("22/08/2020", "Dr. Jenny Botsi - Ophthalmology"),
                PrescriptionsElements("11/12/2019", "Dr. Stella Sotiriou - Pathologist"),
                PrescriptionsElements("17/09/2019", "Dr. Stella Sotiriou - Pathologist")
            )

            val adapter = PrescriptionsAdapter(prescriptionsList)
//            rvPrescriptions.adapter = adapter
//            rvPrescriptions.layoutManager = LinearLayoutManager(context)

            item_layout.setOnClickListener{
                // when clicked it should lead to prescription_fragment
            }



            return inflater.inflate(R.layout.prescriptions_list_fragment, container, false)
        }

        @SuppressLint("SetTextI18n")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

        }

}