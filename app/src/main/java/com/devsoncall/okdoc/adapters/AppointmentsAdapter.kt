package com.devsoncall.okdoc.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.Appointment
import kotlinx.android.synthetic.main.item_appointment.view.*
import kotlinx.android.synthetic.main.item_prescription.view.*
import kotlinx.android.synthetic.main.item_prescription.view.tvDate
import kotlinx.android.synthetic.main.item_prescription.view.tvDoctorDetails

class AppointmentsAdapter(
    private val appointments: List<Appointment>,
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
//            we need to parse this date and get only YY-MM-DD
//            val date: Date = Date.from(Instant.parse(prescriptions[position].date))
            tvHospital.text = appointments[position].hospital.name
            tvDate.text = appointments[position].date

            val doctorName = appointments[position].doctor.name
            val doctorLastName = appointments[position].doctor.lastName
            tvDoctorDetails.text = "Dr. $doctorName $doctorLastName"
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