package com.bangkit.hargain.presentation.main.product.create

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.hargain.R
import com.bangkit.hargain.data.product.remote.dto.ProductCreateRequest
import com.bangkit.hargain.databinding.FragmentCreateProductBinding
import com.bangkit.hargain.domain.brand.entity.BrandEntity
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.presentation.common.extension.TAG
import com.bangkit.hargain.presentation.common.extension.gone
import com.bangkit.hargain.presentation.common.extension.showToast
import com.bangkit.hargain.presentation.common.helper.reduceFileImage
import com.bangkit.hargain.presentation.common.extension.visible
import com.bangkit.hargain.presentation.common.helper.rotateBitmap
import com.bangkit.hargain.presentation.common.helper.uriToFile
import com.bangkit.hargain.presentation.main.MainActivity
import com.bangkit.hargain.presentation.main.product.camera.CameraActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.net.URI
import java.util.*
import kotlin.collections.HashMap

@AndroidEntryPoint
class CreateProductFragment : Fragment() {
    private var _binding: FragmentCreateProductBinding? = null
    private val binding get() = _binding

    private val viewModel : CreateProductViewModel by viewModels()

    private var getFile: File? = null
    private lateinit var ImageUri : URI

    private lateinit var categoriesValue: MutableList<String>
    private lateinit var categoriesMap: HashMap<String, String>
    private lateinit var brandsValue: MutableList<String>
    private lateinit var brandsMap: HashMap<String, String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateProductBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNav: BottomNavigationView = (activity as MainActivity).getBottomNav()
        bottomNav.gone()

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        viewModel.getCategories()
        viewModel.getBrands()
        observe()

        binding?.cameraButton?.setOnClickListener { startCameraX() }
        binding?.galleryButton?.setOnClickListener { startGallery() }
        binding?.saveButton?.setOnClickListener{ saveProduct() }
    }

    private fun createCategoryDropdown(categories: List<CategoryEntity>) {
        categoriesMap = hashMapOf()
        categoriesValue = mutableListOf()
        categories.forEach {
            categoriesValue.add(it.name)
            categoriesMap.put(it.categoryId, it.name)
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_category, categoriesValue)
        binding?.categoryInput?.setAdapter(arrayAdapter)
    }

    private fun createBrandDropdown(brands: List<BrandEntity>) {
        brandsMap = hashMapOf()
        brandsValue = mutableListOf()
        brands.forEach {
            brandsValue.add(it.name)
            brandsMap.put(it.brandId, it.name)
        }
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_dropdown_category, brandsValue)
        binding?.brandInput?.setAdapter(arrayAdapter)
    }

    private fun saveProduct() {
        val title = binding?.nameInput?.text.toString().trim()

        val categoryValue = binding?.categoryInput?.text.toString()
        val categoryId = categoriesMap.filterValues { it == categoryValue }.keys.first()
        val brandValue = binding?.brandInput?.text.toString()
        val brandId = brandsMap.filterValues { it == brandValue }.keys.first()

        val description = binding?.descriptionInput?.text.toString().trim()
        val currentPrice = binding?.currentPriceInput?.text.toString().trim().toDouble()
        val cost = binding?.costInput?.text.toString().trim().toDouble()
        val startPrice = binding?.startPriceInput?.text.toString().trim().toDouble()
        val endPrice = binding?.endPriceInput?.text.toString().trim().toDouble()

        viewModel.createProduct(
            ProductCreateRequest(
                    title, description, brandId, categoryId, currentPrice,
                    cost, "ju", startPrice, endPrice
                )
        )
    }

    private fun observe(){
        viewModel.mState.flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state -> handleState(state) }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.mCategories
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { categories ->
                createCategoryDropdown(categories)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)

        viewModel.mBrands
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { brands ->
                createBrandDropdown(brands)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: CreateProductFragmentState){
        when(state){
            is CreateProductFragmentState.IsLoading -> handleLoading(state.isLoading)
            is CreateProductFragmentState.SuccessCreate -> {
//                setResultOkToPreviousFragment()
                findNavController().navigateUp()
            }
            is CreateProductFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is CreateProductFragmentState.Init -> Unit
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if(isLoading) {
            binding?.progressBar?.visible()
        } else {
            binding?.progressBar?.gone()
        }

        binding?.saveButton?.isEnabled = !isLoading
    }

    private fun saveImage(){
        val file = reduceFileImage(getFile as File)

        var imageUrl = ""
        val fileUri = Uri.fromFile(file)
        if (fileUri != null) {
            val fileName = UUID.randomUUID().toString() +".jpg"

            val refStorage = FirebaseStorage.getInstance().reference.child("images/$fileName")

            refStorage.putFile(fileUri)
                .addOnSuccessListener(
                    OnSuccessListener<UploadTask.TaskSnapshot> { taskSnapshot ->
                        taskSnapshot.storage.downloadUrl.addOnSuccessListener {
                            imageUrl = it.toString()
                        }
                    })
                ?.addOnFailureListener(OnFailureListener { e ->
                    print(e.message)
                })

            Log.w(TAG, imageUrl)
        }
    }

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
            val myFile = uriToFile(selectedImg, requireActivity().baseContext)//context di fragment masih belum ketemu solusinya
            getFile = myFile
            binding?.previewImageView?.setImageURI(selectedImg)
        }
    }


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        val bottomNav: BottomNavigationView = (requireActivity() as MainActivity).getBottomNav()
        bottomNav.gone()
    }

    override fun onStop() {
        super.onStop()

        val bottomNav: BottomNavigationView = (requireActivity() as MainActivity).getBottomNav()
        bottomNav.visible()
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireActivity().baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
