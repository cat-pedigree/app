package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentFollowerBinding
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentFollowingBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.FollowerAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.FollowingAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class FollowingFragment : Fragment() {
    private lateinit var _binding : FragmentFollowingBinding
    private val binding get() = _binding
    private val args: FollowerFragmentArgs by navArgs()

    private lateinit var users: UserItems

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
        setupMenu()
    }

    private fun setupAction(){
        val user = args.user
        val followingAdapter = FollowingAdapter()
        binding.apply {
            viewModel.getFollowing(user.id!!).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            rvUsers.visibility = View.GONE
                            rvUsers.visibility = View.VISIBLE
                            val userData = result.data
                            followingAdapter.submitList(userData)
                        }
                        is Result.Error -> {
                            progressBar.visibility = View.GONE
                            Toast.makeText(
                                context,
                                getString(R.string.result_error) + result.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            rvUsers.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                adapter = followingAdapter
            }
            topAppBar.title = user.username
        }
    }

    private fun setupViewModel() {
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.users = userItems
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_followingFragment_to_myProfileFragment)
                    return@setOnMenuItemClickListener true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_followingFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_followingFragment_to_aboutFragment)
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
        (if (isLoading) View.VISIBLE else View.INVISIBLE).also {
            binding.progressBar.visibility = it
        }
    }
}