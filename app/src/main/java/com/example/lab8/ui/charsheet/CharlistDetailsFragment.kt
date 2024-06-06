package com.example.lab8.ui.charsheet

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.findNavController
import com.example.lab8.R
import com.example.lab8.databinding.FragmentCharlistDetailsBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso


class CharlistDetailsFragment : Fragment() {

    private var _binding: FragmentCharlistDetailsBinding? = null
    private val binding get() = _binding!!
    private val database = FirebaseDatabase.getInstance().getReference("Charsheet")
    val storage = FirebaseStorage.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharlistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let { bundle ->
            val uuid = bundle.getString("uuid").toString()
            if (uuid.isNotEmpty()) {
                fetchCharsheet(uuid)
                setupDeleteButton(uuid)
            }
        }

        binding.editButton.setOnClickListener {
            arguments?.let { bundle ->
                val bundleCharsheet = Bundle().apply {
                    putString("uuid",bundle.getString("uuid").toString())
                }
                findNavController().navigate(R.id.nav_edit_charsheet, bundleCharsheet)
            }
        }
    }

    private fun fetchCharsheet(uuid: String) {
        database.orderByChild("uuid").equalTo(uuid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        val charsheet = dataSnapshot.getValue(Charsheet::class.java)
                        if (charsheet != null) {
                            displayCharSheet(charsheet)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CharlistDetailsFragment", "Database error: ${error.message}")
            }
        })
    }

    private fun displayCharSheet(charsheet: Charsheet) {
        // Загрузка и отображение аватарки
        val storageRef = storage.reference.child("images/${charsheet.uuid}")
        storageRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.with(requireContext()).load(uri).into(binding.characterAvatar)
        }.addOnFailureListener { exception ->
            Log.e("CharlistDetailsFragment", "Failed to load image: ${exception.message}")
        }

        binding.characterName.text = charsheet.name
        binding.characterLevel.text = charsheet.level.toString() + " уровень"
        binding.characterClass.text = charsheet.charClass
        binding.characterRace.text = charsheet.race

        binding.textCharStrength.text = charsheet.strength.toString()
        binding.textModifierStrength.text = calculateModifier(charsheet.strength)


        binding.textCharDexterity.text = charsheet.dexterity.toString()
        binding.textModifierDexterity.text = calculateModifier(charsheet.dexterity)



        binding.textCharConstitution.text = charsheet.constitution.toString()
        binding.textModifierConstitution.text = calculateModifier(charsheet.constitution)

        binding.textCharIntellegence.text = charsheet.intelligence.toString()
        binding.textModifierIntellegence.text = calculateModifier(charsheet.intelligence)


        binding.textCharWisdom.text = charsheet.wisdom.toString()
        binding.textModifierWisdom.text = calculateModifier(charsheet.wisdom)


        binding.textCharCharisma.text = charsheet.charisma.toString()
        binding.textModifierCharisma.text = calculateModifier(charsheet.charisma)


        binding.initiative.text = charsheet.initiative.toString()
        binding.kd.text = charsheet.armorClass.toString()
        binding.speed.text = charsheet.speed.toString()
        binding.hits.text = charsheet.hits.toString()
        binding.bioinfo.text = charsheet.bioinfo

    }

    fun calculateModifier(attribute: Int): String {
        val modifier = if (attribute >= 10) {
            (attribute - 10) / 2
        } else {
            (attribute - 11) / 2
        }

        return if (modifier >= 0) "+$modifier" else "$modifier"
    }





    private fun setupDeleteButton(uuid: String) {
        binding.deleteButton.setOnClickListener {
            showDeleteConfirmationDialog(uuid)
            findNavController().navigate(R.id.nav_charsheet)
        }
    }

    private fun showDeleteConfirmationDialog(uuid: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Подтверждение удаления")
        builder.setMessage("Вы уверены, что хотите удалить этот чарник?")
        builder.setPositiveButton("Да") { dialog, _ ->
            dialog.dismiss()
            deleteCharsheet(uuid)
        }
        builder.setNegativeButton("Нет") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }

    private fun deleteCharsheet(uuid: String) {
        // Удаление записи из базы данных
        database.orderByChild("uuid").equalTo(uuid).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (dataSnapshot in snapshot.children) {
                        dataSnapshot.ref.removeValue()
                            .addOnSuccessListener {
                                Log.d("CharlistDetailsFragment", "Charsheet deleted from database")
                            }
                            .addOnFailureListener { exception ->
                                Log.e("CharlistDetailsFragment", "Failed to delete charsheet: ${exception.message}")
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CharlistDetailsFragment", "Database error: ${error.message}")
            }
        })

        // Удаление изображения из хранилища
        val storageRef = storage.reference.child("images/$uuid")
        storageRef.delete()
            .addOnSuccessListener {
                Log.d("CharlistDetailsFragment", "Image deleted from storage")
                findNavController().navigateUp()
            }
            .addOnFailureListener { exception ->
                Log.e("CharlistDetailsFragment", "Failed to delete image: ${exception.message}")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}