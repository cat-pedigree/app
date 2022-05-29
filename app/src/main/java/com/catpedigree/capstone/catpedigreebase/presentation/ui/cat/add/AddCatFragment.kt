package com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.add

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentAddCatBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.camera.CameraCatActivity
import com.catpedigree.capstone.catpedigreebase.presentation.ui.main.MainActivity
import com.catpedigree.capstone.catpedigreebase.utils.CameraUtils
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class AddCatFragment : Fragment() {

    private lateinit var _binding: FragmentAddCatBinding
    private val binding get() = _binding

    private lateinit var user: UserItems
    private var currentFile: File? = null

    private var isGenderSelected:String? = null
    private var isBreedSelected: String? = null

    private val viewModel: AddCatViewModel by viewModels {
        ViewModelFactory.getInstance(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!checkAllPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                MainActivity.REQUIRED_PERMISSIONS,
                MainActivity.REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCatBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val breed = resources.getStringArray(R.array.item_breed)
        val adapterBreed = ArrayAdapter(requireContext(), R.layout.item_dropdown, breed)
        binding.breed.setAdapter(adapterBreed)
        binding.breed.setText(getString(R.string.choose_breed),false)

        binding.breed.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            isBreedSelected = parent.getItemAtPosition(position).toString()
        }

        val gender = resources.getStringArray(R.array.item_gender)
        val adapterGender = ArrayAdapter(requireContext(), R.layout.item_dropdown, gender)
        binding.gender.setAdapter(adapterGender)
        binding.gender.setText(getString(R.string.choose_gender),false)

        binding.gender.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            isGenderSelected = parent.getItemAtPosition(position).toString()
        }

        setupViewModel()
        setupAction()
    }

    private fun setupViewModel(){
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

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(binding.btnConfirm, R.string.cat_success, Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun setupAction(){
        binding.apply {
            ivPhoto.setOnClickListener {
                startCameraX()
            }
            btnConfirm.setOnClickListener { 
                uploadCat()
            }
        }
    }

    private fun uploadCat() {
        binding.apply {
            val userId = user.id
            val name = editTextCatName.editText?.text.toString().trim()
            val gender = autoCompleteTextGender.editText?.text.toString().trim()
            val color = editTextColor.editText?.text.toString().trim()
            val weight = editTextWeight.editText?.text.toString().trim()
            val age = editTextAge.editText?.text.toString().trim()
            val story = editTextStory.editText?.text.toString().trim()
            when{
                name.isEmpty() -> {
                    Snackbar.make(editTextCatName,R.string.title_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                gender.isEmpty() -> {
                    Snackbar.make(autoCompleteTextBreed,R.string.color_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                color.isEmpty() -> {
                    Snackbar.make(editTextColor,R.string.color_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                weight.isEmpty() -> {
                    Snackbar.make(editTextWeight,R.string.weight_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                age.isEmpty() -> {
                    Snackbar.make(editTextAge,R.string.age_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                story.isEmpty() -> {
                    Snackbar.make(editTextStory,R.string.story_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                currentFile == null -> {
                    Snackbar.make(ivPhoto,R.string.select_a_picture, Snackbar.LENGTH_LONG).show()
                    return
                }
                currentFile != null -> {
                    val file = CameraUtils.reduceFileImage(currentFile as File)
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    if (userId != null) {
                        viewModel.uploadCat(user.token ?: "", userId,name,isBreedSelected!!,isGenderSelected!!,color,weight.toDouble(),age.toInt(),story, imageMultipart)
                    }
                }
            else -> {
                    Snackbar.make(ivPhoto,getString(R.string.select_a_picture), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun checkAllPermissionsGranted() = MainActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity().baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        launcherIntentCameraX.launch(Intent(requireContext(), CameraCatActivity::class.java))
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == MainActivity.CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result =
                CameraUtils.rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)

            val os: OutputStream = BufferedOutputStream(FileOutputStream(myFile))
            result.compress(Bitmap.CompressFormat.JPEG, 100, os)
            os.close()

            currentFile = myFile

            binding.ivPhoto.setImageBitmap(result)

        }else if(it.resultCode == AppCompatActivity.RESULT_OK){
            val photoFile = it.data?.getSerializableExtra("photoFile") as File
            currentFile = photoFile
            binding.ivPhoto.setImageURI(Uri.fromFile(photoFile))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

}