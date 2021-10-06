package com.devsoncall.okdoc.fragments


import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import kotlinx.android.synthetic.main.home_fragment.*


class HomeFragment : Fragment(R.layout.home_fragment) {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.home_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val textView: TextView = view.findViewById(R.id.text_home)
//        textView.text = "This is home Fragment"


        buttonPrescriptions.setOnClickListener {
            Toast.makeText(this.context, "Click listener ok", Toast.LENGTH_LONG).show()
//            val transaction = childFragmentManager.beginTransaction()
//            transaction.replace(R.id.frameLayoutFragment, prescriptionFragment)
//            transaction.commit()
//            parentFragmentManager.beginTransaction().apply {
//                replace(R.id.frameLayoutFragment, prescriptionFragment)
//                commit()
//            }
//            val prescriptionFragment = PrescriptionFragment()
//            val transaction: FragmentTransaction = requireFragmentManager().beginTransaction()
//
//            transaction.replace(R.id.frameLayoutFragment, prescriptionFragment)
//            transaction.addToBackStack(null)
//
//            transaction.commit()
        }
    }
}