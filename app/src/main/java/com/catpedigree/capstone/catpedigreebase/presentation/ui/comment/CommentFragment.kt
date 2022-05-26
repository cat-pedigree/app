package com.catpedigree.capstone.catpedigreebase.presentation.ui.comment

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
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentCommentBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.CommentAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.home.HomeFragmentDirections
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

    @SuppressLint("NotifyDataSetChanged")
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

//        binding.btnSend.setOnClickListener {
//            createComment(post.id)
//        }

    }

    private fun setupViewModel(){
        val post = args.post

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

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                ToastUtils.showToast(requireContext(), "Comment successfully added")
//                findNavController().navigate(R.id.commentFragment)

            }
        }
    }



//    private fun createComment(post_id: Int){
//        val userId = user.id
//        val description = binding.edtComment.text.toString().trim()
//
//            if (userId != null) {
//                viewModel.createComment(user.token ?: "", post_id, userId, description)
//            }
//    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}