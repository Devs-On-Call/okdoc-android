package com.devsoncall.okdoc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.Hospital
import kotlinx.android.synthetic.main.item_hospital.view.*

class HospitalsAdapter(
    private val hospitals: List<Hospital>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<HospitalsAdapter.HospitalsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HospitalsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_hospital, parent, false)

        return HospitalsViewHolder(view)
    }

    override fun onBindViewHolder(holder: HospitalsViewHolder, position: Int) {
        holder.itemView.apply {
            tvHospitalName.text = hospitals[position].name
            tvHospitalLocation.text = hospitals[position].address
        }
    }

    override fun getItemCount(): Int {
        return hospitals.size
    }


    inner class HospitalsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    listener.onItemClick(hospitals[position], v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(hospital: Hospital, view: View)
    }

}