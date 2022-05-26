package com.catpedigree.capstone.catpedigreebase.presentation.ui.cat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentAddCatBinding

class AddCatFragment : Fragment() {

    private var _binding: FragmentAddCatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentAddCatBinding.inflate(inflater, container,false)

        val breed = resources.getStringArray(R.array.item_breed)
        val adapterBreed = ArrayAdapter(requireContext(), R.layout.item_dropdown, breed)
        binding.breed.setAdapter(adapterBreed)

        val gender = resources.getStringArray(R.array.item_gender)
        val adapterGender = ArrayAdapter(requireContext(), R.layout.item_dropdown, gender)
        binding.gender.setAdapter(adapterGender)

        return binding.root
    }

}