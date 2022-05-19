package com.catpedigree.capstone.catpedigreebase.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentOnboardingOneBinding
import com.catpedigree.capstone.catpedigreebase.factory.ViewModelFactory

class OnboardingOne : Fragment() {

    private var _binding: FragmentOnboardingOneBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OnboardingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingOneBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        binding.btnContinue.setOnClickListener{
            findNavController().navigate(R.id.action_onboardingOne_to_onboardingTwo2)
        }
        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingOne_to_loginFragment)
        }
    }

    private fun setupViewModel(){
        viewModel.userItem.observe(viewLifecycleOwner) { userModel ->
            if (userModel?.isLoggedIn == true) {
                findNavController().navigate(R.id.action_onboardingOne_to_loginFragment)
            }
        }
    }

}