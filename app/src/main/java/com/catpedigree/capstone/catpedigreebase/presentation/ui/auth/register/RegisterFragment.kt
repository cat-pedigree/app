package com.catpedigree.capstone.catpedigreebase.presentation.ui.auth.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentRegisterBinding
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory

class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    private val binding get() = _binding

    private val viewModel: RegisterViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        binding.btnRegister.setOnClickListener {
            val name = binding.nameEditText.editText?.text.toString()
            val username = binding.usernameEditText.editText?.text.toString()
            val phoneNumber = binding.phoneNumberEditText.editText?.text.toString()
            val email = binding.emailEditText.editText?.text.toString()
            val password = binding.passwordEditText.editText?.text.toString()
            when{
                name.isEmpty() -> {
                    binding.nameEditText.error = getString(R.string.name_required)
                }
                username.isEmpty() -> {
                    binding.usernameEditText.error = getString(R.string.username_required)
                }
                phoneNumber.isEmpty() -> {
                    binding.phoneNumberEditText.error = getString(R.string.phone_number_required)
                }
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
                    viewModel.register(name,username,phoneNumber,email,password)
                }
            }
        }
        binding.tvLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupViewModel(){
        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                ToastUtils.showToast(requireContext(), "Successfully signup, please continue to login.")
                findNavController().navigateUp()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun isValidEmail(email: String): Boolean{
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}