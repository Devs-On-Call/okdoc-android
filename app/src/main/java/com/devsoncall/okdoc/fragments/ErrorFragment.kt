package com.devsoncall.okdoc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.utils.animFadeIn
import com.devsoncall.okdoc.utils.animKeyIn
import kotlinx.android.synthetic.main.confirmed_fragment.*
import kotlinx.android.synthetic.main.error_fragment.*
import kotlinx.android.synthetic.main.error_fragment.buttonBack

class ErrorFragment : Fragment(R.layout.error_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.error_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animKeyIn(viewBackground, it) }
        activity?.applicationContext?.let { animFadeIn(viewErrorBorders, it) }
        activity?.applicationContext?.let { animFadeIn(textViewErrorMessage, it) }
        activity?.applicationContext?.let { animFadeIn(imageViewError, it) }

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }

        buttonErrorOk.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }
}