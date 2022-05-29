package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.favorite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentFavoriteBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.PostFavoriteAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class FavoriteFragment : Fragment() {
    private lateinit var _binding: FragmentFavoriteBinding
    private val binding get() = _binding

    private lateinit var user: UserItems

    private val viewModel: FavoriteViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        val postAdapterFavorite = PostFavoriteAdapter(onFavoriteClick = { post ->
            if (post.isBookmarked) {
                viewModel.deletePost(post)
                viewModel.getPostFavoriteCount().observe(viewLifecycleOwner){
                    if(it == 0){
                        findNavController().navigate(R.id.action_favoriteFragment_to_emptyFavoriteFragment)
                    }
                }
            } else {
                viewModel.savePost(post)
            }
        }, onLoveClick = {post ->
            if(post.isLoved){
                viewModel.deleteLovePost(post)
                viewModel.loveDelete(user.token ?: "",post.id!!, user.id!!)
            }else{
                viewModel.createLovePost(post)
                viewModel.loveCreate(user.token ?: "",post.id!!, user.id!!)
            }
        })

        viewModel.getPostFavorite().observe(viewLifecycleOwner) { postFavorite ->
            showLoading(false)
            postAdapterFavorite.submitList(postFavorite)
            if(postAdapterFavorite.itemCount == 0){
                findNavController().navigate(R.id.action_favoriteFragment_to_emptyFavoriteFragment)
            }
        }

        binding.rvPost.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = postAdapterFavorite
        }
    }

    private fun setupViewModel(){
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
        (if (isLoading) View.VISIBLE else View.INVISIBLE).also { binding.progressBar.visibility = it }
    }
}