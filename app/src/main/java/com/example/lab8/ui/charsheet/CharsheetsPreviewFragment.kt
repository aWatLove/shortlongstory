package com.example.lab8.ui.charsheet

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab8.R
import com.example.lab8.databinding.FragmentCharsheetsPreviewBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CharsheetsPreviewFragment : Fragment() {

    private lateinit var binding: FragmentCharsheetsPreviewBinding
    private lateinit var database: DatabaseReference
    private lateinit var charsheetAdapter: CharsheetAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCharsheetsPreviewBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance().getReference("Charsheet")

        binding.createButton.setOnClickListener {
            findNavController().navigate(R.id.nav_create_charsheet)
        }


        setupRecyclerView()
        loadCharsheets()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        charsheetAdapter = CharsheetAdapter()
        binding.recyclerView.adapter = charsheetAdapter
    }

    private fun loadCharsheets() {
        // Извлекаем логин пользователя из SharedPreferences
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userLogin = sharedPref.getString("user_login", "Unknown")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val charsheets = mutableListOf<Charsheet>()
                snapshot.children.forEach {
                    it.getValue(Charsheet::class.java)?.let { charsheet ->
                        if (charsheet.author == userLogin) {
                            charsheets.add(charsheet)
                        }
                    }
                }
                charsheetAdapter.submitList(charsheets)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load creatures", Toast.LENGTH_SHORT).show()
            }
        })
    }
}