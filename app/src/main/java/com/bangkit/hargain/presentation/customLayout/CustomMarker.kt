package com.bangkit.hargain.presentation.customLayout

import android.content.Context
import android.view.LayoutInflater
import com.bangkit.hargain.databinding.MarkerViewBinding
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF

class CustomMarker(context: Context, layoutResource: Int) : MarkerView(context, layoutResource) {

    private var binding: MarkerViewBinding =
        MarkerViewBinding.inflate(LayoutInflater.from(context), this, true)

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val y = entry?.y?.toDouble() ?: 0.0
        val x = entry?.x?.toDouble() ?: 0.0

        val yText = if(y.toString().length > 8){
            "Y: " + y.toString().substring(0,7)
        } else{
            "Y: $y"
        }

        val xText = if(x.toString().length > 8){
            "X: " + x.toString().substring(0,7)
        } else{
            "X: $x"
        }

        val resText = "$xText\n$yText"
        binding.tvPrice.text = resText
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}