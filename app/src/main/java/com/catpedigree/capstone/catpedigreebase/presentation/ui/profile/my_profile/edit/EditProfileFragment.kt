package com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.edit

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentEditProfileBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.main.MainActivity
import com.catpedigree.capstone.catpedigreebase.presentation.ui.profile.my_profile.edit.camera.CameraProfileActivity
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

class EditProfileFragment : Fragment() {

    private lateinit var _binding: FragmentEditProfileBinding
    private val binding get() = _binding
    private lateinit var user: UserItems
    private var currentFile: File? = null

    private val viewModel: EditProfileViewModel by viewModels {
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
        _binding = FragmentEditProfileBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){

        binding.apply {

            bioEditText.editText?.doOnTextChanged { text, _, _, _ ->
                when{
                    text!!.length > 60 -> {
                        bioEditText.error = getString(R.string.bio_length)
                    }
                    text.length <= 60 -> {
                        bioEditText.error = null
                    }
                }
            }

            btnConfirm.setOnClickListener {
                val name = nameEditText.editText?.text.toString().trim()
                val username = usernameEditText.editText?.text.toString().trim()
                val bio = bioEditText.editText?.text.toString().trim()

                when{
                    name.isEmpty() -> {
                        Snackbar.make(binding.nameEditText,R.string.name_required, Snackbar.LENGTH_LONG).show()
                    }
                    username.isEmpty() ->{
                        Snackbar.make(binding.usernameEditText,R.string.username_required, Snackbar.LENGTH_LONG).show()
                    }
                    bio.isEmpty() -> {
                        Snackbar.make(binding.bioEditText,R.string.bio_required, Snackbar.LENGTH_LONG).show()
                    }
                    bio.length > 60 -> {
                        Snackbar.make(binding.bioEditText,R.string.bio_length, Snackbar.LENGTH_LONG).show()
                    }else -> {
                    viewModel.profile(user.token ?: "", name, username,bio)
                }
                }
            }
        }

        binding.btnEditProfile.setOnClickListener {
            startCameraX()
        }

    }
    private fun setupViewModel(){
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
            val profilePhotoPath = "http://192.168.1.4/api-cat/public/storage/${userItems.profile_photo_path}"
            binding.apply {
                nameEditText.editText?.setText(userItems.name)
                usernameEditText.editText?.setText(userItems.username)
                bioEditText.editText?.setText(userItems.bio)
                Glide.with(binding.root)
                    .load(profilePhotoPath)
                    .signature(ObjectKey(profilePhotoPath))
                    .placeholder(R.drawable.ic_avatar)
                    .circleCrop()
                    .into(ivProfile)

            }
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(binding.btnEditProfile, "Profile changed successfully", Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }
    }

    private fun checkAllPermissionsGranted() = MainActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity().baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        launcherIntentCameraX.launch(Intent(requireContext(), CameraProfileActivity::class.java))
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        when (it.resultCode) {
            MainActivity.CAMERA_X_RESULT -> {
                val myFile = it.data?.getSerializableExtra("picture") as File
                val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
                val result =
                    CameraUtils.rotateBitmap(BitmapFactory.decodeFile(myFile.path), isBackCamera)

                val os: OutputStream = BufferedOutputStream(FileOutputStream(myFile))
                result.compress(Bitmap.CompressFormat.JPEG, 100, os)
                os.close()

                currentFile = myFile

                binding.ivProfile.setImageBitmap(result)
            }
            AppCompatActivity.RESULT_OK -> {
                val photoFile = it.data?.getSerializableExtra("photoFile") as File
                binding.ivProfile.setImageURI(Uri.fromFile(photoFile))
            }
        }
        val file = CameraUtils.reduceFileImage(currentFile as File)
        val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "profile_photo_path",
            file.nameWithoutExtension,
            requestImageFile
        )
        val slug = "profile-photos/${file.nameWithoutExtension}.${file.extension}"
        viewModel.change(user.token ?: "", imageMultipart,slug)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}