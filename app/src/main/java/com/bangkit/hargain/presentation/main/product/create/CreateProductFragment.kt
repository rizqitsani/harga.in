package com.bangkit.hargain.presentation.main.product.create

import android.Manifest
import android.R
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bangkit.hargain.databinding.FragmentCreateProductBinding
import com.bangkit.hargain.presentation.common.extension.gone
import com.bangkit.hargain.presentation.common.helper.rotateBitmap
import com.bangkit.hargain.presentation.common.helper.uriToFile
import com.bangkit.hargain.presentation.main.MainActivity
import com.bangkit.hargain.presentation.main.product.camera.CameraActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File


class CreateProductFragment : Fragment() {
    private var _binding: FragmentCreateProductBinding? = null
    private val binding get() = _binding
    private var getFile: File? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateProductBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav: BottomNavigationView = (requireActivity() as MainActivity).getBottomNav()
        bottomNav.gone()

        binding?.cameraButton?.setOnClickListener { startCameraX() }
        binding?.galleryButton?.setOnClickListener { startGallery() }
    }

    //CameraLauncher
    private fun startCameraX() {
        val intent = Intent(activity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            binding?.previewImageView?.setImageBitmap(result)
        }
    }

    //GalleryLauncher
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireActivity())//context di fragment masih belum ketemu solusinya
            getFile = myFile
            binding?.previewImageView?.setImageURI(selectedImg)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}