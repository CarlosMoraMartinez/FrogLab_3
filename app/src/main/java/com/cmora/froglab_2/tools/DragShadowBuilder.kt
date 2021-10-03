import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View

private class DragShadowBuilder(v: View) : View.DragShadowBuilder(v) {

    private val shadow = ColorDrawable(Color.LTGRAY)
    override fun onProvideShadowMetrics(size: Point, touch: Point) {
        Log.d("DragShadowBuilder", "onProvideShadowMetrics 1")
        val width: Int = view.width
        val height: Int = view.height
        shadow.setBounds(0, 0, width, height)
        size.set(width, height)
        touch.set(width / 2, height / 2)
        Log.d("DragShadowBuilder", "onProvideShadowMetrics, w=$width, h=$height ")
    }

    override fun onDrawShadow(canvas: Canvas) {
        Log.d("DragShadowBuilder", "onDrawShadow")
        shadow.draw(canvas)
    }
}
