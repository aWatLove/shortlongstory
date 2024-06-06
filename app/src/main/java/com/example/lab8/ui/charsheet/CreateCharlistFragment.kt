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
import com.example.lab8.databinding.FragmentCreateCharlistBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.mikepenz.iconics.typeface.library.fontawesome.FontAwesome.author
import java.util.UUID


class CreateCharlistFragment : Fragment() {

    private var _binding: FragmentCreateCharlistBinding? = null
    private val binding get() = _binding!!
    private val database = FirebaseDatabase.getInstance().getReference("Charsheet")
    private val storage = FirebaseStorage.getInstance().reference
    private var imageUri: Uri? = null
    companion object {
        private const val IMAGE_PICK_CODE = 1000
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCharlistBinding.inflate(inflater, container, false)

        binding.createCreateButton.setOnClickListener {
            saveCreature()
        }

        binding.loadImageButton.setOnClickListener {
            pickImageFromGallery()
        }

        return binding.root
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


        val charsheetId = UUID.randomUUID().toString()
        val charsheet = Charsheet(uuid = charsheetId,maker?:"Unknown", level, name, charClass, race,
            strength, dexterity, constitution, intellegence, wisdom,charisma,
            0, armorClass,initiative,speed,hitpoints,
            "",null, null, null, null, bioinfo)

//        database.push().setValue(charsheet).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                Toast.makeText(requireContext(), "Charsheet saved", Toast.LENGTH_SHORT).show()
//                findNavController().navigate(R.id.nav_charsheet)
//            } else {
//                Toast.makeText(requireContext(), "Failed to save charsheet", Toast.LENGTH_SHORT).show()
//            }
//        }

        database.child(charsheetId).setValue(charsheet).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageUri?.let { uri ->
                    uploadImage(charsheetId, uri)
                } ?: run {
                    Toast.makeText(requireContext(), "Charsheet saved", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.nav_charsheet)
                }
            } else {
                Toast.makeText(requireContext(), "Failed to save charsheet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(charsheetId: String, uri: Uri) {
        val storageRef = storage.child("images/$charsheetId")
        storageRef.putFile(uri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
//                database.child(charsheetId).child("imageUrl").setValue(downloadUrl.toString())
                Toast.makeText(requireContext(), "Charsheet and image saved", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.nav_charsheet)
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