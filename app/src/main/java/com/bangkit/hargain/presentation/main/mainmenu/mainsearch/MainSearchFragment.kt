package com.bangkit.hargain.presentation.main.mainmenu.mainsearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.hargain.R
import com.bangkit.hargain.databinding.FragmentMainSearchBinding
import com.bangkit.hargain.domain.category.entity.CategoryEntity
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.extension.gone
import com.bangkit.hargain.presentation.common.extension.showToast
import com.bangkit.hargain.presentation.common.extension.visible
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class MainSearchFragment : Fragment()  {

    private var _binding: FragmentMainSearchBinding? = null
    private val binding get() = _binding

    private val viewModel: MainSearchViewModel by viewModels()

    private lateinit var productsAdapter: ProductAdapter

    private var searchQuery: String = ""
    private var categoryIdQuery: String = ""
    private var categoryNameArgs: String = ""

    private lateinit var categoriesOriginal: List<CategoryEntity>

    private var categoriesValue: MutableList<String> = mutableListOf()
    private lateinit var categoriesValueArray: Array<String>
    private var checkedCategoryIndex = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        _binding = FragmentMainSearchBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        categoryIdQuery = MainSearchFragmentArgs.fromBundle(arguments as Bundle).categoryId
        categoryNameArgs = MainSearchFragmentArgs.fromBundle(arguments as Bundle).categoryName

        setupRecyclerView()

        viewModel.getCategories()

        if (categoryIdQuery.isEmpty()) {
            viewModel.fetchProducts()
        } else {
            viewModel.searchProducts(searchQuery, categoryIdQuery)
        }

        observe()

        val searchView = binding?.SearchViewProduct as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchQuery = query as String
                viewModel.searchProducts(searchQuery, categoryIdQuery)

                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText == ""){
                    viewModel.fetchProducts()
                }
                return true
            }
        })

        binding?.createProductFab?.setOnClickListener {
            findNavController().navigate(R.id.action_mainSearchFragment_to_createProductFragment)
        }
        binding?.filterButton?.setOnClickListener {
            showCategoryFilterDialog()
        }
    }

    private fun showCategoryFilterDialog() {
        if(categoryNameArgs.isNotEmpty()) {
            categoriesValueArray.forEachIndexed { index, s ->
                if(s == categoryNameArgs) {
                    checkedCategoryIndex = index
                }
            }
        }
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Category")
            .setNeutralButton("Cancel") { _, _ ->
                // Respond to neutral button press
            }
            .setPositiveButton("Ok") { _, _ ->
                categoryIdQuery = if (categoriesValueArray[checkedCategoryIndex] == "Default") {
                    ""
                } else {
                    val categoryIdFilter = categoriesOriginal.firstOrNull {
                        it.name == categoriesValueArray[checkedCategoryIndex]
                    }?.categoryId
                    categoryIdFilter ?: ""
                }

                viewModel.searchProducts(searchQuery, categoryIdQuery)
            }
            .setSingleChoiceItems(categoriesValueArray, checkedCategoryIndex) { _, which ->
                checkedCategoryIndex = which
            }
            .show()
    }

    private fun observe() {
        observeState()
        observeProducts()
        observeCategories()
    }

    private fun observeState() {
        viewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeProducts() {
        viewModel.mProducts
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { products ->
                handleProducts(products)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeCategories() {
        viewModel.mCategories
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { categories ->
                handleCategories(categories)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: MainSearchFragmentState) {
        when (state) {
            is MainSearchFragmentState.IsLoading -> handleLoading(state.isLoading)
            is MainSearchFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is MainSearchFragmentState.Init -> Unit
        }
    }

    private fun handleProducts(products: List<ProductEntity>) {
        binding?.recyclerViewProduct?.adapter.let {
            if (it is ProductAdapter) {
                it.setProducts(products)
            }
            if(products.isEmpty()) {
                binding?.notFoundTextView?.visible()
            } else {
                binding?.notFoundTextView?.gone()
            }
        }
    }

    private fun handleCategories(categories: List<CategoryEntity>) {
        this.categoriesOriginal = categories

        categoriesValue.add("Default")
        categories.forEach {
            categoriesValue.add(it.name)
        }

        categoriesValueArray = categoriesValue.toTypedArray()
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visible()
            binding?.notFoundTextView?.gone()
        } else {
            binding?.progressBar?.gone()
        }
    }

    private fun setupRecyclerView() {
        productsAdapter = ProductAdapter(mutableListOf())

        productsAdapter.setOnItemTapListener(object : ProductAdapter.OnItemTap {
            override fun onTap(product: ProductEntity) {
                val bundle = bundleOf("productId" to product.id)
                findNavController().navigate(
                    R.id.action_mainSearchFragment_to_detailProductFragment,
                    bundle
                )
            }
        })

        binding?.recyclerViewProduct?.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = productsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }


}