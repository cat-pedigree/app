package com.catpedigree.capstone.catpedigreebase.ui.comment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.adapter.CommentAdapter
import com.catpedigree.capstone.catpedigreebase.adapter.LoadingStateAdapter
import com.catpedigree.capstone.catpedigreebase.data.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentCommentBinding
import com.catpedigree.capstone.catpedigreebase.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.catpedigree.capstone.catpedigreebase.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CommentFragment : Fragment() {

    private lateinit var _binding: FragmentCommentBinding
    private val binding get() = _binding
    private val args: CommentFragmentArgs by navArgs()
    private lateinit var user: UserItems

    private lateinit var adapter: CommentAdapter

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

        binding.btnSend.setOnClickListener {
            post.id?.let { data -> createComment(data) }
        }

        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        adapter = CommentAdapter()
        binding.rvComments.adapter = adapter

        adapter.refresh()

        binding.swipeLayout.setOnRefreshListener {
            adapter.refresh()
            binding.swipeLayout.isRefreshing = false
        }

        wrapEspressoIdlingResource {
            lifecycleScope.launch {
                adapter.loadStateFlow.collect {
                    binding.progressBar.isVisible = (it.refresh is LoadState.Loading)
                    binding.tvNoData.isVisible = it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached && adapter.itemCount < 1
                    if (it.refresh is LoadState.Error) {
                        ToastUtils.showToast(
                            requireContext(),
                            (it.refresh as LoadState.Error).error.localizedMessage?.toString()
                                ?: getString(R.string.error_load)
                        )
                    }
                }
            }
        }

    }

    private fun setupViewModel(){
        val post = args.post
        viewModel.userItem.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
        }

        post.id?.let {
            viewModel.comments(it).observe(viewLifecycleOwner) { comments ->
                adapter.submitData(lifecycle, comments)
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                findNavController().navigateUp()
            }
        }
    }



    private fun createComment(post_id: Int){
        val userId = user.id
        val description = binding.edtComment.text.toString().trim()

            if (userId != null) {
                viewModel.createComment(user.token ?: "", post_id, userId, description)
            }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}