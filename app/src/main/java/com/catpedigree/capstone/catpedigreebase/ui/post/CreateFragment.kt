package com.catpedigree.capstone.catpedigreebase.ui.post

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentCreateBinding
import com.catpedigree.capstone.catpedigreebase.databinding.ModalChooseImageBinding
import com.catpedigree.capstone.catpedigreebase.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.ui.camera.CameraActivity
import com.catpedigree.capstone.catpedigreebase.ui.main.MainActivity
import com.catpedigree.capstone.catpedigreebase.ui.maps.MapsFragment
import com.catpedigree.capstone.catpedigreebase.utils.CameraUtils
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.google.android.gms.maps.model.LatLng
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class CreateFragment : Fragment() {

    private var _binding: FragmentCreateBinding? = null
    private val binding get() = _binding!!
    private var currentFile: File? = null
    private var latLng: LatLng? = null
    private lateinit var user: UserItems

    private val viewModel: CreateViewModel by viewModels {
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
        _binding = FragmentCreateBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if (currentFile != null) binding.ivPreview.setImageURI(Uri.fromFile(currentFile))
        if (latLng != null) binding.tvLocation.text =
            getString(R.string.maps_lat_lon_format, latLng!!.latitude, latLng!!.longitude)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
    }

    private fun setupAction(){
        binding.uploadButton.setOnClickListener {
            uploadFile()
        }

        binding.textBtnLocation.setOnClickListener {
            findNavController().navigate(
                CreateFragmentDirections.actionCreateFragmentToMapsFragment(
                    MapsFragment.ACTION_PICK_LOCATION
                )
            )
        }

        binding.btnDeleteLocation.setOnClickListener {
            if (latLng != null) {
                latLng = null
                binding.tvLocation.text = getString(R.string.no_location_selected)
            }
        }

        setFragmentResultListener(MapsFragment.KEY_RESULT) { _, bundle ->
            val location = bundle.getParcelable(MapsFragment.KEY_LAT_LONG) as LatLng?
            if (location != null) {
                binding.tvLocation.text =
                    getString(R.string.maps_lat_lon_format, location.latitude, location.longitude)
                latLng = location
            }
        }

        binding.tvLocation.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(text: Editable?) {
                binding.btnDeleteLocation.isGone = text.isNullOrEmpty() || latLng == null
            }
        })

        setupPopupMenu()
    }

    private fun setupViewModel() {
        viewModel.userItem.observe(viewLifecycleOwner) { userItem ->
            this.user = userItem
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { state ->
            showLoading(state)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            ToastUtils.showToast(requireContext(), message)
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                findNavController().navigateUp()
            }
        }
    }

    private fun setupPopupMenu() {
        val popupBinding = ModalChooseImageBinding.inflate(layoutInflater)
        val popupWindow = PopupWindow(
            popupBinding.root,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
            isFocusable = true
            elevation = 10F
        }

        popupBinding.btnCamera.setOnClickListener {
            startCameraX()
            popupWindow.dismiss()
        }

        popupBinding.btnGallery.setOnClickListener {
            startGallery()
            popupWindow.dismiss()
        }

        binding.ivCamera.setOnClickListener { btn ->
            popupWindow.showAsDropDown(btn)
        }
    }

    private fun uploadFile() {
        val description = binding.postEditText.text.toString().trim()

        if (description.isEmpty()) {
            binding.postEditText.error = getString(R.string.description_required)
            return
        }

        if (currentFile == null) {
            ToastUtils.showToast(requireContext(), getString(R.string.select_a_picture))
            return
        }

        if (currentFile != null) {
            val file = CameraUtils.reduceFileImage(currentFile as File)

            val desc = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            viewModel.uploadImage(user.token ?: "", imageMultipart, desc, latLng)

        } else {
            ToastUtils.showToast(requireContext(), getString(R.string.select_a_picture))
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
                binding.ivPreview.setImageURI(Uri.fromFile(currentFile))
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

            binding.ivPreview.setImageBitmap(result)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}