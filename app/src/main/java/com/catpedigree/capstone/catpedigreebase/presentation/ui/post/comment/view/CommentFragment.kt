package com.catpedigree.capstone.catpedigreebase.presentation.ui.post.comment.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentCommentBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.CommentAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class CommentFragment : Fragment() {

    private lateinit var _binding: FragmentCommentBinding
    private val binding get() = _binding
    private val args: CommentFragmentArgs by navArgs()
    private lateinit var user: UserItems

    private val viewModel: CommentViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        val post = args.post

        val commentAdapter = CommentAdapter()

        viewModel.comments(post.id!!).observe(viewLifecycleOwner){result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.GONE
                        val commentData = result.data
                        commentAdapter.submitList(commentData)
                    }
                    is Result.Error -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        binding.rvComments.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            adapter = commentAdapter
        }

        val profilePhotoPath = "http://192.168.1.4/api-cat/public/storage/${post.profile_photo_path}"

        binding.apply {
            tvItemName.text = post.name
            tvPost.text = post.description
            Glide.with(binding.root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .into(imgProfile)
        }

        binding.btnAddComment.setOnClickListener {
            Navigation.findNavController(binding.btnAddComment).navigate(
                CommentFragmentDirections.actionCommentFragmentToCreateCommentFragment(
                    post
                )
            )
        }

    }

    private fun setupViewModel(){
        viewModel.userItem.observe(viewLifecycleOwner) { userItems ->
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
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}