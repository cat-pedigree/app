package com.catpedigree.capstone.catpedigreebase.presentation.ui.post.comment.create

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentCreateCommentBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class CreateCommentFragment : Fragment() {

    private lateinit var _binding: FragmentCreateCommentBinding
    private val binding get() = _binding

    private val args:CreateCommentFragmentArgs by navArgs()
    private lateinit var user: UserItems

    private val viewModel: CreateCommentViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateCommentBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        val post = args.post
        binding.btnComment.setOnClickListener {
            val userId = user.id
            val postId = post.id
            val description = binding.edtComment.editText?.text.toString().trim()

            if (userId != null) {
                if (postId != null) {
                    when{
                        description.isEmpty() ->{
                            binding.edtComment.error = "Enter Comment"
                        }
                        else -> {
                            viewModel.createComment(user.token ?: "", postId, userId, description)
                        }
                    }
                }
            }
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

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                ToastUtils.showToast(requireContext(), "Comment successfully added")
                findNavController().navigateUp()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

}