package com.example.lab8

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab8.databinding.FragmentCharsheetsPreviewBinding
import com.example.lab8.databinding.FragmentDiceBinding
import kotlin.random.Random


class diceFragment : Fragment() {

    private lateinit var binding: FragmentDiceBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiceBinding.inflate(inflater, container, false)


        val countTextfield = binding.diceCount
        val diceTextfield = binding.dice
        val modTextfield = binding.modifier

        val resultTextView = binding.resultTextview

        binding.rollButton.setOnClickListener {
            val count = countTextfield.text.toString().toIntOrNull() ?: 1
            val dice = diceTextfield.text.toString().toIntOrNull() ?: 20
            val mod = modTextfield.text.toString().toIntOrNull() ?: 0

            var res = 0

            for (i in 1..count) {
                res += Random.nextInt(dice) + 1
            }
            res += mod

            binding.resultTextview.setText(res.toString())
        }

        return binding.root
    }

}