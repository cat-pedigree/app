package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.view

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentMyProfileBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.CatAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.SectionPagerAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
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
        _binding = FragmentMyProfileBinding.inflate(layoutInflater, container, false)

        val bundle = Bundle()
        val sectionPagerAdapter =
            SectionPagerAdapter(requireActivity() as AppCompatActivity, bundle)
        binding.viewPager.adapter = sectionPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.icon = ContextCompat.getDrawable(requireActivity(), TAB_ICONS[position])
        }.attach()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
        setupMenu()
        setupNavigation()
    }

    private fun setupAction() {
        binding.apply {
            btnEditProfile.setOnClickListener {
                findNavController().navigate(R.id.action_myProfileFragment_to_editProfileFragment)
            }

            ivFollowerCount.setOnClickListener {
                findNavController().navigate(R.id.action_myProfileFragment_to_myFollowerFragment)
            }

            ivFollowingCount.setOnClickListener {
                findNavController().navigate(R.id.action_myProfileFragment_to_myFollowingFragment)
            }
        }

        setupCatAdapter()
    }

    private fun setupViewModel() {
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
            val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${user.profile_photo_path}"
            binding.apply {
                tvName.text = user.name
                tvBio.text = user.bio ?: "Bio"
                tvPostCount.text = user.postsCount.toString()
                tvFollowerCount.text = user.followersCount.toString()
                tvFollowingCount.text = user.following.toString()
                topAppBar.title = user.username

                Glide.with(root)
                    .load(profilePhotoPath)
                    .signature(ObjectKey(profilePhotoPath))
                    .placeholder(R.drawable.ic_avatar)
                    .circleCrop()
                    .into(ivAvatar)
                btnAddCat.setOnClickListener {
                    findNavController().navigate(R.id.action_myProfileFragment_to_addCatFragment)
                }
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }
    }

    private fun setupCatAdapter(){
        val catAdapter = CatAdapter()

        binding.apply {
            viewModel.getCat().observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            val catData = result.data
                            catAdapter.submitList(catData)
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
                adapter = catAdapter
            }
        }
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.account -> {
                    findNavController().navigate(R.id.action_myProfileFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_myProfileFragment_to_aboutFragment)
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

    private fun setupNavigation() {
        binding.apply {
            bottomNavigationView.setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_home -> {
                        findNavController().navigate(R.id.action_myProfileFragment_to_homeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_service -> {
                        findNavController().navigate(R.id.action_myProfileFragment_to_servicesFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_pedigree -> {
                        findNavController().navigate(R.id.action_myProfileFragment_to_pedigreeFragment)
                        return@setOnItemSelectedListener true
                    }
                    R.id.menu_dating -> {
                        findNavController().navigate(R.id.action_myProfileFragment_to_datingFragment)
                        return@setOnItemSelectedListener true
                    }
                }
                return@setOnItemSelectedListener false
            }
            fab.setOnClickListener {
                findNavController().navigate(R.id.action_myProfileFragment_to_resultFragment)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    companion object {
        @StringRes
        private val TAB_ICONS =
            intArrayOf(R.drawable.ic_baseline_dashboard_24, R.drawable.ic_baseline_bookmark_black)
    }
}