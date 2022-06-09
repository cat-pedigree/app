package com.catpedigree.capstone.catpedigreebase.presentation.ui.services

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
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentServicesBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.maps.MapsFragment
import com.catpedigree.capstone.catpedigreebase.presentation.ui.veterinary.VeterinaryViewModel
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class ServicesFragment : Fragment() {

    private lateinit var _binding: FragmentServicesBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private val viewModel: VeterinaryViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentServicesBinding.inflate(layoutInflater, container, false)
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
            menuVeterinary.setOnClickListener {
                findNavController().navigate(R.id.action_servicesFragment_to_veterinaryFragment)
            }
            menuMaps.setOnClickListener {
                findNavController().navigate(
                    ServicesFragmentDirections.actionServicesFragmentToMapsFragment(
                        MapsFragment.ACTION_CATS
                    )
                )
            }
        }
    }

    private fun setupViewModel() {
        viewModel.userItem.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_servicesFragment_to_myProfileFragment)
                    true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_servicesFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_servicesFragment_to_aboutFragment)
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
                        findNavController().navigate(R.id.action_servicesFragment_to_homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_pedigree -> {
                        findNavController().navigate(R.id.action_servicesFragment_to_pedigreeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_dating -> {
                        findNavController().navigate(R.id.action_servicesFragment_to_datingFragment)
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener false
            }
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_servicesFragment_to_resultFragment)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

}