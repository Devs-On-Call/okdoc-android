package com.devsoncall.okdoc.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.BookedHours
import kotlinx.android.synthetic.main.item_hour.view.*


class HoursAdapter(
    private val hours: List<BookedHours>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HoursAdapter.HoursViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoursViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hour, parent, false)

        return HoursViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HoursViewHolder, position: Int) {
        holder.itemView.apply {
//            we need to parse this date and get only YY-MM-DD
//            val date: Date = Date.from(Instant.parse(prescriptions[position].date))
            //tvHospital.text = appointments[position].hospital.name
            tvHours.text = hours[position].booked_hours[0]  //TODO TO TEST THE FIRST, CHANGE WITH HOURS i

//            val doctorName = appointments[position].doctor.name
//            val doctorLastName = appointments[position].doctor.lastName
//            tvDoctorDetails.text = "Dr. $doctorName $doctorLastName"
        }
    }

    override fun getItemCount(): Int {
        return hours.size
    }

    inner class HoursViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
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