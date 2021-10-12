package com.devsoncall.okdoc.adapters


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.item_prescription.view.*

class PrescriptionsAdapter(
    private val prescriptions: List<PrescriptionsElements>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<PrescriptionsAdapter.PrescriptionsViewHolder>() {

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


    inner class PrescriptionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
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