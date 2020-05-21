package com.mteam.base_ui.debug

import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import android.os.Build.VERSION_CODES
import android.util.AttributeSet
import android.util.Log
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.widget.FrameLayout
import java.util.*

/**
 * Renders your view hierarchy as an interactive 3D visualization of layers.
 *
 *
 * Interactions supported:
 *
 *  * Single touch: controls the rotation of the model.
 *  * Two finger vertical pinch: Adjust zoom.
 *  * Two finger horizontal pinch: Adjust layer spacing.
 *
 */
class ScalpelFrameLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : FrameLayout(context, attrs, defStyle) {
    private class LayeredView {
        var view: View? = null
        var layer = 0
        operator fun set(view: View?, layer: Int) {
            this.view = view
            this.layer = layer
        }

        fun clear() {
            view = null
            layer = -1
        }
    }

    private val viewBoundsRect = Rect()
    private val viewBorderPaint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    private val camera = Camera()
    private val mMatrix = Matrix()
    private val location = IntArray(2)
    private val visibilities =
        BitSet(CHILD_COUNT_ESTIMATION)
    private val idNames = SparseArray<String?>()
    private val layeredViewQueue: Deque<LayeredView> = ArrayDeque()
    private val layeredViewPool: Pool<LayeredView> =
        object :
            Pool<LayeredView>(CHILD_COUNT_ESTIMATION) {
            override fun newObject(): LayeredView {
                return LayeredView()
            }
        }
    private val res: Resources
    private val density: Float
    private val slop: Float
    private val textOffset: Float
    private val textSize: Float
    private var mEnabled = false
    /** Returns true when view layers draw their contents.  */
    var isDrawingViews = true
        private set
    /** Returns true when view layers draw their IDs.  */
    var isDrawingIds = false
        private set
    private var pointerOne = MotionEvent.INVALID_POINTER_ID
    private var lastOneX = 0f
    private var lastOneY = 0f
    private var pointerTwo = MotionEvent.INVALID_POINTER_ID
    private var lastTwoX = 0f
    private var lastTwoY = 0f
    private var multiTouchTracking = TRACKING_UNKNOWN
    private var rotationYY = ROTATION_DEFAULT_Y.toFloat()
    private var rotationXX = ROTATION_DEFAULT_X.toFloat()
    private var zoom = ZOOM_DEFAULT
    private var spacing = SPACING_DEFAULT.toFloat()
    /** Get the view border chrome color.  */
    /** Set the view border chrome color.  */
    var chromeColor = 0
        set(color) {
            if (chromeColor != color) {
                viewBorderPaint.color = color
                field = color
                invalidate()
            }
        }
    /** Get the view border chrome shadow color.  */
    /** Set the view border chrome shadow color.  */
    var chromeShadowColor = 0
        set(color) {
            if (chromeShadowColor != color) {
                viewBorderPaint.setShadowLayer(1f, -1f, 1f, color)
                field = color
                invalidate()
            }
        }

    /** Returns true when 3D view layer interaction is enabled.  */
    /** Set whether or not the 3D view layer interaction is enabled.  */
    var isLayerInteractionEnabled: Boolean
        get() = mEnabled
        set(enabled) {
            if (this.mEnabled != enabled) {
                this.mEnabled = enabled
                setWillNotDraw(!enabled)
                invalidate()
            }
        }

    /** Set whether the view layers draw their contents. When false, only wireframes are shown.  */
    fun setDrawViews(drawViews: Boolean) {
        if (isDrawingViews != drawViews) {
            isDrawingViews = drawViews
            invalidate()
        }
    }

    /** Set whether the view layers draw their IDs.  */
    fun setDrawIds(drawIds: Boolean) {
        if (isDrawingIds != drawIds) {
            isDrawingIds = drawIds
            invalidate()
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mEnabled || super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mEnabled) {
            return super.onTouchEvent(event)
        }
        val action = event.actionMasked
        when (action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val index =
                    if (action == MotionEvent.ACTION_DOWN) 0 else event.actionIndex
                if (pointerOne == MotionEvent.INVALID_POINTER_ID) {
                    pointerOne = event.getPointerId(index)
                    lastOneX = event.getX(index)
                    lastOneY = event.getY(index)
                    if (DEBUG) log(
                        "Got pointer 1!  id: %s  x: %s  y: %s",
                        pointerOne,
                        lastOneY,
                        lastOneY
                    )
                } else if (pointerTwo == MotionEvent.INVALID_POINTER_ID) {
                    pointerTwo = event.getPointerId(index)
                    lastTwoX = event.getX(index)
                    lastTwoY = event.getY(index)
                    if (DEBUG) log(
                        "Got pointer 2!  id: %s  x: %s  y: %s",
                        pointerTwo,
                        lastTwoY,
                        lastTwoY
                    )
                } else {
                    if (DEBUG) log(
                        "Ignoring additional pointer.  id: %s",
                        event.getPointerId(index)
                    )
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (pointerTwo == MotionEvent.INVALID_POINTER_ID) {
                    // Single pointer controlling 3D rotation.
                    var i = 0
                    val count = event.pointerCount
                    while (i < count) {
                        if (pointerOne == event.getPointerId(i)) {
                            val eventX = event.getX(i)
                            val eventY = event.getY(i)
                            val dx = eventX - lastOneX
                            val dy = eventY - lastOneY
                            val drx = 90 * (dx / width)
                            val dry = 90 * (-dy / height) // Invert Y-axis.
                            // An 'x' delta affects 'y' rotation and vise versa.
                            rotationYY = Math.min(
                                Math.max(
                                    rotationYY + drx,
                                    ROTATION_MIN.toFloat()
                                ), ROTATION_MAX.toFloat()
                            )
                            rotationXX = Math.min(
                                Math.max(
                                    rotationXX + dry,
                                    ROTATION_MIN.toFloat()
                                ), ROTATION_MAX.toFloat()
                            )
                            if (DEBUG) {
                                log(
                                    "Single pointer moved (%s, %s) affecting rotation (%s, %s).",
                                    dx,
                                    dy,
                                    drx,
                                    dry
                                )
                            }
                            lastOneX = eventX
                            lastOneY = eventY
                            invalidate()
                        }
                        i++
                    }
                } else {
                    // We know there's two pointers and we only care about pointerOne and pointerTwo
                    val pointerOneIndex = event.findPointerIndex(pointerOne)
                    val pointerTwoIndex = event.findPointerIndex(pointerTwo)
                    val xOne = event.getX(pointerOneIndex)
                    val yOne = event.getY(pointerOneIndex)
                    val xTwo = event.getX(pointerTwoIndex)
                    val yTwo = event.getY(pointerTwoIndex)
                    val dxOne = xOne - lastOneX
                    val dyOne = yOne - lastOneY
                    val dxTwo = xTwo - lastTwoX
                    val dyTwo = yTwo - lastTwoY
                    if (multiTouchTracking == TRACKING_UNKNOWN) {
                        val adx =
                            Math.abs(dxOne) + Math.abs(dxTwo)
                        val ady =
                            Math.abs(dyOne) + Math.abs(dyTwo)
                        if (adx > slop * 2 || ady > slop * 2) {
                            multiTouchTracking = if (adx > ady) {
                                // Left/right movement wins. Track horizontal.
                                TRACKING_HORIZONTALLY
                            } else {
                                // Up/down movement wins. Track vertical.
                                TRACKING_VERTICALLY
                            }
                        }
                    }
                    if (multiTouchTracking == TRACKING_VERTICALLY) {
                        zoom += if (yOne >= yTwo) {
                            dyOne / height - dyTwo / height
                        } else {
                            dyTwo / height - dyOne / height
                        }
                        zoom = Math.min(
                            Math.max(
                                zoom,
                                ZOOM_MIN
                            ), ZOOM_MAX
                        )
                        invalidate()
                    } else if (multiTouchTracking == TRACKING_HORIZONTALLY) {
                        spacing += if (xOne >= xTwo) {
                            dxOne / width * SPACING_MAX - dxTwo / width * SPACING_MAX
                        } else {
                            dxTwo / width * SPACING_MAX - dxOne / width * SPACING_MAX
                        }
                        spacing = Math.min(
                            Math.max(
                                spacing,
                                SPACING_MIN.toFloat()
                            ), SPACING_MAX.toFloat()
                        )
                        invalidate()
                    }
                    if (multiTouchTracking != TRACKING_UNKNOWN) {
                        lastOneX = xOne
                        lastOneY = yOne
                        lastTwoX = xTwo
                        lastTwoY = yTwo
                    }
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val index =
                    if (action != MotionEvent.ACTION_POINTER_UP) 0 else event.actionIndex
                val pointerId = event.getPointerId(index)
                if (pointerOne == pointerId) {
                    // Shift pointer two (real or invalid) up to pointer one.
                    pointerOne = pointerTwo
                    lastOneX = lastTwoX
                    lastOneY = lastTwoY
                    if (DEBUG) log(
                        "Promoting pointer 2 (%s) to pointer 1.",
                        pointerTwo
                    )
                    // Clear pointer two and tracking.
                    pointerTwo = MotionEvent.INVALID_POINTER_ID
                    multiTouchTracking = TRACKING_UNKNOWN
                } else if (pointerTwo == pointerId) {
                    if (DEBUG) log(
                        "Lost pointer 2 (%s).",
                        pointerTwo
                    )
                    pointerTwo = MotionEvent.INVALID_POINTER_ID
                    multiTouchTracking = TRACKING_UNKNOWN
                }
            }
        }
        return true
    }

    override fun draw(canvas: Canvas) {
        if (!mEnabled) {
            super.draw(canvas)
            return
        }
        getLocationInWindow(location)
        val x = location[0].toFloat()
        val y = location[1].toFloat()
        val saveCount = canvas.save()
        val cx = width / 2f
        val cy = height / 2f
        camera.save()
        camera.rotate(rotationXX, rotationYY, 0f)
        camera.getMatrix(mMatrix)
        camera.restore()
        mMatrix.preTranslate(-cx, -cy)
        mMatrix.postTranslate(cx, cy)
        canvas.concat(mMatrix)
        canvas.scale(zoom, zoom, cx, cy)
        if (!layeredViewQueue.isEmpty()) {
            throw AssertionError("View queue is not empty.")
        }

        // We don't want to be rendered so seed the queue with our children.
        var i = 0
        val count = childCount
        while (i < count) {
            val layeredView = layeredViewPool.obtain()
            layeredView[getChildAt(i)] = 0
            layeredViewQueue.add(layeredView)
            i++
        }
        while (!layeredViewQueue.isEmpty()) {
            val layeredView = layeredViewQueue.removeFirst()
            val view = layeredView.view
            val layer = layeredView.layer

            // Restore the object to the pool for use later.
            layeredView.clear()
            layeredViewPool.restore(layeredView)

            // Hide any visible children.
            if (view is ViewGroup) {
                val viewGroup = view
                visibilities.clear()
                var i = 0
                val count = viewGroup.childCount
                while (i < count) {
                    val child = viewGroup.getChildAt(i)
                    if (child.visibility == View.VISIBLE) {
                        visibilities.set(i)
                        child.visibility = View.INVISIBLE
                    }
                    i++
                }
            }
            val viewSaveCount = canvas.save()

            // Scale the layer index translation by the rotation amount.
            val translateShowX = rotationYY / ROTATION_MAX
            val translateShowY = rotationXX / ROTATION_MAX
            val tx = layer * spacing * density * translateShowX
            val ty = layer * spacing * density * translateShowY
            canvas.translate(tx, -ty)
            view!!.getLocationInWindow(location)
            canvas.translate(location[0] - x, location[1] - y)
            viewBoundsRect[0, 0, view.width] = view.height
            canvas.drawRect(viewBoundsRect, viewBorderPaint)
            if (isDrawingViews) {
                view.draw(canvas)
            }
            if (isDrawingIds) {
                val id = view.id
                if (id != View.NO_ID) {
                    canvas.drawText(nameForId(id)!!, textOffset, textSize, viewBorderPaint)
                }
            }
            canvas.restoreToCount(viewSaveCount)

            // Restore any hidden children and queue them for later drawing.
            if (view is ViewGroup) {
                val viewGroup = view
                var i = 0
                val count = viewGroup.childCount
                while (i < count) {
                    if (visibilities[i]) {
                        val child = viewGroup.getChildAt(i)
                        child.visibility = View.VISIBLE
                        val childLayeredView = layeredViewPool.obtain()
                        childLayeredView[child] = layer + 1
                        layeredViewQueue.add(childLayeredView)
                    }
                    i++
                }
            }
        }
        canvas.restoreToCount(saveCount)
    }

    private fun nameForId(id: Int): String? {
        var name = idNames[id]
        if (name == null) {
            name = try {
                res.getResourceEntryName(id)
            } catch (e: Resources.NotFoundException) {
                String.format("0x%8x", id)
            }
            idNames.put(id, name)
        }
        return name
    }

    private abstract class Pool<T> internal constructor(initialSize: Int) {
        private val pool: Deque<T>
        fun obtain(): T {
            return if (pool.isEmpty()) newObject() else pool.removeLast()
        }

        fun restore(instance: T) {
            pool.addLast(instance)
        }

        protected abstract fun newObject(): T

        init {
            pool = ArrayDeque(initialSize)
            for (i in 0 until initialSize) {
                pool.addLast(newObject())
            }
        }
    }

    companion object {
        private const val TRACKING_UNKNOWN = 0
        private const val TRACKING_VERTICALLY = 1
        private const val TRACKING_HORIZONTALLY = -1
        private const val ROTATION_MAX = 60
        private const val ROTATION_MIN = -ROTATION_MAX
        private const val ROTATION_DEFAULT_X = -10
        private const val ROTATION_DEFAULT_Y = 15
        private const val ZOOM_DEFAULT = 0.6f
        private const val ZOOM_MIN = 0.33f
        private const val ZOOM_MAX = 2f
        private const val SPACING_DEFAULT = 25
        private const val SPACING_MIN = 10
        private const val SPACING_MAX = 100
        private const val CHROME_COLOR = -0x777778
        private const val CHROME_SHADOW_COLOR = -0x1000000
        private const val TEXT_OFFSET_DP = 2
        private const val TEXT_SIZE_DP = 10
        private const val CHILD_COUNT_ESTIMATION = 25
        private const val DEBUG = false
        private fun log(message: String, vararg args: Any) {
            Log.d("Scalpel", String.format(message, *args))
        }
    }

    init {
        res = context.resources
        density = context.resources.displayMetrics.density
        slop = ViewConfiguration.get(context).scaledTouchSlop.toFloat()
        textSize = TEXT_SIZE_DP * density
        textOffset = TEXT_OFFSET_DP * density
        chromeColor = CHROME_COLOR
        viewBorderPaint.style = Paint.Style.STROKE
        viewBorderPaint.textSize = textSize
        chromeShadowColor = CHROME_SHADOW_COLOR
        if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
            viewBorderPaint.typeface = Typeface.create("sans-serif-condensed", Typeface.NORMAL)
        }
    }
}