package com.devsoncall.okdoc.adapters

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.Diagnosis
import com.devsoncall.okdoc.utils.formatDateString
import com.devsoncall.okdoc.utils.getDayOfWeek
import kotlinx.android.synthetic.main.item_diagnosis.view.*
import kotlinx.android.synthetic.main.item_diagnosis.view.tvDate
import kotlinx.android.synthetic.main.item_prescription.view.*

class DiagnosesAdapter(
    private val diagnoses: List<Diagnosis>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DiagnosesAdapter.DiagnosesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diagnosis, parent, false)

        return DiagnosesViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DiagnosesViewHolder, position: Int) {
        holder.itemView.apply {
            // we need to parse this date and get only YY-MM-DD
            // val date: Date = Date.from(Instant.parse(diagnoses[position].date))
            val date = diagnoses[position].date.take(10)
            val dayOfWeek = getDayOfWeek(date).toString()
            tvDate.text = formatDateString(date, dayOfWeek)
            tvDiagnosis.text = diagnoses[position].diagnosis
        }
    }

    override fun getItemCount(): Int {
        return diagnoses.size
    }

    inner class DiagnosesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    listener.onItemClick(diagnoses[position], v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(diagnosis: Diagnosis, view: View)
    }
}