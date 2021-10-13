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
import com.devsoncall.okdoc.adapters.DiagnosesAdapter
import com.devsoncall.okdoc.adapters.DiagnosesElements

class DiagnosesListFragment : Fragment(R.layout.diagnoses_list_fragment), DiagnosesAdapter.OnItemClickListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.diagnoses_list_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val diagnosesList = mutableListOf(
            DiagnosesElements("25/09/2021", "Streptococcus Pneumonia"),
            DiagnosesElements("05/03/2021", "Atopic Dermatitis"),
            DiagnosesElements("25/09/2021", "Streptococcus Pneumonia"),
            DiagnosesElements("12/12/2020", "Eczema"),
            DiagnosesElements("22/08/2020", "Eyes problem"),
            DiagnosesElements("11/12/2019", "Amygdalitis"),
            DiagnosesElements("17/09/2019", "Vronchitis")
        )

        val adapter = DiagnosesAdapter(diagnosesList, this)
        val rvDiagnoses = view.findViewById<RecyclerView>(R.id.rvDiagnoses)
        rvDiagnoses.adapter = adapter
        rvDiagnoses.layoutManager = LinearLayoutManager(this.context)


        val buttonBack = view.findViewById<Button>(R.id.btBack)

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }


    }

    override fun onItemClick(position: Int, view: View) {
        //view.findNavController().navigate(R.id.navigation_diagnosis)
        Toast.makeText(this.context, "Item $position clicked", Toast.LENGTH_SHORT).show()

    }


}