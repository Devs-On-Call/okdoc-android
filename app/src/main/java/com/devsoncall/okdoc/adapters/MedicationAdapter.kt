package com.devsoncall.okdoc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.Prescription
import com.devsoncall.okdoc.utils.formatPrescriptionDateString
import com.devsoncall.okdoc.utils.getDayOfWeek
import kotlinx.android.synthetic.main.medication_item.view.*

class MedicationAdapter(
    var medicationList: MutableList<Prescription>
) : RecyclerView.Adapter<MedicationAdapter.MedicationViewHolder>() {

    inner class MedicationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medication_item, parent, false)
        return MedicationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MedicationViewHolder, position: Int) {
        holder.itemView.apply {

            val date = medicationList[position].date.take(10)
            val dayOfWeek = getDayOfWeek(date).toString()
            val duration = medicationList[position].duration.toInt()

            textViewMedicineTitle.text = medicationList[position].drug
            textViewDosage.text = medicationList[position].dosage
            textViewDuration.text = formatPrescriptionDateString(date, dayOfWeek, duration)
        }
    }

    override fun getItemCount(): Int {
        return medicationList.size
    }
}