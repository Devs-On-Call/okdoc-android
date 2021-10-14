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
import com.devsoncall.okdoc.adapters.HospitalsAdapter
import com.devsoncall.okdoc.adapters.HospitalsElements

class HospitalListFragment : Fragment(R.layout.hospital_list_fragment), HospitalsAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.hospital_list_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val hospitalsList = mutableListOf(
            HospitalsElements("Hospital A", "Athens"),
            HospitalsElements("Hospital B", "Athens"),
            HospitalsElements("Hospital C", "Athens"),
            HospitalsElements("Hospital D", "Thessaloniki"),
            HospitalsElements("Hospital E", "Patra"),
            HospitalsElements("Hospital F", "Ioannina")
        )

        val adapter = HospitalsAdapter(hospitalsList, this)
        val rvHospitals = view.findViewById<RecyclerView>(R.id.rvHospitals)
        rvHospitals.adapter = adapter
        rvHospitals.layoutManager = LinearLayoutManager(this.context)


        val buttonBack = view.findViewById<Button>(R.id.btBack)

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }


    }

    override fun onItemClick(position: Int, view: View) {
        //view.findNavController().navigate(R.id.navigation_prescription)
        Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()

    }


}