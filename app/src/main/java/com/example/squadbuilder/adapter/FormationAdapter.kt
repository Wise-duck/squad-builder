package com.example.squadbuilder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.squadbuilder.R
import com.example.squadbuilder.data.Formation
import com.example.squadbuilder.databinding.FormationListItemBinding

class FormationAdapter(
    private val formations: List<Formation>,
    private val clickListener: (Formation) -> Unit
) : RecyclerView.Adapter<FormationAdapter.FormationViewHolder>() {

    inner class FormationViewHolder(val binding: FormationListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(formation: Formation) {
            binding.formation = formation
            binding.root.setOnClickListener {
                clickListener(formation)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FormationViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: FormationListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.formation_list_item, parent, false)
        return FormationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FormationViewHolder, position: Int) {
        holder.bind(formations[position])
    }

    override fun getItemCount(): Int {
        return formations.size
    }
}
