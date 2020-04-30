package com.mteam.example.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.Rect
import android.media.MediaPlayer
import android.os.Build
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.animation.DecelerateInterpolator
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.Keep
import androidx.fragment.app.Fragment
import com.mteam.example.AndroidUtilities
import com.mteam.example.R
import com.mteam.example.WatchlistApp
import java.util.*

class PipVideoView {
    private var windowView: FrameLayout? = null
    private var controlsView: View? = null
    private var videoWidth = 0
    private var videoHeight = 0

    private var windowLayoutParams: WindowManager.LayoutParams? = null
    private var windowManager: WindowManager? = null
    private var preferences: SharedPreferences? = null
    private var decelerateInterpolator: DecelerateInterpolator? = null

    private var activity: Activity? = null
    private val movieView: MovieView? = null

    val actionBarHeight = with(TypedValue().also {
        WatchlistApp.applicationContext!!.theme.resolveAttribute(
            android.R.attr.actionBarSize,
            it,
            true
        )
    }) {
        TypedValue.complexToDimensionPixelSize(
            this.data,
            WatchlistApp.applicationContext!!.resources.displayMetrics
        )
    }

    inner class MiniControlsView(context: Context) : FrameLayout(context) {
        private val progressPaint: Paint? = null
        private val progressInnerPaint: Paint? = null
        private var isVisible = true
        private var currentAnimation: AnimatorSet? = null
        private val inlineButton: ImageView? = null
        private val playButton: ImageView? = null
        internal var progress = 0f
        var isCompleted = false
        var bufferedPosition = 0f
        private val hideRunnable = Runnable { show(false, true) }

        private val progressRunnable: Runnable = object : Runnable {
            override fun run() {
                if (activity == null) {
                    return
                }
                movieView?.let {
                    val mediaPlayer = movieView.mMediaPlayer
                    if (mediaPlayer != null) {
                        setProgress(mediaPlayer.currentPosition / mediaPlayer.duration as Float)
                    }
                }

            }
        }

        fun setProgress(value: Float) {
            progress = value
            invalidate()
        }

        fun setBufferedProgress(position: Float) {
            bufferedPosition = position
            invalidate()
        }

        fun updatePlayButton() {
            movieView?.let {
                val videoPlayer: MediaPlayer = it.mMediaPlayer ?: return
                removeCallbacks(hideRunnable)
                if (!videoPlayer.isPlaying()) {
                    if (isCompleted) {
                        playButton!!.setImageResource(R.drawable.ic_info_24dp)
                    } else {
                        playButton!!.setImageResource(R.drawable.ic_play_arrow_24dp)
                    }
                } else {
                    playButton!!.setImageResource(R.drawable.ic_pause_24dp)
                    activity?.let {
                        it.runOnUiThread(hideRunnable)
                    }
                }
            }

        }

        fun show(value: Boolean, animated: Boolean) {
            if (isVisible == value) {
                return
            }
            isVisible = value
            if (currentAnimation != null) {
                currentAnimation?.cancel()
            }
            if (isVisible) {
                if (animated) {
                    currentAnimation = AnimatorSet()
                    currentAnimation?.playTogether(ObjectAnimator.ofFloat(this, "alpha", 1.0f))
                    currentAnimation?.setDuration(150)
                    currentAnimation?.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animator: Animator) {
                            currentAnimation = null
                        }
                    })
                    currentAnimation?.start()
                } else {
                    alpha = 1.0f
                }
            } else {
                if (animated) {
                    currentAnimation = AnimatorSet()
                    currentAnimation?.playTogether(ObjectAnimator.ofFloat(this, "alpha", 0.0f))
                    currentAnimation?.setDuration(150)
                    currentAnimation?.addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animator: Animator) {
                            currentAnimation = null
                        }
                    })
                    currentAnimation?.start()
                } else {
                    alpha = 0.0f
                }
            }
            checkNeedHide()
        }

        private fun checkNeedHide() {
            removeCallbacks(hideRunnable)
            if (isVisible) {
                activity?.let {
                    it.runOnUiThread(hideRunnable)
                }
            }
        }

        override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                if (!isVisible) {
                    show(true, true)
                    return true
                } else {
                    checkNeedHide()
                }
            }
            return super.onInterceptTouchEvent(ev)
        }

        override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
            super.requestDisallowInterceptTouchEvent(disallowIntercept)
            checkNeedHide()
        }

        override fun onAttachedToWindow() {
            super.onAttachedToWindow()
            checkNeedHide()
        }

        override fun onDraw(canvas: Canvas) {
            val width = measuredWidth
            val height = measuredHeight
            val progressLineY: Int = height
            val progressLineX = 0
            val cy: Int = height
            val progressX = progressLineX + ((width - progressLineX) * progress).toInt()
            if (bufferedPosition != 0f) {
                canvas.drawRect(
                    progressLineX.toFloat(),
                    progressLineY.toFloat(),
                    progressLineX + (width - progressLineX) * bufferedPosition,
                    progressLineY.toFloat(),
                    progressInnerPaint!!
                )
            }
            canvas.drawRect(
                progressLineX.toFloat(),
                progressLineY.toFloat(),
                progressX.toFloat(),
                progressLineY.toFloat(),
                progressPaint!!
            )
        }

    }


    fun show(
        activity: Activity,
        controls: View?,
        aspectRatio: Float,
        rotation: Int,
        faceView: SurfaceView?
    ): TextureView? {
        this.activity = activity
        windowView = object : FrameLayout(activity) {
            private var startX = 0f
            private var startY = 0f
            private var dragging = false
            override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
                val x = event.rawX
                val y = event.rawY
                if (event.action == MotionEvent.ACTION_DOWN) {
                    startX = x
                    startY = y
                } else if (event.action == MotionEvent.ACTION_MOVE && !dragging) {
                    if (Math.abs(startX - x) >= AndroidUtilities.getPixelsInCM(
                            0.3f,
                            true
                        ) || Math.abs(startY - y) >= AndroidUtilities.getPixelsInCM(
                            0.3f,
                            false
                        )
                    ) {
                        dragging = true
                        startX = x
                        startY = y
                        if (controlsView != null) {
                            (controlsView as ViewParent).requestDisallowInterceptTouchEvent(true)
                        }
                        return true
                    }
                }
                return super.onInterceptTouchEvent(event)
            }

            override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
                super.requestDisallowInterceptTouchEvent(disallowIntercept)
            }

            override fun onTouchEvent(event: MotionEvent): Boolean {
                if (!dragging) {
                    return false
                }
                val x = event.rawX
                val y = event.rawY
                if (event.action == MotionEvent.ACTION_MOVE) {
                    val dx = x - startX
                    val dy = y - startY
                    windowLayoutParams!!.x += dx.toInt()
                    windowLayoutParams!!.y += dy.toInt()
                    var maxDiff = videoWidth / 2
                    if (windowLayoutParams!!.x < -maxDiff) {
                        windowLayoutParams!!.x = -maxDiff
                    } else if (windowLayoutParams!!.x > AndroidUtilities.displaySize.x - windowLayoutParams!!.width + maxDiff) {
                        windowLayoutParams!!.x =
                            AndroidUtilities.displaySize.x - windowLayoutParams!!.width + maxDiff
                    }
                    var alpha = 1.0f
                    if (windowLayoutParams!!.x < 0) {
                        alpha = 1.0f + windowLayoutParams!!.x / maxDiff.toFloat() * 0.5f
                    } else if (windowLayoutParams!!.x > AndroidUtilities.displaySize.x - windowLayoutParams!!.width) {
                        alpha =
                            1.0f - (windowLayoutParams!!.x - AndroidUtilities.displaySize.x + windowLayoutParams!!.width) / maxDiff.toFloat() * 0.5f
                    }
                    if (windowView!!.alpha != alpha) {
                        windowView!!.alpha = alpha
                    }
                    maxDiff = 0
                    if (windowLayoutParams!!.y < -maxDiff) {
                        windowLayoutParams!!.y = -maxDiff
                    } else if (windowLayoutParams!!.y > AndroidUtilities.displaySize.y - windowLayoutParams!!.height + maxDiff) {
                        windowLayoutParams!!.y =
                            AndroidUtilities.displaySize.y - windowLayoutParams!!.height + maxDiff
                    }
                    windowManager!!.updateViewLayout(windowView, windowLayoutParams)
                    startX = x
                    startY = y
                } else if (event.action == MotionEvent.ACTION_UP) {
                    dragging = false
                    animateToBoundsMaybe()
                }
                return true
            }
        }
        if (aspectRatio > 1) {
            videoWidth = AndroidUtilities.dp(192f)
            videoHeight = (videoWidth / aspectRatio).toInt()
        } else {
            videoHeight = AndroidUtilities.dp(192f)
            videoWidth = (videoHeight * aspectRatio).toInt()
        }
        val aspectRatioFrameLayout = AspectRatioFrameLayout(activity)
        aspectRatioFrameLayout.setAspectRatio(aspectRatio, rotation)
        val layoutParame = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        windowView!!.addView(
            aspectRatioFrameLayout, layoutParame
        )
        val textureView: TextureView?
        if (faceView != null) {
            val parent = faceView.parent as ViewGroup
            parent.removeView(faceView)
            aspectRatioFrameLayout.addView(
                faceView,
                layoutParame
            )
            textureView = null
        } else {
            textureView = TextureView(activity)
            aspectRatioFrameLayout.addView(
                textureView,
                layoutParame
            )
        }
        controlsView = controls ?: MiniControlsView(activity)
        windowView!!.addView(
            controlsView,
            layoutParame
        )
        windowManager = WatchlistApp.applicationContext!!.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        preferences = WatchlistApp.applicationContext!!.getSharedPreferences(
            "pipconfig",
            Context.MODE_PRIVATE
        )
        val sidex = preferences!!.getInt("sidex", 1)
        val sidey = preferences!!.getInt("sidey", 0)
        val px = preferences!!.getFloat("px", 0f)
        val py = preferences!!.getFloat("py", 0f)
        try {
            windowLayoutParams = WindowManager.LayoutParams()
            windowLayoutParams!!.width = videoWidth
            windowLayoutParams!!.height = videoHeight
            windowLayoutParams!!.x = getSideCoord(true, sidex, px, videoWidth)
            windowLayoutParams!!.y =getSideCoord(false, sidey, py, videoHeight)
            windowLayoutParams!!.format = PixelFormat.TRANSLUCENT
            windowLayoutParams!!.gravity = Gravity.TOP or Gravity.LEFT
            if (Build.VERSION.SDK_INT >= 26) {
                windowLayoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                windowLayoutParams!!.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
            }
            windowLayoutParams!!.flags =
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            windowManager!!.addView(windowView, windowLayoutParams)
        } catch (e: java.lang.Exception) {
            return null
        }
        return textureView
    }


    fun onVideoCompleted() {
        if (controlsView is MiniControlsView) {
            val miniControlsView = controlsView as MiniControlsView
            miniControlsView.isCompleted = true
            miniControlsView.progress = 0f
            miniControlsView.bufferedPosition = 0f
            miniControlsView.updatePlayButton()
            miniControlsView.invalidate()
            miniControlsView.show(true, true)
        }
    }

    fun setBufferedProgress(progress: Float) {
        if (controlsView is MiniControlsView) {
            (controlsView as MiniControlsView).setBufferedProgress(progress)
        }
    }

    fun updatePlayButton() {
        if (controlsView is MiniControlsView) {
            val miniControlsView = controlsView as MiniControlsView
            miniControlsView.updatePlayButton()
            miniControlsView.invalidate()
        }
    }

    private fun getSideCoord(
        isX: Boolean,
        side: Int,
        p: Float,
        sideSize: Int
    ): Int {
        val total: Int
        total = if (isX) {
            AndroidUtilities.displaySize.x - sideSize
        } else {
            AndroidUtilities.displaySize.y - sideSize - actionBarHeight
        }
        var result: Int
        if (side == 0) {
            result = AndroidUtilities.dp(10f)
        } else if (side == 1) {
            result = total - AndroidUtilities.dp(10f)
        } else {
            result =
                Math.round((total - AndroidUtilities.dp(20f)) * p) + AndroidUtilities.dp(10f)
        }
        if (!isX) {
            result += actionBarHeight;
        }
        return result
    }

    fun close() {
        try {
            windowManager!!.removeView(windowView)
        } catch (e: Exception) {
            //don't promt
        }
    }

    fun onConfigurationChanged() {
        val sidex = preferences!!.getInt("sidex", 1)
        val sidey = preferences!!.getInt("sidey", 0)
        val px = preferences!!.getFloat("px", 0f)
        val py = preferences!!.getFloat("py", 0f)
        windowLayoutParams!!.x = getSideCoord(true, sidex, px, videoWidth)
        windowLayoutParams!!.y = getSideCoord(false, sidey, py, videoHeight)
        windowManager!!.updateViewLayout(windowView, windowLayoutParams)
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun animateToBoundsMaybe() {
        val startX: Int = getSideCoord(true, 0, 0f, videoWidth)
        val endX: Int = getSideCoord(true, 1, 0f, videoWidth)
        val startY: Int = getSideCoord(false, 0, 0f, videoHeight)
        val endY: Int = getSideCoord(false, 1, 0f, videoHeight)
        var animators: ArrayList<Animator?>? = null
        val editor = preferences!!.edit()
        val maxDiff = AndroidUtilities.dp(20f)
        var slideOut = false
        if (Math.abs(startX - windowLayoutParams!!.x) <= maxDiff || windowLayoutParams!!.x < 0 && windowLayoutParams!!.x > -videoWidth / 4) {
            if (animators == null) {
                animators = ArrayList()
            }
            editor.putInt("sidex", 0)
            if (windowView!!.alpha != 1.0f) {
                animators.add(ObjectAnimator.ofFloat(windowView, "alpha", 1.0f))
            }
            animators.add(ObjectAnimator.ofInt(this, "x", startX))
        } else if (Math.abs(endX - windowLayoutParams!!.x) <= maxDiff || windowLayoutParams!!.x > AndroidUtilities.displaySize.x - videoWidth && windowLayoutParams!!.x < AndroidUtilities.displaySize.x - videoWidth / 4 * 3) {
            if (animators == null) {
                animators = ArrayList()
            }
            editor.putInt("sidex", 1)
            if (windowView!!.alpha != 1.0f) {
                animators.add(ObjectAnimator.ofFloat(windowView, "alpha", 1.0f))
            }
            animators.add(ObjectAnimator.ofInt(this, "x", endX))
        } else if (windowView!!.alpha != 1.0f) {
            if (animators == null) {
                animators = ArrayList()
            }
            if (windowLayoutParams!!.x < 0) {
                animators.add(ObjectAnimator.ofInt(this, "x", -videoWidth))
            } else {
                animators.add(ObjectAnimator.ofInt(this, "x", AndroidUtilities.displaySize.x))
            }
            slideOut = true
        } else {
            editor.putFloat("px", (windowLayoutParams!!.x - startX) / (endX - startX).toFloat())
            editor.putInt("sidex", 2)
        }
        if (!slideOut) {
            if (Math.abs(startY - windowLayoutParams!!.y) <= maxDiff || windowLayoutParams!!.y <= actionBarHeight) {
                if (animators == null) {
                    animators = ArrayList()
                }
                editor.putInt("sidey", 0)
                animators.add(ObjectAnimator.ofInt(this, "y", startY))
            } else if (Math.abs(endY - windowLayoutParams!!.y) <= maxDiff) {
                if (animators == null) {
                    animators = ArrayList()
                }
                editor.putInt("sidey", 1)
                animators.add(ObjectAnimator.ofInt(this, "y", endY))
            } else {
                editor.putFloat(
                    "py",
                    (windowLayoutParams!!.y - startY) / (endY - startY).toFloat()
                )
                editor.putInt("sidey", 2)
            }
            editor.commit()
        }
        if (animators != null) {
            if (decelerateInterpolator == null) {
                decelerateInterpolator = DecelerateInterpolator()
            }
            val animatorSet = AnimatorSet()
            animatorSet.interpolator = decelerateInterpolator
            animatorSet.duration = 150
            if (slideOut) {
                animators.add(ObjectAnimator.ofFloat(windowView, "alpha", 0.0f))
                animatorSet.addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {

                    }
                })
            }
            animatorSet.playTogether(animators)
            animatorSet.start()
        }
    }

    fun getPipRect(aspectRatio: Float): Rect? {
        val preferences: SharedPreferences =
            WatchlistApp.applicationContext!!.getSharedPreferences(
                "pipconfig",
                Context.MODE_PRIVATE
            )
        val sidex = preferences.getInt("sidex", 1)
        val sidey = preferences.getInt("sidey", 0)
        val px = preferences.getFloat("px", 0f)
        val py = preferences.getFloat("py", 0f)
        val videoWidth: Int
        val videoHeight: Int
        if (aspectRatio > 1) {
            videoWidth = AndroidUtilities.dp(192f)
            videoHeight = (videoWidth / aspectRatio).toInt()
        } else {
            videoHeight = AndroidUtilities.dp(192f)
            videoWidth = (videoHeight * aspectRatio).toInt()
        }
        return Rect(
            getSideCoord(true, sidex, px, videoWidth), getSideCoord(false, sidey, py, videoHeight),
            videoWidth,
            videoHeight
        )
    }

    @Keep
    fun getX(): Int {
        return windowLayoutParams!!.x
    }

    @Keep
    fun getY(): Int {
        return windowLayoutParams!!.y
    }

    @Keep
    fun setX(value: Int) {
        windowLayoutParams!!.x = value
        windowManager!!.updateViewLayout(windowView, windowLayoutParams)
    }

    @Keep
    fun setY(value: Int) {
        windowLayoutParams!!.y = value
        windowManager!!.updateViewLayout(windowView, windowLayoutParams)
    }

}