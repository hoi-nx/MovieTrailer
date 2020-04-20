package com.mteam.base_ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.core.content.res.use
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.mteam.base_ui.R

class MaterialShapeConstraintLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : CheckableConstraintLayout(context, attrs, defStyle) {

    init {
        context.obtainStyledAttributes(attrs, R.styleable.MaterialShapeConstraintLayout).use { ta ->
            background = MaterialShapeDrawable.createWithElevationOverlay(context, elevation).apply {
                fillColor = ta.getColorStateList(R.styleable.MaterialShapeConstraintLayout_materialBackgroundColor)

                val topLeft = ta.getDimensionPixelSize(R.styleable.MaterialShapeConstraintLayout_materialBackgroundTopLeftRadius, 0)
                if (topLeft > 0) {
                    shapeAppearanceModel.setTopLeftCorner(CornerFamily.ROUNDED, topLeft)
//                    shapeAppearanceModel.topLeftCornerSize = MaterialShapeUtils.createCornerTreatment(CornerFamily.ROUNDED, topLeft)
                }
                val topRight = ta.getDimensionPixelSize(
                        R.styleable.MaterialShapeConstraintLayout_materialBackgroundTopRightRadius, 0)
                if (topRight > 0) {
                    shapeAppearanceModel.setTopRightCorner(CornerFamily.ROUNDED, topRight)
                }
            }
        }
    }

    override fun draw(canvas: Canvas?) {
        updateElevationRelativeToParentSurface()
        super.draw(canvas)
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)

        if (background is MaterialShapeDrawable) {
            background.elevation = elevation
            background.translationZ = translationZ
        }
    }

    override fun setElevation(elevation: Float) {
        super.setElevation(elevation)

        val bg = background
        if (bg is MaterialShapeDrawable) {
            bg.elevation = elevation
        }
    }

    override fun setTranslationZ(translationZ: Float) {
        super.setTranslationZ(translationZ)

        val bg = background
        if (bg is MaterialShapeDrawable) {
            bg.translationZ = translationZ
        }
    }

    private fun updateElevationRelativeToParentSurface() {
        val bg = background
        if (bg is MaterialShapeDrawable) {
            var v = parent
            var cumulativeElevation = elevation

            // Iterate through our parents, until we find a view with a MaterialShapeDrawable
            // background (the 'parent surface'). We then update our background, based on the
            // 'parent surface's elevation
            while (v is View) {
                val vBg = v.background
                if (vBg is MaterialShapeDrawable) {
                    bg.elevation = vBg.elevation.coerceAtLeast(v.elevation) + cumulativeElevation
                    break
                } else {
                    cumulativeElevation += v.elevation
                }

                v = v.getParent()
            }
        }
    }
}