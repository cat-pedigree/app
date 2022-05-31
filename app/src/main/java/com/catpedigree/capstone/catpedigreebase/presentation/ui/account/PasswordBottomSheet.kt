package com.catpedigree.capstone.catpedigreebase.presentation.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentPasswordBottomSheetBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar

class PasswordBottomSheet : Fragment() {
    private lateinit var _binding: FragmentPasswordBottomSheetBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private val viewModel: AccountViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPasswordBottomSheetBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        binding.apply {

            edtPassword.editText?.doOnTextChanged { text, _, _, _ ->
                when{
                    text!!.length < 8 -> {
                        edtPassword.error = getString(R.string.password_required_length)
                    }
                    text.length >= 8 -> {
                        edtPassword.error = null
                    }
                }
            }

            edtConfirmPassword.editText?.doOnTextChanged { text, _, _, _ ->
                when{
                    text!!.length < 8 -> {
                        edtConfirmPassword.error = getString(R.string.password_required_length)
                    }
                    text.length >= 8 -> {
                        edtConfirmPassword.error = null
                    }
                }
            }

            btnConfirm.setOnClickListener {
                val password = edtPassword.editText?.text.toString().trim()
                val confirmPassword = edtConfirmPassword.editText?.text.toString().trim()
                when{
                    password.isEmpty() -> {
                        edtPassword.error = getString(R.string.password_required)
                    }
                    password.length < 8 -> {
                        edtPassword.error = getString(R.string.password_required_length)
                    }
                    confirmPassword.isEmpty() -> {
                        edtConfirmPassword.error = getString(R.string.password_required)
                    }
                    confirmPassword.length < 8 -> {
                        edtConfirmPassword.error = getString(R.string.password_required_length)
                    }
                    password != confirmPassword -> {
                        edtPassword.error = getString(R.string.password_match)
                    }else -> {
                        viewModel.changePassword(user.token ?: "", password)
                    }
                }
            }
        }
    }

    private fun setupViewModel(){
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
                Snackbar.make(binding.root, R.string.password_success, Snackbar.LENGTH_LONG).show()
                viewModel.logout()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        (if (isLoading) View.VISIBLE else View.GONE).also { binding.progressBar.visibility = it }
    }
}