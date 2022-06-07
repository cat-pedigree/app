package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserDataItems
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentProfileBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.CatProfileAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.PostProfileUserAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class ProfileFragment : Fragment() {
    private lateinit var _binding: FragmentProfileBinding
    private val binding get() = _binding
    private val args: ProfileFragmentArgs by navArgs()
    private lateinit var users: UserItems

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
        setupMenu()
    }

    private fun setupAction() {
        val user = args.user
        val catProfileAdapter = CatProfileAdapter()
        val postProfileAdapter = PostProfileUserAdapter()
        val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${user.profile_photo_path}"
        binding.apply {
            Glide.with(root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_avatar)
                .circleCrop()
                .into(ivAvatar)
            tvName.text = user.name
            tvBio.text = user.bio
            tvPostCount.text = user.postsCount.toString()
            viewModel.getFollowersCount(user.id!!).observe(viewLifecycleOwner){
                binding.tvFollowerCount.text = it.toString()
            }
            tvFollowingCount.text = user.following.toString()
            topAppBar.title = user.username

            ivFollowerCount.setOnClickListener {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToFollowerFragment(
                        user
                    )
                )
            }

            ivFollowingCount.setOnClickListener {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileFragmentToFollowingFragment(
                        user
                    )
                )
            }

            btnMessage.setOnClickListener {
                viewModel.postRoom(users.token?:"", user.id)
            }

            viewModel.checkCat(user.id).observe(viewLifecycleOwner){
                if(it == 0){
                    myCats.visibility = View.GONE
                    rvCats.visibility = View.GONE
                }
            }

            viewModel.getCat(user.id).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            val catData = result.data
                            catProfileAdapter.submitList(catData)
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

            rvCats.apply {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                adapter = catProfileAdapter
            }

            viewModel.checkPost(user.id).observe(viewLifecycleOwner){
                if(it == 0){
                    rvPostProfile.visibility = View.GONE
                    ivNoData.visibility = View.VISIBLE
                    tvNoData.visibility = View.VISIBLE
                }
            }

            viewModel.getPostProfile(user.id).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            val postData = result.data
                            postProfileAdapter.submitList(postData)
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
            rvPostProfile.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                adapter = postProfileAdapter
            }


        }

        setupFollow(user)
    }

    private fun setupViewModel() {
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.users = userItems
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(requireContext(), "Berhasil", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_profileFragment_to_myProfileFragment)
                    return@setOnMenuItemClickListener true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_profileFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_profileFragment_to_aboutFragment)
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

    private fun setupFollow(userDataItems: UserDataItems){
        val user = args.user
        var isCheck = false
        viewModel.checkFollow(user.id!!).observe(viewLifecycleOwner) {
            if (it > 0) {
                binding.btnFollow.isChecked = true
                isCheck = true
            }else{
                binding.btnFollow.isChecked = false
                isCheck = false
            }
        }

        binding.btnFollow.setOnClickListener {
            isCheck = !isCheck
            if(isCheck){
                viewModel.follow(users.token?:"", user.id,users.id!!)
                viewModel.saveFollow(userDataItems)
                viewModel.getFollowersCount(user.id).observe(viewLifecycleOwner){
                    binding.tvFollowerCount.text = it.toString()
                }
                Toast.makeText(requireContext(),"Follow", Toast.LENGTH_LONG).show()
            }else{
                viewModel.followDelete(users.token?:"", user.id, users.id!!)
                viewModel.deleteFollow(userDataItems)
                viewModel.getFollowersCount(user.id).observe(viewLifecycleOwner){
                    binding.tvFollowerCount.text = it.toString()
                }
                Toast.makeText(requireContext(),"Unfollow", Toast.LENGTH_LONG).show()
            }
            binding.btnFollow.isChecked = isCheck
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }


}