package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentFavoriteMyProfileBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.PostFavoriteProfileAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.favorite.FavoriteViewModel
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class FavoriteMyProfileFragment : Fragment() {

    private lateinit var _binding: FragmentFavoriteMyProfileBinding
    private val binding get() = _binding

    private lateinit var user: UserItems

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteMyProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction() {
        val postAdapterFavorite = PostFavoriteProfileAdapter()

        viewModel.getPostFavorite().observe(viewLifecycleOwner) { postFavorite ->
            showLoading(false)
            postAdapterFavorite.submitList(postFavorite)
        }

        binding.rvPost.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = postAdapterFavorite
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

    private fun showLoading(isLoading: Boolean) {
        (if (isLoading) View.VISIBLE else View.INVISIBLE).also {
            binding.progressBar.visibility = it
        }
    }
}