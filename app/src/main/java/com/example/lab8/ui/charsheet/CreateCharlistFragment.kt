package com.example.lab8.ui.charsheet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.lab8.R
import com.example.lab8.databinding.FragmentCreateCharlistBinding
import com.google.firebase.database.FirebaseDatabase
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome.author


class CreateCharlistFragment : Fragment() {

    private var _binding: FragmentCreateCharlistBinding? = null
    private val binding get() = _binding!!
    private val database = FirebaseDatabase.getInstance().getReference("Charsheet")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCharlistBinding.inflate(inflater, container, false)

        binding.createCreateButton.setOnClickListener {
            saveCreature()
        }

        return binding.root
    }

    private fun saveCreature() {
        val name = binding.name.text.toString()
        val level = binding.level.text.toString().toIntOrNull() ?: 1
        val charClass = binding.charclass.text.toString()
        val race = binding.race.text.toString()

        val strength = binding.strength.text.toString().toIntOrNull() ?: 10
        val dexterity = binding.dexterity.text.toString().toIntOrNull() ?: 10
        val constitution = binding.constitution.text.toString().toIntOrNull() ?: 10
        val intellegence = binding.intellegence.text.toString().toIntOrNull() ?: 10
        val wisdom = binding.wisdom.text.toString().toIntOrNull() ?: 10
        val charisma = binding.charisma.text.toString().toIntOrNull() ?: 10

        val armorClass = binding.charkd.text.toString().toIntOrNull()  ?: 10
        val initiative = binding.charinitiative.text.toString().toIntOrNull()  ?: 10
        val speed = binding.charspeed.text.toString().toIntOrNull()  ?: 10
        val hitpoints = binding.hitpoints.text.toString().toIntOrNull()  ?: 10

        val bioinfo = binding.charbioinfo.text.toString()


        // Извлекаем логин пользователя из SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val maker = sharedPref.getString("user_login", "Unknown")

        val charsheet = Charsheet(maker?:"Unknown", level, name, charClass, race,
            strength, dexterity, constitution, intellegence, wisdom,charisma,
            0, armorClass,initiative,speed,hitpoints,
            "",null, null, null, null, bioinfo)

        database.push().setValue(charsheet).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(requireContext(), "Charsheet saved", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.nav_charsheet)
            } else {
                Toast.makeText(requireContext(), "Failed to save charsheet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}