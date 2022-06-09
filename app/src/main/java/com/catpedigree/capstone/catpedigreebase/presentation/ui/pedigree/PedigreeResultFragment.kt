package com.catpedigree.capstone.catpedigreebase.presentation.ui.pedigree

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentPedigreeResultBinding
import com.catpedigree.capstone.catpedigreebase.ml.ModelPedigreeFemale4
import com.catpedigree.capstone.catpedigreebase.ml.PedigreeMaleModelTflite3
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class PedigreeResultFragment : Fragment() {

    private lateinit var _binding: FragmentPedigreeResultBinding
    private val binding get() = _binding
    private lateinit var user: UserItems
    val args: PedigreeResultFragmentArgs by navArgs()


    private val viewModel: PedigreeViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPedigreeResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAction()
        setupViewModel()
//        setupMenu()
//        setupNavigation()
    }

    @SuppressLint("SetTextI18n")
    private fun setupAction() {
        val cat = args.cat
        val catOneName = args.catOneName
        val catOnePhoto = args.catOnePhoto
        val catOneGender = args.catOneGender
        var catOneColor = args.catOneColor
        val catOneIsWhite = args.catOneIsWhite
        val catTwoName = args.catTwoName
        val catTwoPhoto = args.catTwoPhoto
        val catTwoGender = args.catTwoGender
        var catTwoColor = args.catTwoColor
        val catTwoIsWhite = args.catTwoIsWhite
        val profilePhotoOne = "${BuildConfig.BASE_API_PHOTO}${catOnePhoto}"
        val profilePhotoTwo = "${BuildConfig.BASE_API_PHOTO}${catTwoPhoto}"

        when(catOneColor){
            "Black" -> {
                catOneColor = 1.toString()
            }
            "Grey" -> {
                catOneColor = 2.toString()
            }
            "Orange" -> {
                catOneColor = 3.toString()
            }
            "Silver" -> {
                catOneColor = 4.toString()
            }
            "Black Tortie" -> {
                catOneColor = 5.toString()
            }
            "Grey Tortie" -> {
                catOneColor = 6.toString()
            }
        }

        when(catTwoColor){
            "Black" -> {
                catTwoColor = 1.toString()
            }
            "Grey" -> {
                catTwoColor = 2.toString()
            }
            "Orange" -> {
                catTwoColor = 3.toString()
            }
            "Silver" -> {
                catTwoColor = 4.toString()
            }
            "Black Tortie" -> {
                catTwoColor = 5.toString()
            }
            "Grey Tortie" -> {
                catTwoColor = 6.toString()
            }
        }

        val inputPedigree = intArrayOf(catOneColor.toInt(), catOneIsWhite, catTwoColor.toInt(), catTwoIsWhite)

        if(catOneGender == "male"){
            binding.apply {
                Glide.with(root)
                    .load(profilePhotoOne)
                    .signature(ObjectKey(profilePhotoOne))
                    .placeholder(R.drawable.ic_avatar)
                    .into(imgAvatarCatOne)

                Glide.with(root)
                    .load(profilePhotoTwo)
                    .signature(ObjectKey(profilePhotoTwo))
                    .placeholder(R.drawable.ic_avatar)
                    .into(imgAvatarCatTwo)

                tvGenderCatOne.text = catOneName
                tvGenderCatTwo.text = catTwoName
            }
            predictColorFemale(inputPedigree)
            predictColorMale(inputPedigree)
        }else if(catOneGender == "female"){
            binding.apply {
                Glide.with(root)
                    .load(catOnePhoto)
                    .signature(ObjectKey(catOnePhoto))
                    .placeholder(R.drawable.ic_loading)
                    .into(imgAvatarCatOne)

                Glide.with(root)
                    .load(catTwoPhoto)
                    .signature(ObjectKey(catTwoPhoto))
                    .placeholder(R.drawable.ic_loading)
                    .into(imgAvatarCatTwo)

                tvGenderCatOne.text = catOneName
                tvGenderCatTwo.text = catTwoName
            }
            predictColorMale(inputPedigree)
            predictColorFemale(inputPedigree)
        }

        binding.btnRetry.setOnClickListener {
            findNavController().navigate(R.id.action_pedigreeResultFragment_to_pedigreeFragment)
            viewModel.deleteSelectedCat(cat)
        }
    }

    private fun predictColorFemale(inputArray: IntArray){
        val labels = activity?.application?.assets?.open("labels_pedigree.txt")?.bufferedReader().use{it?.readText()}?.split("\n")

        val model = ModelPedigreeFemale4.newInstance(requireContext())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 4), DataType.FLOAT32)
        inputFeature0.loadArray(inputArray)
        val outputs = model.process(inputFeature0)

        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val max = getMax(outputFeature0.floatArray)

        binding.tvResultFemale.text = labels!![max]
        model.close()
    }

    private fun predictColorMale(inputArray: IntArray){
        val labels = activity?.application?.assets?.open("labels_pedigree_male.txt")?.bufferedReader().use{it?.readText()}?.split("\n")

        val model = PedigreeMaleModelTflite3.newInstance(requireContext())

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 4), DataType.FLOAT32)
        inputFeature0.loadArray(inputArray)
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        val max = getMaxMale(outputFeature0.floatArray)

        binding.tvResultMale.text = labels!![max]
        model.close()
    }

    private fun setupViewModel() {
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
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

//
//    private fun showLoading(isLoading: Boolean) {
//        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
//    }

    private fun getMax(arr:FloatArray) : Int{
        var min = 0.0f
        var max = 0
        for(i in 0..5)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                max = i
            }
        }
        return max
    }

    private fun getMaxMale(arr:FloatArray) : Int{
        var min = 0.0f
        var max = 0
        for(i in 0..3)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                max = i
            }
        }
        return max
    }
}