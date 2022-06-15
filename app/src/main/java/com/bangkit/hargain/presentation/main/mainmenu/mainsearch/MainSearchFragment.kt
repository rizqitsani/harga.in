package com.bangkit.hargain.presentation.main.mainmenu.mainsearch

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat.getSystemService
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
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.extension.gone
import com.bangkit.hargain.presentation.common.extension.showToast
import com.bangkit.hargain.presentation.common.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
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
        binding?.createProductFab?.setOnClickListener {
            findNavController().navigate(R.id.action_mainSearchFragment_to_createProductFragment)
        }

        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        setupRecyclerView()
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

        viewModel.fetchProducts()

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
    }


    private fun observe() {
        observeState()
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

    private fun observeCategories() {
        viewModel.mProducts
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { products ->
                handleProducts(products)
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
        }
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visible()
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