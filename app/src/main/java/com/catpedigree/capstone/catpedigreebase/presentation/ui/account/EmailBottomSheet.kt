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
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentEmailBottomSheetBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.google.android.material.snackbar.Snackbar

class EmailBottomSheet : Fragment() {
    private lateinit var _binding: FragmentEmailBottomSheetBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private val viewModel: AccountViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEmailBottomSheetBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
        setupMenu()
    }

    private fun setupAction() {
        binding.apply {
            btnConfirm.setOnClickListener {
                val email = edtEmail.editText?.text.toString().trim()
                when {
                    email.isEmpty() -> {
                        Snackbar.make(btnConfirm, R.string.email_required, Snackbar.LENGTH_LONG)
                            .show()
                    }
                    !isValidEmail(email) -> {
                        binding.edtEmail.error = getString(R.string.email_pattern)
                    }
                    else -> {
                        viewModel.changeEmail(user.token ?: "", email)
                    }
                }
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
                Snackbar.make(binding.btnConfirm, R.string.email_success, Snackbar.LENGTH_LONG)
                    .show()
                viewModel.logout()
            }
        }
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_emailBottomSheet_to_myProfileFragment)
                    true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_emailBottomSheet_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_emailBottomSheet_to_aboutFragment)
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

    private fun showLoading(isLoading: Boolean) {
        (if (isLoading) View.VISIBLE else View.GONE).also { binding.progressBar.visibility = it }
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}