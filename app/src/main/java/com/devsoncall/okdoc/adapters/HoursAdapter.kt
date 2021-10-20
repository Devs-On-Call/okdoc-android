package com.devsoncall.okdoc.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import kotlinx.android.synthetic.main.item_hour.view.*


class HoursAdapter(
    private val hours: List<String>,
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
            tvHours.text = hours[position]
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
                    listener.onItemClick(hours[position], v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(hour: String, view: View)
    }
}