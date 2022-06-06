package com.catpedigree.capstone.catpedigreebase.presentation.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentMessageBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.MessageRoomAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class MessageFragment : Fragment() {

    private lateinit var _binding: FragmentMessageBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private val viewModel: MessageViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
//        setupMenu()
//        setupNavigation()
    }

    private fun setupAction() {
        val messageRoomAdapter = MessageRoomAdapter()
        binding.apply {
            viewModel.getMessageRoom().observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            val messageRoomData = result.data
                            messageRoomAdapter.submitList(messageRoomData)
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

            rvMessageRoom.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                adapter = messageRoomAdapter
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

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }
    }

//    private fun setupMenu(){
//        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
//            when(menuItem.itemId){
//                R.id.account -> {
//                    findNavController().navigate(R.id.action_myProfileFragment_to_accountFragment)
//                    true
//                }
//                R.id.language -> {
//                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
//                    true
//                }
//                R.id.about -> {
//                    findNavController().navigate(R.id.action_myProfileFragment_to_aboutFragment)
//                    true
//                }
//                R.id.logout -> {
//                    viewModel.logout()
//                    true
//                }
//                else -> {
//                    false
//                }
//            }
//        }
//    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}