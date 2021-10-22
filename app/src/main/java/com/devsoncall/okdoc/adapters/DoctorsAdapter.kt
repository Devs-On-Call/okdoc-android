package com.devsoncall.okdoc.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.Doctor
import kotlinx.android.synthetic.main.item_doctor.view.*

class DoctorsAdapter(
    private val doctors: List<Doctor>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DoctorsAdapter.DoctorsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor, parent, false)

        return DoctorsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DoctorsViewHolder, position: Int) {
        holder.itemView.apply {
            val doctorName = doctors[position].name
            val doctorLastName = doctors[position].lastName
            tvDoctorFullName.text = "Dr. $doctorName $doctorLastName"
            tvDoctorProfession.text = doctors[position].profession?.name
        }
    }

    override fun getItemCount(): Int {
        return doctors.size
    }

    inner class DoctorsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    listener.onItemClick(doctors[position], v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(doctor: Doctor, view: View)
    }

}