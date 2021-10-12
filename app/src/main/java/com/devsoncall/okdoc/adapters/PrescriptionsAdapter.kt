package com.devsoncall.okdoc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.item_prescription.view.*

class PrescriptionsAdapter(
    private val prescriptions: List<PrescriptionsElements>
) : RecyclerView.Adapter<PrescriptionsAdapter.PrescriptionsViewHolder>() {

    inner class PrescriptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PrescriptionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_prescription, parent, false)
        return PrescriptionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PrescriptionsViewHolder, position: Int) {
        holder.itemView.apply {
            tvDate.text = prescriptions[position].date
            tvDoctorDetails.text = prescriptions[position].doctor
        }

    }

    override fun getItemCount(): Int {
        return prescriptions.size
    }


}