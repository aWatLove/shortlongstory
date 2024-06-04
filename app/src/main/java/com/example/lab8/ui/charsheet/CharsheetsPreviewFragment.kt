package com.example.lab8.ui.charsheet

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab8.R
import com.example.lab8.databinding.FragmentCharsheetsPreviewBinding
import com.google.firebase.database.DatabaseReference


class CharsheetsPreviewFragment : Fragment() {

    private var _binding: FragmentCharsheetsPreviewBinding? = null

    private val binding get() = _binding!!

    private lateinit var database: DatabaseReference
    private lateinit var creatureAdapter: CreatureAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBestiaryBinding.inflate(inflater, container, false)
        database = FirebaseDatabase.getInstance().getReference("Creature")

        binding.btnAddCreature.setOnClickListener {
            findNavController().navigate(R.id.nav_create_creature)
        }


        setupRecyclerView()
        loadCreatures()

        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        creatureAdapter = CreatureAdapter()
        binding.recyclerView.adapter = creatureAdapter
    }

    private fun loadCreatures() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val creatures = mutableListOf<Creature>()
                snapshot.children.forEach {
                    it.getValue(Creature::class.java)?.let { creature ->
                        creatures.add(creature)
                    }
                }
                creatureAdapter.submitList(creatures)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Failed to load creatures", Toast.LENGTH_SHORT).show()
            }
        })
    }
}