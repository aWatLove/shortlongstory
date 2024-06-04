package com.example.lab8.ui.charsheet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.lab8.R

class CharsheetAdapter : ListAdapter<Charsheet, CharsheetAdapter.CharsheetViewHolder>(CharsheetDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharsheetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_creature, parent, false)
        return CharsheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharsheetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CharsheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val creatureName: TextView = itemView.findViewById(R.id.charLevel)
        private val creatureSize: TextView = itemView.findViewById(R.id.charName)
        private val creatureKind: TextView = itemView.findViewById(R.id.charClass)
        private val creatureDangerIndicator: TextView = itemView.findViewById(R.id.charArchetype)
        private val creatureDescription: TextView = itemView.findViewById(R.id.charStrength)
        private val creatureMaker: TextView = itemView.findViewById(R.id.creatureMaker)

        fun bind(creature: Charsheet) {
            creatureName.text = creature.creatureName
            creatureSize.text = creature.creatureSize
            creatureKind.text = creature.creatureKind
            creatureDangerIndicator.text = creature.creatureDangerIndicator.toString()
            creatureDescription.text = creature.creatureDescription
            creatureMaker.text = creature.creatureMaker
        }
    }

    class CharsheetDiffCallback : DiffUtil.ItemCallback<Charsheet>() {
        override fun areItemsTheSame(oldItem: Charsheet, newItem: Charsheet): Boolean {
            return oldItem.name == newItem.name
        }

        override fun areContentsTheSame(oldItem: Charsheet, newItem: Charsheet): Boolean {
            return oldItem.equals(newItem)
        }
    }
}