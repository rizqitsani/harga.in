package com.bangkit.hargain.presentation.main.product.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bangkit.hargain.R
import com.bangkit.hargain.presentation.customLayout.CustomMarker
import com.bangkit.hargain.data.product.remote.dto.PricePrediction
import com.bangkit.hargain.databinding.FragmentDetailProductBinding
import com.bangkit.hargain.domain.product.entity.ProductEntity
import com.bangkit.hargain.presentation.common.extension.gone
import com.bangkit.hargain.presentation.common.extension.showToast
import com.bangkit.hargain.presentation.common.extension.visible
import com.bangkit.hargain.presentation.common.helper.formatCurrency
import com.bangkit.hargain.presentation.main.MainActivity
import com.bumptech.glide.Glide
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.bottomnavigation.BottomNavigationView
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
                if (product !== null) {
                    handleProductDetail(product)
                    binding?.buttonEdit?.setOnClickListener {
                        val bundle = bundleOf("product" to product)
                        findNavController().navigate(
                            R.id.action_detailProductFragment_to_updateProductFragment,
                            bundle
                        )
                    }
                }


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
                .centerCrop()
                .into(it)
        }

        binding?.productName?.text = product.title
        binding?.price?.text = resources.getString(R.string.product_price, formatCurrency(product.optimalPrice))
        binding?.tvKategori?.text = product.categoryName
        binding?.tvDescription?.text = product.description
        binding?.tvMerk?.text = product.brandName
        binding?.tvCurrentPrice?.text = resources.getString(R.string.product_price, formatCurrency(product. currentPrice))

        setupSalesPredictionChart(product.pricePredictions)
        setupProfitPredictionChart(product.pricePredictions)

    }

    private fun setupSalesPredictionChart(pricePredictions: List<PricePrediction>) {
        //Part1
        val entries = ArrayList<Entry>()
        val lineChart: LineChart = binding!!.lineChartSales

        //Part2
        pricePredictions.forEach {
            entries.add(Entry(it.sellingPrice.toFloat(), it.totalSales.toFloat()))
        }

        //Part3
        val vl = LineDataSet(entries, getString(R.string.total_sales))

        //Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
//        vl.fillColor = R.color.neutral_60
//        vl.color = R.color.neutral_60
//        vl.fillAlpha = R.color.red
//        vl.fillColor = R.color.red

        //Part5
        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Part6
        lineChart.data = LineData(vl)

        //Part7
        lineChart.axisRight.isEnabled = false
//        lineChart.xAxis.axisMaximum = j+0.1f

        //Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

        //Part9
        lineChart.description.text = getString(R.string.selling_price)
        lineChart.setNoDataText(getString(R.string.no_prediction_yet))

        //Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

        //Part11
        val markerView = CustomMarker(requireContext(), R.layout.marker_view)
        lineChart.marker = markerView
    }

    private fun setupProfitPredictionChart(pricePredictions: List<PricePrediction>) {
        //Part1
        val entries = ArrayList<Entry>()
        val lineChart: LineChart = binding!!.lineChartProfit

        //Part2
        pricePredictions.forEach {
            entries.add(Entry(it.sellingPrice.toFloat(), it.totalProfit.toFloat()))
        }

        //Part3
        val vl = LineDataSet(entries, getString(R.string.total_profit))

        //Part4
        vl.setDrawValues(false)
        vl.setDrawFilled(true)
        vl.lineWidth = 3f
//        vl.fillColor = R.color.neutral_60
//        vl.color = R.color.neutral_60
//        vl.fillAlpha = R.color.red
//        vl.fillColor = R.color.red

        //Part5
        lineChart.xAxis.labelRotationAngle = 0f
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        //Part6
        lineChart.data = LineData(vl)

        //Part7
        lineChart.axisRight.isEnabled = false
//        lineChart.xAxis.axisMaximum = j+0.1f

        //Part8
        lineChart.setTouchEnabled(true)
        lineChart.setPinchZoom(true)

        //Part9
        lineChart.description.text = getString(R.string.selling_price)
        lineChart.setNoDataText(getString(R.string.no_prediction_yet))

        //Part10
        lineChart.animateX(1800, Easing.EaseInExpo)

        //Part11
        val markerView = CustomMarker(requireContext(), R.layout.marker_view)
        lineChart.marker = markerView
    }

    private fun handleLoading(isLoading: Boolean) {
        if (isLoading) {
            binding?.progressBar?.visible()
            binding?.imageView?.gone()
            binding?.productName?.gone()
            binding?.price?.gone()
            binding?.layoutDescription?.gone()
            binding?.salesPredictionTextView?.gone()
            binding?.salesPrediction?.gone()
            binding?.profitPredictionTextView?.gone()
            binding?.profitPrediction?.gone()
            binding?.buttonEdit?.gone()
        } else {
            binding?.progressBar?.gone()
            binding?.imageView?.visible()
            binding?.productName?.visible()
            binding?.price?.visible()
            binding?.layoutDescription?.visible()
            binding?.salesPredictionTextView?.visible()
            binding?.salesPrediction?.visible()
            binding?.profitPredictionTextView?.visible()
            binding?.profitPrediction?.visible()
            binding?.buttonEdit?.visible()
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val bottomNav: BottomNavigationView = (requireActivity() as MainActivity).getBottomNav()
        bottomNav.gone()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        val bottomNav: BottomNavigationView = (requireActivity() as MainActivity).getBottomNav()
        bottomNav.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}