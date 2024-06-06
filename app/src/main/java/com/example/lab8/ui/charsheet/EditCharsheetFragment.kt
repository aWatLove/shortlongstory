package com.example.lab8.ui.charsheet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.lab8.R
import com.example.lab8.databinding.FragmentEditCharsheetBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class EditCharsheetFragment : Fragment() {

    private var _binding: FragmentEditCharsheetBinding? = null
    private val binding get() = _binding!!
    private val database = FirebaseDatabase.getInstance().getReference("Charsheet")
    private val storage = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null
    private lateinit var charsheetId: String

    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditCharsheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let { bundle ->
            charsheetId = bundle.getString("uuid").toString()
            if (charsheetId.isNotEmpty()) {
                fetchCharsheet(charsheetId)
            }
        }

        binding.editButton.setOnClickListener {
            saveChanges()
        }

        binding.loadImageButton.setOnClickListener {
            pickImageFromGallery()
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            imageUri = data?.data
            Toast.makeText(requireContext(), "Image selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCharsheet(uuid: String) {
        database.child(uuid).get().addOnSuccessListener { snapshot ->
            val charsheet = snapshot.getValue(Charsheet::class.java)
            if (charsheet != null) {
                displayCharSheet(charsheet)
            }
        }
    }

    private fun displayCharSheet(charsheet: Charsheet) {
        binding.name.setText(charsheet.name)
        binding.level.setText(charsheet.level.toString())
        binding.charclass.setText(charsheet.charClass)
        binding.race.setText(charsheet.race)
        binding.strength.setText(charsheet.strength.toString())
        binding.dexterity.setText(charsheet.dexterity.toString())
        binding.constitution.setText(charsheet.constitution.toString())
        binding.intellegence.setText(charsheet.intelligence.toString())
        binding.wisdom.setText(charsheet.wisdom.toString())
        binding.charisma.setText(charsheet.charisma.toString())
        binding.charkd.setText(charsheet.armorClass.toString())
        binding.charinitiative.setText(charsheet.initiative.toString())
        binding.charspeed.setText(charsheet.speed.toString())
        binding.hitpoints.setText(charsheet.hits.toString())
        binding.charbioinfo.setText(charsheet.bioinfo)
    }

    private fun saveChanges() {
        val name = binding.name.text.toString()
        val level = binding.level.text.toString().toInt()
        val charClass = binding.charclass.text.toString()
        val race = binding.race.text.toString()
        val strength = binding.strength.text.toString().toInt()
        val dexterity = binding.dexterity.text.toString().toInt()
        val constitution = binding.constitution.text.toString().toInt()
        val intelligence = binding.intellegence.text.toString().toInt()
        val wisdom = binding.wisdom.text.toString().toInt()
        val charisma = binding.charisma.text.toString().toInt()
        val armorClass = binding.charkd.text.toString().toInt()
        val initiative = binding.charinitiative.text.toString().toInt()
        val speed = binding.charspeed.text.toString().toInt()
        val hits = binding.hitpoints.text.toString().toInt()
        val bioinfo = binding.charbioinfo.text.toString()


        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val maker = sharedPref.getString("user_login", "Unknown") ?: "Unknown"

        val updatedCharsheet = Charsheet(
            uuid = charsheetId,
            author = maker,
            level = level,
            name = name,
            charClass = charClass,
            race = race,
            strength = strength,
            dexterity = dexterity,
            constitution = constitution,
            intelligence = intelligence,
            wisdom = wisdom,
            charisma = charisma,
            experience = 0,
            armorClass = armorClass,
            initiative = initiative,
            speed = speed,
            hits = hits,
            bioinfo = bioinfo
        )

        database.child(charsheetId).setValue(updatedCharsheet).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageUri?.let { uri ->
                    uploadImage(charsheetId, uri)
                } ?: run {
                    Toast.makeText(requireContext(), "Charsheet updated", Toast.LENGTH_SHORT).show()
                    val bundleCharsheet = Bundle().apply {
                        putString("uuid",charsheetId)
                    }
                    findNavController().navigate(R.id.nav_charsheet, bundleCharsheet)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to update charsheet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(charsheetId: String, uri: Uri) {
        val storageRef = storage.child("images/$charsheetId")
        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//                database.child(charsheetId).child("imageUrl").setValue(downloadUrl.toString())
                Toast.makeText(requireContext(), "Charsheet and image updated", Toast.LENGTH_SHORT).show()
                val bundleCharsheet = Bundle().apply {
                    putString("uuid",charsheetId)
                }
                findNavController().navigate(R.id.nav_charsheet, bundleCharsheet)
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Image upload failed", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
