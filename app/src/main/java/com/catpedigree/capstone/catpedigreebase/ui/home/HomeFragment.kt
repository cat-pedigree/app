package com.catpedigree.capstone.catpedigreebase.ui.home

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.adapter.LoadingStateAdapter
import com.catpedigree.capstone.catpedigreebase.adapter.PostAdapter
import com.catpedigree.capstone.catpedigreebase.data.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentHomeBinding
import com.catpedigree.capstone.catpedigreebase.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.catpedigree.capstone.catpedigreebase.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var _binding: FragmentHomeBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private lateinit var adapter: PostAdapter
    private lateinit var layoutManager: GridLayoutManager

    private var isFromOtherScreen = false

    private val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    private fun setupAction(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.addPost -> {
                    findNavController().navigate(R.id.action_homeFragment_to_createFragment)
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

            adapter = PostAdapter().apply {
                stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
                registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                        if (positionStart == 0 && isFromOtherScreen.not()) {
                        binding.rvPost.smoothScrollToPosition(0)
                    }
                }
            })
        }

        val adapterWithLoading =
            adapter.withLoadStateFooter(footer = LoadingStateAdapter { adapter.retry() })
            layoutManager = GridLayoutManager(requireContext(), 2)

        binding.rvPost.layoutManager = layoutManager
        binding.rvPost.adapter = adapterWithLoading
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
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
        }

        viewModel.posts.observe(viewLifecycleOwner) { posts ->
            adapter.submitData(lifecycle, posts)
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