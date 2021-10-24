package com.devsoncall.okdoc.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.PatientAppointment
import com.devsoncall.okdoc.utils.formatDateString
import com.devsoncall.okdoc.utils.getDayOfWeek
import kotlinx.android.synthetic.main.item_appointment.view.*
import kotlinx.android.synthetic.main.item_prescription.view.tvDate
import kotlinx.android.synthetic.main.item_prescription.view.tvDoctorDetails

class AppointmentsAdapter(
    private val appointments: List<PatientAppointment>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)

        return AppointmentsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        holder.itemView.apply {

            val doctorName = appointments[position].doctor.name
            val doctorLastName = appointments[position].doctor.lastName
            val doctorSpecialty = appointments[position].doctor.profession.name
            val hospitalName = appointments[position].hospital.name
            val hospitalLocation = appointments[position].hospital.address
            val date = appointments[position].date.take(10)
            val dayOfWeek = getDayOfWeek(date).toString()
            val time = appointments[position].date.subSequence(11, 16)

            textViewDoctorName.text = "Dr. $doctorName $doctorLastName"
            textViewDoctorSpecialty.text = doctorSpecialty
            textViewHospitalName.text = "@ $hospitalName"
            textViewHospitalLocation.text = hospitalLocation
            textViewDate.text = formatDateString(date, dayOfWeek)
            textViewTime.text = time
        }
    }

    override fun getItemCount(): Int {
        return appointments.size
    }

    inner class AppointmentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    listener.onItemClick(position, v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }
}