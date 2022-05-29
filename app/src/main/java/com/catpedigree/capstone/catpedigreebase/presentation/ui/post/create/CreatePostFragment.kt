package com.catpedigree.capstone.catpedigreebase.presentation.ui.post.create

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.signature.ObjectKey
import com.catpedigree.capstone.catpedigreebase.BuildConfig
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentCreatePostBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.main.MainActivity
import com.catpedigree.capstone.catpedigreebase.presentation.ui.post.create.camera.CameraActivity
import com.catpedigree.capstone.catpedigreebase.utils.CameraUtils
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class CreatePostFragment : Fragment() {

    private lateinit var _binding: FragmentCreatePostBinding
    private val binding get() = _binding

    private lateinit var user: UserItems
    private var currentFile: File? = null

    private val viewModel: CreatePostViewModel by viewModels {
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
        _binding = FragmentCreatePostBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupAction()
    }

    private fun setupViewModel(){
        viewModel.userItems.observe(viewLifecycleOwner) { userItems ->
            if (userItems?.isLoggedIn == false) {
                findNavController().navigateUp()
            }
            this.user = userItems
            val profilePhotoPath = "${BuildConfig.BASE_API_URL_PHOTO}${userItems.profile_photo_path}"
            binding.apply {
                tvName.text = userItems.name
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

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(binding.btnPost, R.string.post_success, Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
    }

    private fun setupAction(){
        binding.apply {
            btnPost.setOnClickListener {
                uploadFile()
            }

            ivCamera.setOnClickListener {
                startCameraX()
            }

            tvCamera.setOnClickListener {
                startCameraX()
            }

            ivGallery.setOnClickListener {
                startGallery()
            }

            tvGallery.setOnClickListener {
                startGallery()
            }
        }
    }

    private fun uploadFile() {
        binding.apply {
            val title = titleEditText.editText?.text.toString().trim()
            val description = postEditText.editText?.text.toString().trim()

            when{
                title.isEmpty() -> {
                    Snackbar.make(titleEditText,R.string.title_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                title.length > 100 ->{
                    Snackbar.make(postEditText,R.string.title_length, Snackbar.LENGTH_LONG).show()
                    return
                }
                description.isEmpty() -> {
                    Snackbar.make(postEditText,R.string.description_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                currentFile == null -> {
                    Snackbar.make(ivPreview,R.string.select_a_picture, Snackbar.LENGTH_LONG).show()
                    return
                }
                currentFile != null -> {
                    val file = CameraUtils.reduceFileImage(currentFile as File)
                    val desc = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    viewModel.uploadPost(user.token ?: "", imageMultipart,title, desc)
                }else -> {
                Snackbar.make(ivPreview,getString(R.string.select_a_picture), Snackbar.LENGTH_LONG).show()
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
        launcherIntentCameraX.launch(Intent(requireContext(), CameraActivity::class.java))
    }

    private fun startGallery() {
        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            type = "image/*"
        }
        launcherIntentGallery.launch(Intent.createChooser(intent, "Choose a picture!"))
    }

    private val launcherIntentGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                currentFile = CameraUtils.uriToFile(it.data?.data as Uri, requireContext())
                binding.apply {
                    ivPreview.visibility = View.VISIBLE
                    ivPreview.setImageURI(Uri.fromFile(currentFile))
                }
            }
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

            binding.apply {
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageBitmap(result)
            }
        }else if(it.resultCode == AppCompatActivity.RESULT_OK){
            val photoFile = it.data?.getSerializableExtra("photoFile") as File
            binding.apply {
                ivPreview.visibility = View.VISIBLE
                ivPreview.setImageURI(Uri.fromFile(photoFile))
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}