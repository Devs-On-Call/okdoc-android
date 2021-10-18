package com.devsoncall.okdoc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.diagnosis_fragment.*
import kotlinx.android.synthetic.main.home_fragment.*
import kotlinx.android.synthetic.main.home_fragment.buttonPrescriptions
import kotlinx.android.synthetic.main.prescription_fragment.*
import kotlinx.android.synthetic.main.prescription_fragment.buttonBack


class DiagnosisFragment : Fragment(R.layout.diagnosis_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.diagnosis_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_diagnoses_list)
        }

        buttonPrescription.setOnClickListener {
            // TODO
            // navigate to prescription fragment
        }

        imageButtonShareDiagnosis.setOnClickListener {
            // TODO
            // share diagnosis
        }
    }
}