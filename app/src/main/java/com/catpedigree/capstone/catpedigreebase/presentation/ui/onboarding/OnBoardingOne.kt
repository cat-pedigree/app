package com.catpedigree.capstone.catpedigreebase.presentation.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentOnboardingOneBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory

class OnBoardingOne : Fragment() {

    private lateinit var _binding: FragmentOnboardingOneBinding
    private val binding get() = _binding

    private val viewModel: OnBoardingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction() {
        binding.apply {
            btnContinue.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingOne_to_onboardingTwo2)
            }
            btnSkip.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingOne_to_loginFragment)
            }
        }
    }

    private fun setupViewModel() {
        viewModel.userItem.observe(viewLifecycleOwner) { userModel ->
            if (userModel?.isLoggedIn == true) {
                findNavController().navigate(R.id.action_onboardingOne_to_loginFragment)
            }
        }
    }
}