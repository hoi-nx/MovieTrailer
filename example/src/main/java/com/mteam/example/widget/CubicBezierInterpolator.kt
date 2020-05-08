package com.mteam.example.widget

import android.graphics.PointF
import android.view.animation.Interpolator

class CubicBezierInterpolator(start: PointF, end: PointF) :
    Interpolator {
     var start: PointF
     var end: PointF
     var a = PointF()
     var b = PointF()
     var c = PointF()

    constructor(
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float
    ) : this(PointF(startX, startY), PointF(endX, endY)) {
    }



    override fun getInterpolation(time: Float): Float {
        return getBezierCoordinateY(getXForTime(time))
    }

    protected fun getBezierCoordinateY(time: Float): Float {
        c.y = 3 * start.y
        b.y = 3 * (end.y - start.y) - c.y
        a.y = 1 - c.y - b.y
        return time * (c.y + time * (b.y + time * a.y))
    }

    protected fun getXForTime(time: Float): Float {
        var x = time
        var z: Float
        for (i in 1..13) {
            z = getBezierCoordinateX(x) - time
            if (Math.abs(z) < 1e-3) {
                break
            }
            x -= z / getXDerivate(x)
        }
        return x
    }

    private fun getXDerivate(t: Float): Float {
        return c.x + t * (2 * b.x + 3 * a.x * t)
    }

    private fun getBezierCoordinateX(time: Float): Float {
        c.x = 3 * start.x
        b.x = 3 * (end.x - start.x) - c.x
        a.x = 1 - c.x - b.x
        return time * (c.x + time * (b.x + time * a.x))
    }

    companion object {
        val DEFAULT = CubicBezierInterpolator(PointF(0.25f, 0.1f), PointF(0.25f,1f))
        val EASE_OUT = CubicBezierInterpolator(PointF(0f, 0f), PointF(0.58f,1f))
        val EASE_OUT_QUINT = CubicBezierInterpolator(PointF(.23f, 1f), PointF(0.32f,1f))
        val EASE_IN = CubicBezierInterpolator(PointF(0.42f, 0f), PointF(1f,1f))
        val EASE_BOTH = CubicBezierInterpolator(PointF(0.42f, 0f), PointF(.58f,1f))
        val easeOutSine: Interpolator =
            CubicBezierInterpolator(0.39f, 0.575f, 0.565f, 1f)
        val easeInOutSine: Interpolator =
            CubicBezierInterpolator(0.445f, 0.05f, 0.55f, 0.95f)

        // Quad
        val easeInQuad: Interpolator =
            CubicBezierInterpolator(0.55f, 0.085f, 0.68f, 0.53f)
        val easeOutQuad: Interpolator =
            CubicBezierInterpolator(0.25f, 0.46f, 0.45f, 0.94f)
        val easeInOutQuad: Interpolator =
            CubicBezierInterpolator(0.455f, 0.03f, 0.515f, 0.955f)
    }

    init {
        require(!(start.x < 0 || start.x > 1)) { "startX value must be in the range [0, 1]" }
        require(!(end.x < 0 || end.x > 1)) { "endX value must be in the range [0, 1]" }
        this.start = start
        this.end = end
    }
}