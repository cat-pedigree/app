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
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentPostDetailUserBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.PostUserDetailAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class PostDetailUserFragment : Fragment() {

    private lateinit var _binding: FragmentPostDetailUserBinding
    private val binding get() = _binding

    private lateinit var user: UserItems
    private val args: PostDetailUserFragmentArgs by navArgs()

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostDetailUserBinding.inflate(layoutInflater, container, false)
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
        val post = args.post
        val postAdapterProfile = PostUserDetailAdapter(onFavoriteClick = { posts ->
            if (posts.isBookmarked) {
                viewModel.deletePost(posts)
            } else {
                viewModel.savePost(posts)
            }
//
        }, onLoveClick = { posts ->
            if (posts.isLoved) {
                viewModel.deleteLovePost(posts)
                viewModel.loveDelete(user.token ?: "", posts.id!!, user.id!!)
            } else {
                viewModel.createLovePost(posts)
                viewModel.loveCreate(user.token ?: "", posts.id!!, user.id!!)
            }
        })

        viewModel.getPostProfile(post.user_id!!).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val postData = result.data
                        postAdapterProfile.submitList(postData)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            getString(R.string.result_error) + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = postAdapterProfile
        }
    }

    private fun setupViewModel() {
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
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
                    findNavController().navigate(R.id.action_postDetailUserFragment_to_myProfileFragment)
                    return@setOnMenuItemClickListener true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_postDetailUserFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_postDetailUserFragment_to_aboutFragment)
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
                        findNavController().navigate(R.id.action_postDetailUserFragment_to_homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_service -> {
                        findNavController().navigate(R.id.action_postDetailUserFragment_to_servicesFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_pedigree -> {
                        findNavController().navigate(R.id.action_postDetailUserFragment_to_pedigreeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_dating -> {
                        findNavController().navigate(R.id.action_postDetailUserFragment_to_datingFragment)
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener false
            }
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_postDetailUserFragment_to_resultFragment)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        (if (isLoading) View.VISIBLE else View.INVISIBLE).also {
            binding.progressBar.visibility = it
        }
    }
}