package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentMyProfileBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.SectionPagerAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.google.android.material.tabs.TabLayoutMediator

class MyProfileFragment : Fragment() {

    private lateinit var _binding: FragmentMyProfileBinding
    private val binding get() = _binding
    private lateinit var user: UserItems

    private val viewModel: MyProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyProfileBinding.inflate(layoutInflater,container,false)

        val bundle = Bundle()
        val sectionPagerAdapter = SectionPagerAdapter(requireActivity() as AppCompatActivity, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager){
                tab, position ->
                tab.icon = ContextCompat.getDrawable(requireActivity(), TAB_ICONS[position])
        }.attach()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_myProfileFragment_to_editProfileFragment)
        }
    }
    private fun setupViewModel(){
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
            val profilePhotoPath = "http://192.168.1.4/api-cat/public/storage/${user.profile_photo_path}"
            binding.apply {
                tvName.text = user.name
                tvBio.text = user.bio ?: "Bio"
                tvPostCount.text = user.postsCount.toString()
                topAppBar.title = user.username
                Glide.with(binding.root)
                    .load(profilePhotoPath)
                    .signature(ObjectKey(profilePhotoPath))
                    .placeholder(R.drawable.ic_avatar)
                    .circleCrop()
                    .into(ivAvatar)

            }
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

    companion object {
        @StringRes
        private val TAB_ICONS = intArrayOf(R.drawable.ic_baseline_dashboard_24, R.drawable.ic_baseline_bookmark_black)
    }
}