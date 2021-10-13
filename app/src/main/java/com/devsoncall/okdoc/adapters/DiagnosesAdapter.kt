package com.devsoncall.okdoc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.item_diagnosis.view.*

class DiagnosesAdapter(
    private val diagnoses: List<DiagnosesElements>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<DiagnosesAdapter.DiagnosesViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_diagnosis, parent, false)

        return DiagnosesViewHolder(view)
    }

    override fun onBindViewHolder(holder: DiagnosesViewHolder, position: Int) {
        holder.itemView.apply {
            tvDate.text = diagnoses[position].date
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
                    listener.onItemClick(position, v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(position: Int, view: View)
    }

}