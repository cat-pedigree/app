package com.catpedigree.capstone.catpedigreebase.presentation.ui.dating

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentDatingBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class DatingFragment : Fragment() {

    private lateinit var _binding: FragmentDatingBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private val viewModel: DatingViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDatingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
        setupMenu()
        setupNavigation()
    }

    private fun setupAction() {
        binding.apply {
            cardView.setOnClickListener {
                Snackbar.make(cardView, R.string.development, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setupViewModel() {
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
        }
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_datingFragment_to_myProfileFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_datingFragment_to_accountFragment)
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_datingFragment_to_aboutFragment)
                    true
                }
                R.id.logout -> {
                    viewModel.logout()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun setupNavigation() {
        binding.apply {
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> {
                        findNavController().navigate(R.id.action_datingFragment_to_homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_service -> {
                        findNavController().navigate(R.id.action_datingFragment_to_servicesFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_pedigree -> {
                        findNavController().navigate(R.id.action_datingFragment_to_pedigreeFragment)
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener false
            }
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_datingFragment_to_resultFragment)
            }
        }
    }
}