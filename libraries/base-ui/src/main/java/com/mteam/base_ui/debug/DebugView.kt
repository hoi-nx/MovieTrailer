package com.mteam.base_ui.debug

import android.content.Context
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.movie.appconfig.AppConfig
import com.mteam.base_ui.databinding.DebugViewContentBinding
import okhttp3.OkHttpClient

class DebugView(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    fun onDrawerOpened() {

    }

    private val binding = DebugViewContentBinding.inflate(LayoutInflater.from(context), this, true)

    internal val icon = binding.debugIcon
    private val networkDelayView = binding.debugNetworkDelay
    private val networkVarianceView = binding.debugNetworkVariance
    private val networkErrorView = binding.debugNetworkError
    private val enableMockModeView = binding.debugEnableMockMode
    private val uiAnimationSpeedView = binding.debugUiAnimationSpeed
    private val uiPixelGridView = binding.debugUiPixelGrid
    private val uiPixelRatioView = binding.debugUiPixelRatio
    private val uiScalpelView = binding.debugUiScalpel
    private val uiScalpelWireframeView = binding.debugUiScalpelWireframe
    private val buildNameView = binding.debugBuildName
    private val buildCodeView = binding.debugBuildCode
    private val buildDateView = binding.debugBuildDate
    private val deviceMakeView = binding.debugDeviceMake
    private val deviceModelView = binding.debugDeviceModel
    private val deviceResolutionView = binding.debugDeviceResolution
    private val deviceDensityView = binding.debugDeviceDensity
    private val deviceReleaseView = binding.debugDeviceRelease
    private val deviceApiView = binding.debugDeviceApi
    private val okHttpCacheMaxSizeView = binding.debugOkhttpCacheMaxSize
    private val okHttpCacheWriteErrorView = binding.debugOkhttpCacheWriteError
    private val okHttpCacheRequestCountView = binding.debugOkhttpCacheRequestCount
    private val okHttpCacheNetworkCountView = binding.debugOkhttpCacheNetworkCount
    private val okHttpCacheHitCountView = binding.debugOkhttpCacheHitCount
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