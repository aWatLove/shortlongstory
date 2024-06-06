package com.example.lab8.ui.dice

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager


        binding.rollButton.setOnClickListener {
            // Скрыть клавиатуру
            imm.hideSoftInputFromWindow(view?.windowToken, 0)

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