package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.adapters.PrescriptionsAdapter
import com.devsoncall.okdoc.adapters.PrescriptionsElements

class PrescriptionListFragment : Fragment(R.layout.prescriptions_list_fragment), PrescriptionsAdapter.OnItemClickListener {

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            return inflater.inflate(R.layout.prescriptions_list_fragment, container, false)
        }

        @SuppressLint("SetTextI18n")
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

            val prescriptionsList = mutableListOf(
                PrescriptionsElements("25/09/2021", "Dr. Stella Sotiriou - Pathologist"),
                PrescriptionsElements("05/03/2021", "Dr. Michael Dragoumis - Dermatologist"),
                PrescriptionsElements("12/12/2020", "Dr. Giannis Papadoloulos - Obstetrics and Gynaecology"),
                PrescriptionsElements("22/08/2020", "Dr. Jenny Botsi - Ophthalmology"),
                PrescriptionsElements("11/12/2019", "Dr. Stella Sotiriou - Pathologist"),
                PrescriptionsElements("17/09/2019", "Dr. Stella Sotiriou - Pathologist")
            )

            val adapter = PrescriptionsAdapter(prescriptionsList, this)
            val rvPrescriptions = view.findViewById<RecyclerView>(R.id.rvPrescriptions)
            rvPrescriptions.adapter = adapter
            rvPrescriptions.layoutManager = LinearLayoutManager(this.context)


            val buttonBack = view.findViewById<Button>(R.id.btBack)

            buttonBack.setOnClickListener { view ->
                view.findNavController().navigate(R.id.navigation_home)
            }

//            val itemLayout = view.findViewById<LinearLayout>(R.id.item_layout)
//
//            itemLayout.setOnClickListener{
//                //Toast.makeText(this.context, "It clicked!! ", Toast.LENGTH_LONG).show()
//                view -> view.findNavController().navigate(R.id.navigation_prescription)
//            }

        }

        override fun onItemClick(position: Int, view: View) {
            view.findNavController().navigate(R.id.navigation_prescription)
            Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()

//
        }


}