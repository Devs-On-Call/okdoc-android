package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.adapters.MedicationAdapter
import com.devsoncall.okdoc.models.Doctor
import com.devsoncall.okdoc.models.Prescription
import kotlinx.android.synthetic.main.prescription_fragment.*


class PrescriptionFragment : Fragment(R.layout.prescription_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.prescription_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var medicationList = mutableListOf(
            Prescription("id","date","diagnosis","dosage","drug","duration", Doctor()),
            Prescription("1","2","3","4","5","6", Doctor())
        )

        val adapter = MedicationAdapter(medicationList)
        recyclerViewMedicationsList.adapter = adapter
        recyclerViewMedicationsList.layoutManager = LinearLayoutManager(this.context)

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }

        buttonDiagnosis.setOnClickListener {
            // TODO
            // change to diagnosis fragment
            val add = Prescription("id","date","diagnosis","dosage","drug","duration", Doctor())
            medicationList.add(add)
            adapter.notifyItemInserted(medicationList.size - 1)
        }
    }
}