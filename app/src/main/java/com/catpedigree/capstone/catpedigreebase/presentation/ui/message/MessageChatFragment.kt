package com.catpedigree.capstone.catpedigreebase.presentation.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentMessageChatBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.MessageAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.MessageRoomAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class MessageChatFragment : Fragment() {

    private lateinit var _binding: FragmentMessageChatBinding
    private val binding get() = _binding
    private val args: MessageChatFragmentArgs by navArgs()
    private lateinit var user: UserItems

    private val viewModel: MessageViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMessageChatBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
//        setupMenu()
    }

    private fun setupAction() {
        val room = args.room
        val profilePath = "${BuildConfig.BASE_API_PHOTO}${room.profile_photo_path}"
        binding.apply {
            Glide.with(root)
                .load(profilePath)
                .placeholder(R.drawable.ic_avatar)
                .signature(ObjectKey(profilePath))
                .centerCrop()
                .into(imgProfile)

            tvItemName.text = room.name
        }

        val messageAdapter = MessageAdapter()
        binding.apply {
            viewModel.getMessage(room.id!!).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            val messageRoomData = result.data
                            messageAdapter.submitList(messageRoomData)
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

            rvMessage.apply {
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                adapter = messageAdapter
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