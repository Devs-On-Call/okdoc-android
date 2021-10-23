package com.devsoncall.okdoc.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.preference.PreferenceManager
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.activities.MainMenuActivity
import com.devsoncall.okdoc.adapters.MailsAdapter
import com.devsoncall.okdoc.models.Hospital
import com.devsoncall.okdoc.utils.animFadeIn
import com.devsoncall.okdoc.utils.animKeyIn
import kotlinx.android.synthetic.main.mail_fragment.*

class MailFragment : Fragment(R.layout.mail_fragment), MailsAdapter.OnItemClickListener {

    private var sharedPreferences: SharedPreferences? = null
    private var adapter: MailsAdapter? = null
    private var mainMenuActivity: MainMenuActivity? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.context)
        mainMenuActivity = activity as MainMenuActivity
        return inflater.inflate(R.layout.mail_fragment, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.applicationContext?.let { animKeyIn(rvHospitalMails, it) }
        activity?.applicationContext?.let { animFadeIn(tvOr, it) }
        activity?.applicationContext?.let { animFadeIn(btCustomMail, it) }

        val authToken = sharedPreferences?.getString(getString(R.string.auth_token), "")

        // TODO
        // api call get hospitals without professionId

        view.findViewById<Button>(R.id.btMailBack).setOnClickListener { view ->
            // we need to go back to either prescription or diagnosis
            // depending on which was the previous fragment
            fragmentManager?.popBackStackImmediate();
        }

        view.findViewById<Button>(R.id.btCustomMail).setOnClickListener {

            // TODO
            // get real diagnosis/prescription data
            val emailAddress = "hospital@mail.gr"
            val subject = "Diagnosis/Prescription name here"
            val body = "Diagnosis/Prescription details here"
            val chooserTitle = "Send email"

            sendEmail(emailAddress, subject, body, chooserTitle)
        }
    }

    override fun onItemClick(hospital: Hospital, view: View) {

        // TODO
        // get real diagnosis/prescription data
        val emailAddress = "hospital@mail.gr"
        val subject = "Diagnosis/Prescription name here"
        val body = "Diagnosis/Prescription details here"
        val chooserTitle = "Send email"

        sendEmail(emailAddress, subject, body, chooserTitle)
    }

    private fun sendEmail(emailAddress: String, subject: String?, body: String?, chooserTitle: String) {

        val emailIntent = Intent(Intent.ACTION_SENDTO)
        emailIntent.data = Uri.parse("mailto:")
//            emailIntent.type = "text/plain"

        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, body)

        try {
            startActivity(Intent.createChooser(emailIntent, chooserTitle))
        } catch (ex: ActivityNotFoundException) {
            Toast.makeText(
                activity?.applicationContext,
                "There is no email client installed.", Toast.LENGTH_SHORT
            ).show()
        }
    }
}