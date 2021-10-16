package com.devsoncall.okdoc.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devsoncall.okdoc.R
import com.devsoncall.okdoc.models.Profession
import kotlinx.android.synthetic.main.item_profession.view.*

class ProfessionsAdapter(
    private val professions: List<Profession>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ProfessionsAdapter.ProfessionsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfessionsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_profession, parent, false)

        return ProfessionsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProfessionsViewHolder, position: Int) {
        holder.itemView.apply {
            tvProfession.text = professions[position].name
        }
    }

    override fun getItemCount(): Int {
        return professions.size
    }


    inner class ProfessionsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener{
        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = bindingAdapterPosition
            if (position != RecyclerView.NO_POSITION) {
                if (v != null) {
                    listener.onItemClick(professions[position], v)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(profession: Profession, view: View)
    }

}