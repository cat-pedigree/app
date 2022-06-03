package com.catpedigree.capstone.catpedigreebase.presentation.ui.home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import android.app.SearchManager
import android.content.Context.SEARCH_SERVICE
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentHomeBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.PostAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.SearchAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        setupAction()
        setupViewModel()
        setupNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    private fun setupAction() {
        val postAdapter = PostAdapter(onFavoriteClick = { post ->
            if (post.isBookmarked) {
                viewModel.deletePost(post)
            } else {
                viewModel.savePost(post)
            }
        }, onLoveClick = { post ->
            if (post.isLoved) {
                viewModel.deleteLovePost(post)
                viewModel.loveDelete(user.token ?: "", post.id!!, user.id!!)
            } else {
                viewModel.createLovePost(post)
                viewModel.loveCreate(user.token ?: "", post.id!!, user.id!!)
            }
        })

        binding.apply {
            viewModel.checkPost().observe(viewLifecycleOwner){
                if(it == 0){
                    tvNoData.visibility = View.VISIBLE
                }
            }

            viewModel.getPosts().observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            val postData = result.data
                            postAdapter.submitList(postData)
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

            rvPost.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                adapter = postAdapter
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

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }
    }

    private fun setupMenu() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.addPost -> {
                    findNavController().navigate(R.id.action_homeFragment_to_createPostFragment)
                    true
                }
                R.id.search -> {
                    val searchManager =
                        requireActivity().getSystemService(SEARCH_SERVICE) as SearchManager
                    val searchView = menuItem.actionView as SearchView

                    searchView.setSearchableInfo(searchManager.getSearchableInfo(requireActivity().componentName))
                    searchView.queryHint = resources.getString(R.string.search_hint)

                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            setupSearch(query!!)
                            searchView.clearFocus()
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            return false
                        }
                    })
                    true
                }
                R.id.profile -> {
                    findNavController().navigate(R.id.action_homeFragment_to_myProfileFragment)
                    true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_homeFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
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

    private fun setupSearch(name: String) {
        val searchAdapter = SearchAdapter()
        binding.apply {
            viewModel.search(name).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            rvPost.visibility = View.GONE
                            rvUsers.visibility = View.VISIBLE
                            val userData = result.data
                            searchAdapter.submitList(userData)
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
                adapter = searchAdapter
            }
        }
    }

    private fun setupNavigation() {
        binding.apply {
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_service -> {
                        findNavController().navigate(R.id.action_homeFragment_to_servicesFragment)
                        return@setOnItemSelectedListener false
                    }
                }
                return@setOnItemSelectedListener false
            }
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_resultFragment)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        (if (isLoading) View.VISIBLE else View.INVISIBLE).also {
            binding.progressBar.visibility = it
        }
    }
}