package com.devsoncall.okdoc.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.devsoncall.okdoc.R

class CreditsFragment : Fragment(R.layout.credits_fragment) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.credits_fragment, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_team_members).text = HtmlCompat.fromHtml(getString(R.string.team_members), HtmlCompat.FROM_HTML_MODE_LEGACY)

        val backButton: Button = view.findViewById(R.id.btBack)
        backButton.setOnClickListener {
            view.findNavController().navigate(R.id.navigation_settings)
        }
    }

}