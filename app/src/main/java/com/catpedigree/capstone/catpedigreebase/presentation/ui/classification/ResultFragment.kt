package com.catpedigree.capstone.catpedigreebase.presentation.ui.classification

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
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
import com.catpedigree.capstone.catpedigreebase.data.network.item.UserItems
import com.catpedigree.capstone.catpedigreebase.databinding.FragmentResultBinding
import com.catpedigree.capstone.catpedigreebase.ml.Model2
import com.catpedigree.capstone.catpedigreebase.presentation.factory.ViewModelFactory
import com.catpedigree.capstone.catpedigreebase.presentation.ui.main.MainActivity
import com.catpedigree.capstone.catpedigreebase.presentation.ui.post.create.camera.CameraActivity
import com.catpedigree.capstone.catpedigreebase.utils.CameraUtils
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.nio.ByteBuffer


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
//                ivPreview.setImageURI(Uri.fromFile(photoFile))
                ivPreview.setImageBitmap(result)
                ivPreviewCircle.setImageURI(Uri.fromFile(photoFile))
                catClassification(result)
            }
        }
    }

    private fun catClassification(result : Bitmap){
        val labels = activity?.application?.assets?.open("labels.txt")?.bufferedReader().use{it?.readText()}?.split("\n")
        val catImage = Bitmap.createScaledBitmap(result, 150,150,true)
        val catModel = Model2.newInstance(requireContext())
        val inputFeature : TensorBuffer = TensorBuffer.createFixedSize(intArrayOf(1,150,150,3), DataType.FLOAT32)
        val imageFeature = TensorImage(DataType.FLOAT32)

        imageFeature.load(catImage)

        val buffer: ByteBuffer = imageFeature.buffer

        inputFeature.loadBuffer(buffer)

        val output = catModel.process(inputFeature)
        val outputFeature = output.outputFeature0AsTensorBuffer


        val max = getMax(outputFeature.floatArray)

        binding.apply {
            tvTitle.text = labels!![max]
            tvTitleBottom.text = labels[max]
            tvDescBottom.text = labels[max]
            tvTitleBottom.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=${tvTitleBottom.text}"))
                activity?.startActivity(intent)
            }
        }

        catModel.close()

    }

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
        when (number) {
            19 -> {
                description = "Kucing domestik berbulu pendek adalah kucing keturunan campuran yang memiliki bulu berukuran pendek. Karena kucing keturunan campuran, maka kucing ini tidak diakui sebagai ras kucing. Di Britania Raya, kucing ini sering disebut sebagai mogi."
            }
        }
        return description
    }
}