package com.catpedigree.capstone.catpedigreebase.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentOnboardingThreeBinding
import com.catpedigree.capstone.catpedigreebase.factory.ViewModelFactory

class OnboardingThree : Fragment() {
    private var _binding: FragmentOnboardingThreeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingThreeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        binding.btnContinue.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingThree_to_onboardingFour)
        }
        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingThree_to_loginFragment)
        }
    }

    private fun setupViewModel(){
        viewModel.userItem.observe(viewLifecycleOwner) { userModel ->
            if (userModel?.isLoggedIn == true) {
                findNavController().navigate(R.id.action_onboardingThree_to_loginFragment)
            }
        }
    }
}