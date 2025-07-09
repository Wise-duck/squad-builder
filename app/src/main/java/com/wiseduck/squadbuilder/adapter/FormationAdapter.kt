package com.wiseduck.squadbuilder.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.wiseduck.squadbuilder.R
import com.wiseduck.squadbuilder.data.Formation
import com.wiseduck.squadbuilder.databinding.FormationListItemBinding

class FormationAdapter(
    private val formations: List<Formation>,
    private val clickListener: (Formation) -> Unit,
    private val deleteListener: (Formation) -> Unit // 삭제 리스너 추가
) : RecyclerView.Adapter<FormationAdapter.FormationViewHolder>() {

    inner class FormationViewHolder(val binding: FormationListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(formation: Formation) {
            binding.formation = formation
            binding.root.setOnClickListener {
                clickListener(formation)
            }
            binding.executePendingBindings()
            binding.cancelButton.setOnClickListener {
                deleteListener(formation)
            }
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
