package com.catpedigree.capstone.catpedigreebase.presentation.ui.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentLoginBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class LoginFragment : Fragment() {
    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding

    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel() {
        viewModel.userItem.observe(viewLifecycleOwner) { userModel ->
            if (userModel?.isLoggedIn == true) {
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }
    }

    private fun setupAction() {
        binding.apply {
            tvSignup.setOnClickListener {
                findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }

            passwordEditText.editText?.doOnTextChanged { text, _, _, _ ->
                when {
                    text!!.length < 8 -> {
                        passwordEditText.error = getString(R.string.password_required_length)
                    }
                    text.length >= 8 -> {
                        passwordEditText.error = null
                    }
                }
            }

            btnLogin.setOnClickListener {
                val email = emailEditText.editText?.text.toString()
                val password = passwordEditText.editText?.text.toString()

                when {
                    email.isEmpty() -> {
                        binding.emailEditText.error = getString(R.string.email_required)
                    }
                    !isValidEmail(email) -> {
                        binding.emailEditText.error = getString(R.string.email_pattern)
                    }
                    password.isEmpty() -> {
                        binding.passwordEditText.error = getString(R.string.password_required)
                    }
                    password.length < 8 -> {
                        binding.passwordEditText.error =
                            getString(R.string.password_required_length)
                    }
                    else -> {
                        viewModel.login(email, password)
                    }
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