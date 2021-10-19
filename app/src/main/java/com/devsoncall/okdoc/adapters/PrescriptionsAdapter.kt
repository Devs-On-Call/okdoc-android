package com.devsoncall.okdoc.adapters


import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.Prescription
import kotlinx.android.synthetic.main.item_prescription.view.*

class PrescriptionsAdapter(
    private val prescriptions: List<Prescription>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PrescriptionsAdapter.PrescriptionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prescription, parent, false)

        return PrescriptionsViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PrescriptionsViewHolder, position: Int) {
        holder.itemView.apply {
//            we need to parse this date and get only YY-MM-DD
//            val date: Date = Date.from(Instant.parse(prescriptions[position].date))
            tvDate.text = prescriptions[position].date

            val doctorName = prescriptions[position].doctor.name
            val doctorLastName = prescriptions[position].doctor.lastName
            tvDoctorDetails.text = "Dr. $doctorName $doctorLastName"
        }
    }

    override fun getItemCount(): Int {
        return prescriptions.size
    }

    inner class PrescriptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    listener.onItemClick(prescriptions[position], v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(prescription: Prescription, view: View)
    }

}