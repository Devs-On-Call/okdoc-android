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
import kotlinx.android.synthetic.main.confirmed_fragment.buttonBack


class ConfirmedFragment : Fragment(R.layout.confirmed_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.confirmed_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animKeyIn(viewConfirmedBackground, it) }
        activity?.applicationContext?.let { animFadeIn(viewConfirmedBorders, it) }
        activity?.applicationContext?.let { animFadeIn(textViewConfirmed, it) }
        activity?.applicationContext?.let { animFadeIn(imageViewSuccess, it) }

        buttonBack.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }

        buttonConfirmedOk.setOnClickListener { view ->
            view.findNavController().navigate(R.id.navigation_home)
        }
    }
}