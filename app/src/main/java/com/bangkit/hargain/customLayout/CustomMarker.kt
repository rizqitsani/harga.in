import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import kotlinx.android.synthetic.main.marker_view.view.*

class CustomMarker(context: Context, layoutResource: Int):  MarkerView(context, layoutResource) {

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        val y = entry?.y?.toDouble() ?: 0.0
        val x = entry?.x?.toDouble() ?: 0.0
        var yText = ""
        var xText = ""

        if(y.toString().length > 8){
            yText = "Y: " + y.toString().substring(0,7)
        }
        else{
            yText = "Y: $y"
        }

        if(x.toString().length > 8){
            xText = "X: " + x.toString().substring(0,7)
        }
        else{
            xText = "X: $x"
        }

        val resText = "$xText\n$yText"
        tvPrice.text = resText
        super.refreshContent(entry, highlight)
    }

    override fun getOffsetForDrawingAtPoint(xpos: Float, ypos: Float): MPPointF {
        return MPPointF(-width / 2f, -height - 10f)
    }
}