package com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.add

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentAddCatBinding
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.cat.camera.CameraCatActivity
import com.catpedigree.capstone.catpedigreebase.presentation.ui.main.MainActivity
import com.catpedigree.capstone.catpedigreebase.presentation.ui.maps.MapsFragment
import com.catpedigree.capstone.catpedigreebase.utils.CameraUtils
import com.catpedigree.capstone.catpedigreebase.utils.ToastUtils
import com.google.android.gms.maps.model.LatLng
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
import java.util.*

class AddCatFragment : Fragment() {

    private lateinit var _binding: FragmentAddCatBinding
    private val binding get() = _binding

    private lateinit var user: UserItems
    private var currentFile: File? = null

    private var isGenderSelected: String? = null
    private var isBreedSelected: String? = null

    private var latLng: LatLng? = null

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
        _binding = FragmentAddCatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        if(currentFile != null) binding.ivPhoto.setImageURI(Uri.fromFile(currentFile))
        if(latLng != null) binding.tvLocationSelected.text =
            getString(R.string.maps_lat_lon_format, latLng!!.latitude, latLng!!.longitude)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val breed = resources.getStringArray(R.array.item_breed)
        val adapterBreed = ArrayAdapter(requireContext(), R.layout.item_dropdown, breed)
        val gender = resources.getStringArray(R.array.item_gender)
        val adapterGender = ArrayAdapter(requireContext(), R.layout.item_dropdown, gender)

        binding.breed.setAdapter(adapterBreed)
        binding.breed.setText(getString(R.string.choose_breed), false)

        binding.breed.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                isBreedSelected = parent.getItemAtPosition(position).toString()
            }
        binding.gender.setAdapter(adapterGender)
        binding.gender.setText(getString(R.string.choose_gender), false)

        binding.gender.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                isGenderSelected = parent.getItemAtPosition(position).toString()
            }
        setupViewModel()
        setupAction()
        setupMenu()
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

        viewModel.isSuccess.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                Snackbar.make(binding.btnConfirm, R.string.cat_success, Snackbar.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupAction() {
        binding.apply {
            ivPhoto.setOnClickListener {
                startCameraX()
            }
            btnConfirm.setOnClickListener {
                uploadCat()
            }

            btnChange.setOnClickListener {
                findNavController().navigate(
                    AddCatFragmentDirections.actionAddCatFragmentToMapsFragment(
                        MapsFragment.ACTION_PICK_LOCATION
                    )
                )
            }

            setFragmentResultListener(MapsFragment.KEY_RESULT){_, bundle ->
                val location = bundle.getParcelable(MapsFragment.KEY_LAT_LONG) as LatLng?
                val addresses: MutableList<Address>
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                if(location != null){
                    addresses = geocoder.getFromLocation(location.latitude,location.longitude,1)
                    tvAddress.text = "${addresses[0].getAddressLine(0)} ${addresses[0].locality}"
                    tvAddress.visibility = View.VISIBLE
                    tvLocationSelected.text = getString(R.string.maps_lat_lon_format, location.latitude, location.longitude)
                    latLng = location
                }
            }

        }
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_addCatFragment_to_myProfileFragment)
                    return@setOnMenuItemClickListener true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_addCatFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_addCatFragment_to_aboutFragment)
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

    private fun uploadCat() {
        binding.apply {
            val userId = user.id
            val name = editTextCatName.editText?.text.toString()
            val gender = autoCompleteTextGender.editText?.text.toString()
            val color = editTextColor.editText?.text.toString()
            val eyeColor = editTextEyeColor.editText?.text.toString()
            val hairColor = editTextHairColor.editText?.text.toString()
            val earShape = editTextEarShape.editText?.text.toString()
            val weight = editTextWeight.editText?.text.toString()
            val age = editTextAge.editText?.text.toString()
            when {
                name.isEmpty() -> {
                    Snackbar.make(editTextCatName, R.string.title_required, Snackbar.LENGTH_LONG)
                        .show()
                    return
                }
                gender.isEmpty() -> {
                    Snackbar.make(
                        autoCompleteTextBreed,
                        R.string.color_required,
                        Snackbar.LENGTH_LONG
                    ).show()
                    return
                }
                color.isEmpty() -> {
                    Snackbar.make(editTextColor, R.string.color_required, Snackbar.LENGTH_LONG)
                        .show()
                    return
                }
                eyeColor.isEmpty() -> {
                    Snackbar.make(editTextEyeColor, R.string.eye_color_required, Snackbar.LENGTH_LONG)
                        .show()
                    return
                }
                hairColor.isEmpty() -> {
                    Snackbar.make(editTextHairColor, R.string.hair_color_required, Snackbar.LENGTH_LONG)
                        .show()
                    return
                }
                earShape.isEmpty() -> {
                    Snackbar.make(editTextEarShape, R.string.ear_shape_required, Snackbar.LENGTH_LONG)
                        .show()
                    return
                }
                weight.isEmpty() -> {
                    Snackbar.make(editTextWeight, R.string.weight_required, Snackbar.LENGTH_LONG)
                        .show()
                    return
                }
                age.isEmpty() -> {
                    Snackbar.make(editTextAge, R.string.age_required, Snackbar.LENGTH_LONG).show()
                    return
                }
                currentFile == null -> {
                    Snackbar.make(ivPhoto, R.string.select_a_picture, Snackbar.LENGTH_LONG).show()
                    return
                }
                currentFile != null -> {
                    val file = CameraUtils.reduceFileImage(currentFile as File)
                    val nameCat = name.toRequestBody("text/plain".toMediaType())
                    val breedCat = isBreedSelected?.toRequestBody("text/plain".toMediaType())
                    val genderCat = isGenderSelected?.toRequestBody("text/plain".toMediaType())
                    val colorCat = color.toRequestBody("text/plain".toMediaType())
                    val eyeColorCat = eyeColor.toRequestBody("text/plain".toMediaType())
                    val hairColorCat = hairColor.toRequestBody("text/plain".toMediaType())
                    val earShapeCat = earShape.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    if (userId != null) {
                        viewModel.uploadCat(
                            user.token ?: "",
                            userId,
                            nameCat,
                            breedCat!!,
                            genderCat!!,
                            colorCat,
                            eyeColorCat,
                            hairColorCat,
                            earShapeCat,
                            weight.toDouble(),
                            age.toInt(),
                            imageMultipart,
                            latLng
                        )
                    }
                }
                else -> {
                    Snackbar.make(
                        ivPhoto,
                        getString(R.string.select_a_picture),
                        Snackbar.LENGTH_LONG
                    ).show()
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

        } else if (it.resultCode == AppCompatActivity.RESULT_OK) {
            val photoFile = it.data?.getSerializableExtra("photoFile") as File
            currentFile = photoFile
            binding.ivPhoto.setImageURI(Uri.fromFile(photoFile))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }
}