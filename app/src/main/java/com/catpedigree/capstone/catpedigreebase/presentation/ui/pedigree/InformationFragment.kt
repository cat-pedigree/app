package com.catpedigree.capstone.catpedigreebase.presentation.ui.pedigree

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentInformationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class InformationFragment : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentInformationBinding
    private val binding get() = _binding
    private val args: InformationFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInformationBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        val cat = args.cat
        binding.apply {
                tvName.text = cat.name
                tvBreed.text = cat.breed
                tvColor.text = cat.color
                tvEyeColor.text = cat.eye_color
                tvHairColor.text = cat.hair_color
                tvEarShape.text = cat.ear_shape
            }
        }
}