package com.devsoncall.okdoc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.error_fragment.*

class ErrorFragment : Fragment(R.layout.error_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.error_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }

        buttonErrorOk.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }
}