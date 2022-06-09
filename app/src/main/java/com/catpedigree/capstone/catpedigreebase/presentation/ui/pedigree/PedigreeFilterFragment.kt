package com.catpedigree.capstone.catpedigreebase.presentation.ui.pedigree

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentPedigreeFilterBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.FilterAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class PedigreeFilterFragment : Fragment() {

    private lateinit var _binding: FragmentPedigreeFilterBinding
    private val binding get() = _binding
    private lateinit var user: UserItems
    private val args: PedigreeFilterFragmentArgs by navArgs()
    private var isBreedSelected: String? = null
    private var isColorSelected: String? = null

    private val viewModel: PedigreeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedigreeFilterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cat = args.cat

        val breed = resources.getStringArray(R.array.item_breed)
        val adapterBreed = ArrayAdapter(requireContext(), R.layout.item_dropdown, breed)
        binding.breed.setAdapter(adapterBreed)
        binding.breed.setText(getString(R.string.choose_breed), false)

        binding.breed.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                isBreedSelected = parent.getItemAtPosition(position).toString()
            }

        if(cat.gender == "Male"){
            val colorFemale = resources.getStringArray(R.array.item_color_female)
            val adapterColorFemale = ArrayAdapter(requireContext(), R.layout.item_dropdown, colorFemale)
            binding.color.setAdapter(adapterColorFemale)

            binding.color.onItemClickListener =
                AdapterView.OnItemClickListener { child, _, pos, _ ->
                    isColorSelected = child.getItemAtPosition(pos).toString()
                }
            binding.color.setAdapter(adapterColorFemale)
        }else{
            val colorMale = resources.getStringArray(R.array.item_color_male)
            val adapterColorMale = ArrayAdapter(requireContext(), R.layout.item_dropdown, colorMale)
            binding.color.setAdapter(adapterColorMale)
            binding.color.onItemClickListener =
                AdapterView.OnItemClickListener { child, _, pos, _ ->
                    isColorSelected = child.getItemAtPosition(pos).toString()
                }
            binding.color.setAdapter(adapterColorMale)
        }

        setupAction()
        setupViewModel()
        setupMenu()
    }

    private fun setupAction() {
        val cat = args.cat
        val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${cat.photo}"
        val filterAdapter = FilterAdapter(onCatSelected = {
            if(it.isSelected){
                viewModel.deleteSelectedCat(it)
            }else{
                viewModel.createSelectedCat(it)
            }
        })
        binding.apply {
            Glide.with(root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_avatar)
                .into(ivYourCat)

            val breed = isBreedSelected
            val color = isColorSelected
            val gender = cat.gender
            val isWhite = cat.isWhite

            btnFilter.setOnClickListener {
                    viewModel.getCatFilter(breed,color,gender,isWhite).observe(viewLifecycleOwner) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    progressBar.visibility = View.VISIBLE
                                }
                                is Result.Success -> {
                                    progressBar.visibility = View.GONE
                                    val catData = result.data
                                    filterAdapter.submitList(catData)
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
                    adapter = filterAdapter
                }

                rvCats.visibility = View.VISIBLE
                btnPedigree.visibility = View.VISIBLE
                autoCompleteTextBreed.visibility = View.GONE
                autocompleteTextColor.visibility = View.GONE
                btnFilter.visibility = View.GONE

            }

            btnPedigree.setOnClickListener {
                viewModel.getCatSelected().observe(viewLifecycleOwner){
                    val catOneName = cat.name
                    val catOnePhoto = cat.photo
                    val catOneGender = cat.gender
                    val catOneColor = cat.color
                    val catOneIsWhite = cat.isWhite
                    val catTwoName = it.name
                    val catTwoPhoto = it.photo
                    val catTwoGender = it.gender
                    val catTwoColor = it.color
                    val catTwoIsWhite = it.isWhite
                    findNavController().navigate(
                        PedigreeFilterFragmentDirections.actionPedigreeFilterFragmentToPedigreeResultFragment(
                            catOneName,
                            catOnePhoto,
                            catOneGender,
                            catOneColor,
                            catOneIsWhite,
                            catTwoName,
                            catTwoPhoto,
                            catTwoGender,
                            catTwoColor,
                            catTwoIsWhite,
                            cat
                        )
                    )
                }

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

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_pedigreeFilterFragment_to_myProfileFragment)
                    true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_pedigreeFilterFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_pedigreeFilterFragment_to_aboutFragment)
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