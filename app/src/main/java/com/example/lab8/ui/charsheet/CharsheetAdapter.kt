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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_charlist_prev, parent, false)
        return CharsheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharsheetViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class CharsheetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val characterName: TextView = itemView.findViewById(R.id.charactePrevName)
        private val characterLevel: TextView = itemView.findViewById(R.id.characterPrevLevel)
        private val characterClass: TextView = itemView.findViewById(R.id.characterPrevClass)
        private val characterRace: TextView = itemView.findViewById(R.id.characterPrevRace)

        fun bind(character: Charsheet) {
            characterName.text = character.name
            characterLevel.text = character.level.toString()
            characterClass.text = character.charClass
            characterRace.text = character.race
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