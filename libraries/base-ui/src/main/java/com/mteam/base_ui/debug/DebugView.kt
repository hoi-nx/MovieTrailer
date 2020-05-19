package com.mteam.base_ui.debug

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.widget.FrameLayout
import com.movie.appconfig.AppConfig
import okhttp3.OkHttpClient

class DebugView(
    context: Context,
    attrs: AttributeSet? = null,
    private val client: Lazy<OkHttpClient>,
    private val lumberYard: LumberYard,
    private val appConfig: AppConfig
) : FrameLayout(context, attrs) {

    companion object {
        private fun getDensityString(displayMetrics: DisplayMetrics): String {
            return when (val dpi = displayMetrics.densityDpi) {
                DisplayMetrics.DENSITY_LOW -> "ldpi"
                DisplayMetrics.DENSITY_MEDIUM -> "mdpi"
                DisplayMetrics.DENSITY_HIGH -> "hdpi"
                DisplayMetrics.DENSITY_XHIGH -> "xhdpi"
                DisplayMetrics.DENSITY_XXHIGH -> "xxhdpi"
                DisplayMetrics.DENSITY_XXXHIGH -> "xxxhdpi"
                DisplayMetrics.DENSITY_TV -> "tvdpi"
                else -> dpi.toString()
            }
        }

        private fun getSizeString(inputBytes: Long): String {
            var bytes = inputBytes
            val units = arrayOf("B", "KB", "MB", "GB")
            var unit = 0
            while (bytes >= 1024) {
                bytes /= 1024
                unit += 1
            }
            return bytes.toString() + units[unit]
        }
    }
}