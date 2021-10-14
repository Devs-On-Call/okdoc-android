package com.devsoncall.okdoc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.Prescription
import kotlinx.android.synthetic.main.medication_item.view.*

class MedicationAdapter(
    var medicationList: List<Prescription>
) : RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder>() {

    inner class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medication_item, parent, false)
        return MedicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        holder.itemView.apply {
            textViewMedicineTitle.text = medicationList[position].drug
            textViewDosage.text = medicationList[position].dosage
            textViewDuration.text = medicationList[position].duration
        }
    }

    override fun getItemCount(): Int {
        return medicationList.size
    }
}