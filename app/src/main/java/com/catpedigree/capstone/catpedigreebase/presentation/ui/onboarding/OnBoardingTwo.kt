package com.catpedigree.capstone.catpedigreebase.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentOnboardingTwoBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory

class OnBoardingTwo : Fragment() {
    private lateinit var _binding: FragmentOnboardingTwoBinding
    private val binding get() = _binding

    private val viewModel: OnBoardingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingTwoBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        binding.apply {
            btnContinue.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingTwo_to_onboardingThree)
            }
            btnSkip.setOnClickListener {
                findNavController().navigate(R.id.action_onboardingTwo_to_loginFragment)
            }
        }
    }

    private fun setupViewModel(){
        viewModel.userItem.observe(viewLifecycleOwner) { userModel ->
            if (userModel?.isLoggedIn == true) {
                findNavController().navigate(R.id.action_onboardingTwo_to_loginFragment)
            }
        }
    }
}