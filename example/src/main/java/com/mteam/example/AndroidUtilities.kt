/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */
package com.mteam.example

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.util.DisplayMetrics
import android.view.WindowManager

object AndroidUtilities {
    var statusBarHeight = 0
    var firstConfigurationWas = false
    var density = 1f
    var displaySize = Point()
    var roundMessageSize = 0
    var incorrectDisplaySizeFix = false
    var photoSize: Int? = null
    var displayMetrics = DisplayMetrics()
    var leftBaseline = 0
    var usingHardwareInput = false
    var isInMultiwindow = false

    init {
        checkDisplaySize(WatchlistApp.applicationContext!!, null)
    }


    fun getPixelsInCM(cm: Float, isX: Boolean): Float {
        return cm / 2.54f * if (isX) displayMetrics.xdpi else displayMetrics.ydpi
    }
    fun dp(value: Float): Int {
        return if (value == 0f) {
            0
        } else Math.ceil(density * value.toDouble()).toInt()
    }

    fun dpr(value: Float): Int {
        return if (value == 0f) {
            0
        } else Math.round(density * value)
    }

    fun dp2(value: Float): Int {
        return if (value == 0f) {
            0
        } else Math.floor(density * value.toDouble()).toInt()
    }

    fun compare(lhs: Int, rhs: Int): Int {
        if (lhs == rhs) {
            return 0
        } else if (lhs > rhs) {
            return 1
        }
        return -1
    }

    fun checkDisplaySize(context: Context, newConfiguration: Configuration?) {
        try {
            val oldDensity = density.toInt()
            density = context.resources.displayMetrics.density
            val newDensity = density.toInt()
//            if (firstConfigurationWas && oldDensity != newDensity) {
//                Theme.reloadAllResources(context)
//            }
            firstConfigurationWas = true
            var configuration = newConfiguration
            if (configuration == null) {
                configuration = context.resources.configuration
            }
            usingHardwareInput =
                configuration!!.keyboard != Configuration.KEYBOARD_NOKEYS && configuration.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO
            val manager =
                context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (manager != null) {
                val display = manager.defaultDisplay
                if (display != null) {
                    display.getMetrics(displayMetrics)
                    display.getSize(displaySize)
                }
            }
            if (configuration.screenWidthDp != Configuration.SCREEN_WIDTH_DP_UNDEFINED) {
                val newSize =
                    Math.ceil(configuration.screenWidthDp * density.toDouble()).toInt()
                if (Math.abs(displaySize.x - newSize) > 3) {
                    displaySize.x = newSize
                }
            }
            if (configuration.screenHeightDp != Configuration.SCREEN_HEIGHT_DP_UNDEFINED) {
                val newSize =
                    Math.ceil(configuration.screenHeightDp * density.toDouble()).toInt()
                if (Math.abs(displaySize.y - newSize) > 3) {
                    displaySize.y = newSize
                }
            }
            if (roundMessageSize == 0) {
//                roundMessageSize = if (AndroidUtilities.isTablet()) {
//                    (AndroidUtilities.getMinTabletSide() * 0.6f)
//                } else {
//                    (Math.min(
//                        displaySize.x,
//                        displaySize.y
//                    ) * 0.6f).toInt()
//                }
                roundMessageSize = (Math.min(
                    displaySize.x,
                    displaySize.y
                ) * 0.6f).toInt()
            }

        } catch (e: Exception) {
        }
    }
}