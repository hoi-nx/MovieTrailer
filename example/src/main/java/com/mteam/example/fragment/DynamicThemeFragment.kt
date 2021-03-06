package com.mteam.example.fragment

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.animation.doOnEnd
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.mteam.example.MainActivity
import com.mteam.example.R
import com.mteam.example.widget.CubicBezierInterpolator
import kotlinx.android.synthetic.main.dynamic_theme_fragment.*
import kotlin.math.max
import kotlin.math.sqrt


class DynamicThemeFragment : Fragment() {
    val pos = IntArray(2)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val inflater2 = inflater.cloneInContext(context);
        inflater2.factory2 = MyLayoutInflater((activity as MainActivity).delegate)

        return inflater2.inflate(R.layout.dynamic_theme_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vto = imageDrarkTheme.viewTreeObserver
        if (vto.isAlive) {
            vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    imageDrarkTheme.getLocationInWindow(pos)
                    pos[0] += imageDrarkTheme.measuredWidth / 2
                    pos[1] += imageDrarkTheme.measuredHeight / 2
                    if (Build.VERSION.SDK_INT < 16) {
                        vto.removeGlobalOnLayoutListener(this)
                    } else {
                        vto.removeOnGlobalLayoutListener(this)
                    }
                }
            })
        }
        imageDrarkTheme.setOnClickListener {
            val newTheme = when (ThemeManager.theme) {
                ThemeManager.Theme.DARK -> ThemeManager.Theme.LIGHT
                ThemeManager.Theme.LIGHT -> ThemeManager.Theme.DARK
            }
            setTheme(newTheme, animate = true)
        }
    }

    private fun setTheme(theme: ThemeManager.Theme, animate: Boolean = true) {
        if (!animate) {
            ThemeManager.theme = theme
            return
        }

        if (Build.VERSION.SDK_INT >= 21) {
            if (imageView.visibility == View.VISIBLE) {
                return
            }
            try {
//                var w = 0;
//                var h = 0;
//                val vto = container.viewTreeObserver
//                if (vto.isAlive) {
//                    vto.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//                        override fun onGlobalLayout() {
//                             w = container.measuredWidth
//                             h = container.measuredHeight
//                            if (Build.VERSION.SDK_INT < 16) {
//                                vto.removeGlobalOnLayoutListener(this)
//                            } else {
//                                vto.removeOnGlobalLayoutListener(this)
//                            }
//                        }
//                    })
//                }


                val w = container.measuredWidth
                val h = container.measuredHeight

                val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                container.draw(canvas)
                imageView.setImageBitmap(bitmap)
                imageView.visibility = View.VISIBLE
                val finalRadius = max(
                    sqrt(
                        (w - pos[0]) * (w - pos[0]) + (h - pos[1]) * (h - pos[1]).toDouble()
                    ),
                    sqrt(pos[0] * pos[0] + (h - pos[1]) * (h - pos[1]).toDouble())
                ).toFloat()
//        val finalRadius = hypot(w.toFloat(), h.toFloat())

                val anim =
                    ViewAnimationUtils.createCircularReveal(
                        imageView,
                        pos[0],
                        pos[1],
                        0f,
                        finalRadius
                    )
                anim.interpolator = CubicBezierInterpolator.easeInOutQuad
                anim.duration = 1000L
                anim.doOnEnd {
                    imageView.setImageDrawable(null)
                    imageView.visibility = View.GONE

                }
                anim.start()

            } catch (e: Exception) {

            }


        }
        ThemeManager.theme = theme
    }
}

class MyLayoutInflater(
    private val delegate: AppCompatDelegate
) : LayoutInflater.Factory2 {

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return when (name) {
            "TextView" -> MyTextView(context, attrs)
            "LinearLayout" -> MyLinearLayout(context, attrs)
            "Button" -> MyButton(context, attrs, R.attr.buttonStyle)
            else -> delegate.createView(parent, name, context, attrs)
        }
    }

    override fun onCreateView(name: String, context: Context, attrs: AttributeSet): View? {
        return onCreateView(null, name, context, attrs)
    }
}


object ThemeManager {

    private val listeners = mutableSetOf<ThemeChangedListener>()
    var theme = Theme.LIGHT
        set(value) {
            field = value
            listeners.forEach { listener -> listener.onThemeChanged(value) }
        }

    interface ThemeChangedListener {

        fun onThemeChanged(theme: Theme)
    }

    data class ButtonTheme(
        @ColorRes
        val backgroundTint: Int,
        @ColorRes
        val textColor: Int
    )

    data class TextViewTheme(
        @ColorRes
        val textColor: Int
    )

    data class ViewGroupTheme(
        @ColorRes
        val backgroundColor: Int
    )

    enum class Theme(
        val buttonTheme: ButtonTheme,
        val textViewTheme: TextViewTheme,
        val viewGroupTheme: ViewGroupTheme
    ) {
        DARK(
            buttonTheme = ButtonTheme(
                backgroundTint = android.R.color.holo_green_dark,
                textColor = android.R.color.white
            ),
            textViewTheme = TextViewTheme(
                textColor = android.R.color.white
            ),
            viewGroupTheme = ViewGroupTheme(
                backgroundColor = android.R.color.background_dark
            )
        ),
        LIGHT(
            buttonTheme = ButtonTheme(
                backgroundTint = android.R.color.holo_green_light,
                textColor = android.R.color.black
            ),
            textViewTheme = TextViewTheme(
                textColor = android.R.color.black
            ),
            viewGroupTheme = ViewGroupTheme(
                backgroundColor = android.R.color.background_light
            )
        )
    }

    fun addListener(listener: ThemeChangedListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ThemeChangedListener) {
        listeners.remove(listener)
    }
}

class MyTextView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr),
    ThemeManager.ThemeChangedListener {

    override fun onFinishInflate() {
        super.onFinishInflate()
        ThemeManager.addListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ThemeManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ThemeManager.removeListener(this)
    }

    override fun onThemeChanged(theme: ThemeManager.Theme) {
        setTextColor(ContextCompat.getColor(context, theme.textViewTheme.textColor))
    }
}

class MyLinearLayout
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr),
    ThemeManager.ThemeChangedListener {

    override fun onFinishInflate() {
        super.onFinishInflate()
        ThemeManager.addListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ThemeManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ThemeManager.removeListener(this)
    }

    override fun onThemeChanged(theme: ThemeManager.Theme) {
        setBackgroundColor(
            ContextCompat.getColor(
                context,
                theme.viewGroupTheme.backgroundColor
            )
        )
    }
}

class MyButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr),
    ThemeManager.ThemeChangedListener {

    override fun onFinishInflate() {
        super.onFinishInflate()
        ThemeManager.addListener(this)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        ThemeManager.addListener(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        ThemeManager.removeListener(this)
    }

    override fun onThemeChanged(theme: ThemeManager.Theme) {
        setTextColor(
            ContextCompat.getColor(
                context,
                theme.buttonTheme.textColor
            )
        )
        backgroundTintList =
            ColorStateList.valueOf(
                ContextCompat.getColor(
                    context,
                    theme.buttonTheme.backgroundTint
                )
            )
    }
}