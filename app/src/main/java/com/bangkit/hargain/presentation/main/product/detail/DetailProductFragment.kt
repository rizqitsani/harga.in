package com.bangkit.hargain.presentation.main.product.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.hargain.R
import com.bangkit.hargain.data.product.remote.dto.PricePrediction
import com.bangkit.hargain.databinding.FragmentDetailProductBinding
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.extension.gone
import com.bangkit.hargain.presentation.common.extension.showToast
import com.bangkit.hargain.presentation.common.extension.visible
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailProductFragment : Fragment() {
    private lateinit var productId: String
    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding

    private val viewModel: DetailProductViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        productId = DetailProductFragmentArgs.fromBundle(arguments as Bundle).productId

        viewModel.fetchProductDetail(productId)
        observe()

        binding?.buttonBack?.setOnClickListener {
            findNavController().navigate(R.id.action_detailProductFragment_to_mainSearchFragment)
        }
        binding?.buttonDelete?.setOnClickListener {
            viewModel.deleteProduct(productId)
        }
    }

    private fun observe() {
        observeState()
        observeProductDetail()
    }

    private fun observeState() {
        viewModel.mState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                handleState(state)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun observeProductDetail() {
        viewModel.mProduct
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { product ->
                if (product !== null)
                    handleProductDetail(product)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleState(state: DetailProductFragmentState) {
        when (state) {
            is DetailProductFragmentState.IsLoading -> handleLoading(state.isLoading)
            is DetailProductFragmentState.ShowToast -> requireActivity().showToast(state.message)
            is DetailProductFragmentState.IsDeleteSuccess -> if (state.isDeleted) findNavController().navigate(
                R.id.action_detailProductFragment_to_mainSearchFragment
            )
            is DetailProductFragmentState.Init -> Unit
        }
    }

    private fun handleProductDetail(product: ProductEntity) {
        binding?.imageView?.let {
            Glide.with(this)
                .load(product.image)
                .override(80, 80)
                .centerCrop()
                .into(it)
        }

        binding?.productName?.text = product.title
        binding?.price?.text = product.currentPrice.toString()
        binding?.tvKategori?.text = product.categoryId
        binding?.tvDescription?.text = product.description
        binding?.tvMerk?.text = product.brandId
        binding?.sellingPriceTV?.text = product.currentPrice.toString()

    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visible()
            binding?.imageView?.gone()
            binding?.productName?.gone()
            binding?.price?.gone()
            binding?.layoutDescription?.gone()
        } else {
            binding?.progressBar?.gone()
            binding?.imageView?.visible()
            binding?.productName?.visible()
            binding?.price?.visible()
            binding?.layoutDescription?.visible()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}