package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentCatUserBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.AlbumAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.view.CatProfileViewModel
import com.catpedigree.capstone.catpedigreebase.utils.Result
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
    }

    private fun setupAction() {
        val cat = args.cat
        val albumAdapter = AlbumAdapter()
        val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${cat.photo}"
        binding.apply {
            Glide.with(root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_avatar)
                .into(ivProfileCat)

            tvCatName.text = cat.name
            tvBreed.text = cat.breed
            tvColorName.text = cat.color
            tvGenderName.text = cat.gender
            tvStoryCat.text = cat.story
            topAppBar.title = "${cat.name} Profile"

            viewModel.getAlbum(cat.id!!).observe(viewLifecycleOwner) { result ->
                if (result != null) {
                    when (result) {
                        is Result.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }
                        is Result.Success -> {
                            progressBar.visibility = View.GONE
                            val albumData = result.data
                            albumAdapter.submitList(albumData)
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

            rvAlbum.apply {
                layoutManager = GridLayoutManager(requireContext(), 3)
                setHasFixedSize(true)
                isNestedScrollingEnabled = false
                adapter = albumAdapter
            }
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

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}