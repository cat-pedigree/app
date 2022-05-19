package com.catpedigree.capstone.catpedigreebase.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentOnboardingTwoBinding
import com.catpedigree.capstone.catpedigreebase.factory.ViewModelFactory

class OnboardingTwo : Fragment() {
    private var _binding: FragmentOnboardingTwoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels {
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
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingTwo_to_onboardingThree)
        }
        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingTwo_to_loginFragment)
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