package com.catpedigree.capstone.catpedigreebase.presentation.ui.pedigree

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentPedigreeBinding
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentPedigreeFilterBinding
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.FilterAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.adapter.PedigreeAdapter
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.view.CatProfileFragmentArgs
import com.catpedigree.capstone.catpedigreebase.utils.Result
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils

class PedigreeFilterFragment : Fragment() {

    private lateinit var _binding: FragmentPedigreeFilterBinding
    private val binding get() = _binding
    private lateinit var user: UserItems
    private var isBreedSelected: String? = null
    private val args: PedigreeFilterFragmentArgs by navArgs()

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

        val breed = resources.getStringArray(R.array.item_breed)
        val adapterBreed = ArrayAdapter(requireContext(), R.layout.item_dropdown, breed)
        binding.breed.setAdapter(adapterBreed)
        binding.breed.setText(getString(R.string.choose_breed), false)

        binding.breed.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                isBreedSelected = parent.getItemAtPosition(position).toString()
            }

        setupAction()
        setupViewModel()
//        setupMenu()
//        setupNavigation()
    }

    private fun setupAction() {
        val cat = args.cat
        val profilePhotoPath = "${BuildConfig.BASE_API_PHOTO}${cat.photo}"
        val filterAdapter = FilterAdapter()
        binding.apply {
            Glide.with(root)
                .load(profilePhotoPath)
                .signature(ObjectKey(profilePhotoPath))
                .placeholder(R.drawable.ic_avatar)
                .into(ivYourCat)

            val breed = isBreedSelected
            val color = editTextColor.editText?.text.toString()
            val eyeColor = editTextEyeColor.editText?.text.toString()
            val hairColor = editTextHairColor.editText?.text.toString()
            val earShape = editTextEarShape.editText?.text.toString()

            btnFilter.setOnClickListener {
                    viewModel.getCatFilter(breed,color,eyeColor,hairColor,earShape).observe(viewLifecycleOwner) { result ->
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
                autoCompleteTextBreed.visibility = View.INVISIBLE
                editTextColor.visibility = View.INVISIBLE
                editTextEyeColor.visibility = View.INVISIBLE
                editTextHairColor.visibility = View.INVISIBLE
                editTextEarShape.visibility = View.INVISIBLE
                btnFilter.visibility = View.INVISIBLE

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