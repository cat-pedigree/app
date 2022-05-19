package com.catpedigree.capstone.catpedigreebase.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentLoginBinding
import com.catpedigree.capstone.catpedigreebase.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel(){
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

    private fun setupAction(){
        binding.tvSignup.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.emailEditText.editText?.text.toString()
            val password = binding.passwordEditText.editText?.text.toString()

            when{
                email.isEmpty() -> {
                    binding.emailEditText.error = getString(R.string.email_required)
                }
                !isValidEmail(email) -> {
                    binding.emailEditText.error = getString(R.string.email_pattern)
                }
                password.isEmpty() -> {
                    binding.passwordEditText.error = getString(R.string.password_required)
                }
                password.length < 6 -> {
                    binding.passwordEditText.error = getString(R.string.password_required_length)
                }
                else -> {
                    viewModel.login(email, password)
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        (if (isLoading) View.VISIBLE else View.GONE).also { binding.progressBar.visibility = it }
    }

    private fun isValidEmail(email: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}