package com.catpedigree.capstone.catpedigreebase.presentation.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentOnboardingThreeBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory

class OnBoardingThree : Fragment() {

    private lateinit var _binding: FragmentOnboardingThreeBinding
    private val binding get() = _binding

    private val viewModel: OnBoardingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingThreeBinding.inflate(inflater, container, false)
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
                findNavController().navigate(R.id.action_onboardingThree_to_onboardingFour)
            }
            btnSkip.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingThree_to_loginFragment)
            }
        }
    }

    private fun setupViewModel() {
        viewModel.userItem.observe(viewLifecycleOwner) { userModel ->
            if (userModel?.isLoggedIn == true) {
                findNavController().navigate(R.id.action_onboardingThree_to_loginFragment)
            }
        }
    }
}