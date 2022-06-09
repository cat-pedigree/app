package com.catpedigree.capstone.catpedigreebase.presentation.ui.classification

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.catpedigree.capstone.catpedigreebase.R
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentResultBinding
import com.catpedigree.capstone.catpedigreebase.ml.Model2
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.main.MainActivity
import com.catpedigree.capstone.catpedigreebase.presentation.ui.post.create.camera.CameraActivity
import com.catpedigree.capstone.catpedigreebase.utils.CameraUtils
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder


class ResultFragment : Fragment() {
    private lateinit var _binding: FragmentResultBinding
    private val binding get() = _binding
    private var currentFile: File? = null

    private lateinit var user: UserItems

    private val viewModel: ResultViewModel by viewModels {
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
        _binding = FragmentResultBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
        setupViewModel()
        setupMenu()
    }

    private fun setupAction(){
        showCard(true)
        binding.apply {
            btnScan.setOnClickListener {
                startCameraX()
            }
            btnTryAgain.setOnClickListener {
                showCard(true)
            }
        }
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
    }

    private fun setupMenu(){
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId){
                R.id.profile -> {
                    findNavController().navigate(R.id.action_resultFragment_to_myProfileFragment)
                    return@setOnMenuItemClickListener true
                }
                R.id.account -> {
                    findNavController().navigate(R.id.action_resultFragment_to_accountFragment)
                    true
                }
                R.id.language -> {
                    startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    true
                }
                R.id.about -> {
                    findNavController().navigate(R.id.action_resultFragment_to_aboutFragment)
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

    private fun checkAllPermissionsGranted() = MainActivity.REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            requireActivity().baseContext,
            it
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCameraX() {
        launcherIntentCameraX.launch(Intent(requireContext(), CameraActivity::class.java))
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
                showCard(false)
                ivPreview.setImageBitmap(result)
                ivPreviewCircle.setImageBitmap(result)
                catClassification(result)
            }
        }else if(it.resultCode == AppCompatActivity.RESULT_OK){
            val photoFile = it.data?.getSerializableExtra("photoFile") as File
            val imageSource = ImageDecoder.createSource(photoFile)
            var result = ImageDecoder.decodeBitmap(imageSource)
            result = result.copy(Bitmap.Config.ARGB_8888,true)
            binding.apply {
                showCard(false)
                ivPreview.setImageBitmap(result)
                ivPreviewCircle.setImageURI(Uri.fromFile(photoFile))
                catClassification(result)
                catClassification(result)
            }
        }
    }

    private fun catClassification(result : Bitmap){
        val labels = activity?.application?.assets?.open("labels.txt")?.bufferedReader().use{it?.readText()}?.split("\n")
        val catImage = Bitmap.createScaledBitmap(result, 150,150,true)
        val catModel = Model2.newInstance(requireContext())
        val inputFeature : TensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1,150,150,3), DataType.FLOAT32)
        val buffer: ByteBuffer = convertBitmapToByteBuffer(catImage)
        inputFeature.loadBuffer(buffer)

        val output = catModel.process(inputFeature)
        val outputFeature = output.outputFeature0AsTensorBuffer

        val max = getMax(outputFeature.floatArray)

        binding.apply {
            tvTitle.text = labels!![max]
            tvTitleBottom.text = labels[max]
            tvDescBottom.text = checkDescription(max)
            tvTitleBottom.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${tvTitleBottom.text}"))
                activity?.startActivity(intent)
            }
        }
        catModel.close()
    }

    private fun convertBitmapToByteBuffer(result: Bitmap): ByteBuffer{
        val imageByteBuffer = ByteBuffer.allocateDirect(getModelSize())
        imageByteBuffer.order(ByteOrder.nativeOrder())

        val pixels = IntArray(IMAGE_WIDTH * IMAGE_HEIGHT)
        result.getPixels(pixels, 0, result.width,0,0,result.width, result.height)

        var pixel = 0
        for(i in 0 until IMAGE_WIDTH){
            for(j in 0 until IMAGE_HEIGHT){
                val pixelVal = pixels[pixel++]

                imageByteBuffer.putFloat(((pixelVal shr 16 and 0xFF) - 127.5f) / 127.5f)
                imageByteBuffer.putFloat(((pixelVal shr 8 and 0xFF) - 127.5f) / 127.5f)
                imageByteBuffer.putFloat(((pixelVal and 0xFF) - 127.5f) / 127.5f)
            }
        }
        result.recycle()
        return imageByteBuffer
    }

    private fun getModelSize(): Int = FLOAT_SIZE * IMAGE_WIDTH * IMAGE_HEIGHT * CHANNEL_SIZE

    private fun getMax(arr:FloatArray) : Int{
        var min = 0.0f
        var max = 0
        for(i in 0..19)
        {
            if(arr[i] > min)
            {
                min = arr[i]
                max = i
            }
        }
        return max
    }

    private fun showCard(isCard: Boolean){
        when{
            isCard -> {
                binding.cardStart.visibility = View.VISIBLE
                binding.cardFinish.visibility = View.GONE
            }
            !isCard -> {
                binding.cardStart.visibility = View.GONE
                binding.cardFinish.visibility = View.VISIBLE
            }
        }
    }

    private fun checkDescription(number: Int) : String{
        var description = ""
        val menuDescription = resources.getStringArray(R.array.item_description)

            when (number) {
                0 -> {
                    description = menuDescription[0]
                }
                1 -> {
                    description = menuDescription[1]
                }
                2 -> {
                    description = menuDescription[2]
                }
                3 -> {
                    description = menuDescription[3]
                }
                4 -> {
                    description = menuDescription[4]
                }
                5 -> {
                    description = menuDescription[5]
                }
                6 -> {
                    description = menuDescription[6]
                }
                7 -> {
                    description = menuDescription[7]
                }
                8 -> {
                    description = menuDescription[8]
                }
                9 -> {
                    description = menuDescription[9]
                }
                10 -> {
                    description = menuDescription[10]
                }
                11 -> {
                    description = menuDescription[11]
                }
                12 -> {
                    description = menuDescription[12]
                }
                13 -> {
                    description = menuDescription[13]
                }
                14 -> {
                    description = menuDescription[14]
                }
                15 -> {
                    description = menuDescription[15]
                }
                16 -> {
                    description = menuDescription[16]
                }
                17 -> {
                    description = menuDescription[17]
                }
                18 -> {
                    description = menuDescription[18]
                }
                19 -> {
                    description = menuDescription[19]
                }

            }
        return description
    }

    private fun showLoading(isLoading: Boolean) {
        (if (isLoading) View.VISIBLE else View.INVISIBLE).also {
            binding.progressBar.visibility = it
        }
    }

    companion object{
        private const val FLOAT_SIZE = 4
        private const val IMAGE_WIDTH = 150
        private const val IMAGE_HEIGHT = 150
        private const val CHANNEL_SIZE = 3
    }
}