package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentCatUserBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.view.CatProfileViewModel
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class CatUserFragment : Fragment() {

    private lateinit var _binding: FragmentCatUserBinding
    private val binding get() = _binding

    private val args: CatUserFragmentArgs by navArgs()

    private lateinit var user: UserItems

    private val viewModel: CatProfileViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCatUserBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
        setupMenu()
    }

    private fun setupAction() {
        val cat = args.cat
        val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${cat.photo}"
        binding.apply {
            Glide.with(root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_loading)
                .into(ivProfileCat)

            tvCatName.text = cat.name
            tvBreed.text = cat.breed
            tvMonth.text = cat.age.toString()
            tvColorName.text = cat.color
            tvGenderName.text = cat.gender
            tvEyeColor.text = cat.eye_color
            tvHairColor.text = cat.hair_color
            tvEarShape.text = cat.ear_shape
            tvStoryCat.text = cat.story
            topAppBar.title = cat.name
        }

    }

    private fun setupViewModel() {
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

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_catUserFragment_to_myProfileFragment)
                    return@setOnMenuItemClickListener true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_catUserFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_catUserFragment_to_aboutFragment)
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}