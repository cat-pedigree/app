package com.catpedigree.capstone.catpedigreebase.presentation.ui.account

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
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentAccountBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar


class AccountFragment : Fragment() {

    private lateinit var _binding: FragmentAccountBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private val viewModel: AccountViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(layoutInflater, container, false)
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
            menu1.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_emailBottomSheet)
            }
            menu2.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_passwordBottomSheet)
            }
            menu3.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(resources.getString(R.string.account_delete_acc))
                    .setMessage(resources.getString(R.string.account_delete_confirmation))
                    .setIcon(R.drawable.ic_baseline_delete)
                    .setNeutralButton(resources.getString(R.string.account_delete_cancel)) { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setPositiveButton(resources.getString(R.string.account_delete)) { dialog, _ ->
                        viewModel.userDelete(user.token ?: "", user.id!!)
                        dialog.dismiss()
                    }
                    .show()
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

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(binding.root, R.string.account_success, Snackbar.LENGTH_LONG)
                    .show()
                viewModel.logout()
            }
        }
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_accountFragment_to_myProfileFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_accountFragment_to_aboutFragment)
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
                        findNavController().navigate(R.id.action_accountFragment_to_homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_service -> {
                        findNavController().navigate(R.id.action_accountFragment_to_servicesFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_pedigree -> {
                        findNavController().navigate(R.id.action_accountFragment_to_pedigreeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_dating -> {
                        findNavController().navigate(R.id.action_accountFragment_to_datingFragment)
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener false
            }
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_accountFragment_to_resultFragment)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        (if (isLoading) View.VISIBLE else View.GONE).also { binding.progressBar.visibility = it }
    }
}