package com.bangkit.hargain.presentation.main.product.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.hargain.R
import com.bangkit.hargain.databinding.FragmentDetailProductBinding
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.extension.gone
import com.bangkit.hargain.presentation.common.extension.showToast
import com.bangkit.hargain.presentation.common.extension.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class DetailProductFragment : Fragment() {

    private var _binding: FragmentDetailProductBinding? = null
    private val binding get() = _binding

    private val viewModel: DetailProductViewModel by viewModels()

    private val productId = "Tyt0EFSoW5vMTTg0tMk6"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailProductBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        TODO: add safe arg on nav graph
//        val productId = DetailProductFragmentArgs.fromBundle(arguments as Bundle).productId

        viewModel.fetchProductDetail(productId)
        observe()

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
//        TODO: load image
//        Glide.with(this)
//            .load(product.avatarUrl)
//            .apply(RequestOptions().override(550, 550))
//            .into(binding?.imgAvatar as CircleImageView)

        binding?.productName?.text = product.title
        binding?.price?.text = product.optimalPrice.toString()
        binding?.tvKategori?.text = product.categoryId
        binding?.tvDescription?.text = product.description
        binding?.tvMerk?.text = product.brandId
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}