package ipca.pdm.pdmprojetofinal18476.ui.home

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import ipca.pdm.pdmprojetofinal18476.helpers.LightIndicatorStatus

class DailyCountLightIndicator : View {


    private var _status = LightIndicatorStatus.GOOD
    var status: LightIndicatorStatus
        get() {

            return _status
        }
        set(value) {
            _status = value

            invalidate()
        }


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val paint = Paint()
        if (status === LightIndicatorStatus.GOOD) {
            paint.color = Color.GREEN
        } else if (status === LightIndicatorStatus.BAD) {
            paint.color = Color.RED
        } else {
            paint.color = Color.YELLOW
        }
        canvas?.drawCircle(50f, 50f, 50f, paint)

    }
}