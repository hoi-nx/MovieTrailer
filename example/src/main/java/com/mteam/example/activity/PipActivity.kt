package com.mteam.example.activity

import android.app.PendingIntent
import android.app.PictureInPictureParams
import android.app.RemoteAction
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import android.util.Rational
import android.view.View
import android.widget.Button
import android.widget.ScrollView
import com.mteam.example.R
import com.mteam.example.widget.MovieView
import java.util.*


/**
 * Demonstrates usage of Picture-in-Picture mode on phones and tablets.
 */
class PipActivity : AppCompatActivity() {

    companion object {

        /** Intent action for media controls from Picture-in-Picture mode.  */
        private const val ACTION_MEDIA_CONTROL = "media_control"

        /** Intent extra for media controls from Picture-in-Picture mode.  */
        private const val EXTRA_CONTROL_TYPE = "control_type"

        /** The request code for play action PendingIntent.  */
        private const val REQUEST_PLAY = 1

        /** The request code for pause action PendingIntent.  */
        private const val REQUEST_PAUSE = 2

        /** The request code for info action PendingIntent.  */
        private const val REQUEST_INFO = 3

        /** The intent extra value for play action.  */
        private const val CONTROL_TYPE_PLAY = 1

        /** The intent extra value for pause action.  */
        private const val CONTROL_TYPE_PAUSE = 2

    }

    /** The arguments to be used for Picture-in-Picture mode.  */
    private val mPictureInPictureParamsBuilder = PictureInPictureParams.Builder()

    /** This shows the video.  */
    private lateinit var mMovieView: MovieView

    /** The bottom half of the screen; hidden on landscape  */
    private lateinit var mScrollView: ScrollView

    /** A [BroadcastReceiver] to receive action item events from Picture-in-Picture mode.  */
    private val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            if (intent != null) {
                if (intent.action != ACTION_MEDIA_CONTROL) {
                    return
                }

                // This is where we are called back from Picture-in-Picture action items.
                when (intent.getIntExtra(EXTRA_CONTROL_TYPE, 0)) {
                    CONTROL_TYPE_PLAY -> mMovieView.play()
                    CONTROL_TYPE_PAUSE -> mMovieView.pause()
                }
            }
        }
    }

    private val labelPlay: String by lazy { getString(R.string.play) }
    private val labelPause: String by lazy { getString(R.string.pause) }

    /**
     * Callbacks from the [MovieView] showing the video playback.
     */
    private val mMovieListener = object : MovieView.MovieListener() {

        override fun onMovieStarted() {
            // We are playing the video now. In PiP mode, we want to show an action item to pause
            // the video.
            updatePictureInPictureActions(R.drawable.ic_pause_24dp, labelPause,
                    CONTROL_TYPE_PAUSE, REQUEST_PAUSE)
        }

        override fun onMovieStopped() {
            // The video stopped or reached its end. In PiP mode, we want to show an action item
            // to play the video.
            updatePictureInPictureActions(R.drawable.ic_play_arrow_24dp, labelPlay,
                    CONTROL_TYPE_PLAY, REQUEST_PLAY)
        }

        override fun onMovieMinimized() {
            // The MovieView wants us to minimize it. We enter Picture-in-Picture mode now.
            minimize()
        }

    }

    /**
     * Update the state of pause/resume action item in Picture-in-Picture mode.

     * @param iconId      The icon to be used.
     * *
     * @param title       The title text.
     * *
     * @param controlType The type of the action. either [.CONTROL_TYPE_PLAY] or
     * *                    [.CONTROL_TYPE_PAUSE].
     * *
     * @param requestCode The request code for the [PendingIntent].
     */
    internal fun updatePictureInPictureActions(@DrawableRes iconId: Int, title: String,
                                               controlType: Int, requestCode: Int) {
        val actions = ArrayList<RemoteAction>()

        // This is the PendingIntent that is invoked when a user clicks on the action item.
        // You need to use distinct request codes for play and pause, or the PendingIntent won't
        // be properly updated.
        val intent = PendingIntent.getBroadcast(this@PipActivity,
                requestCode, Intent(ACTION_MEDIA_CONTROL)
                .putExtra(EXTRA_CONTROL_TYPE, controlType), 0)
        val icon = Icon.createWithResource(this@PipActivity, iconId)
        actions.add(RemoteAction(icon, title, title, intent))

        // Another action item. This is a fixed action.
        actions.add(RemoteAction(
                Icon.createWithResource(this@PipActivity, R.drawable.ic_info_24dp),
                getString(R.string.info), getString(R.string.info_description),
                PendingIntent.getActivity(this@PipActivity, REQUEST_INFO,
                        Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.info_uri))),
                        0)))

        mPictureInPictureParamsBuilder.setActions(actions)

        // This is how you can update action items (or aspect ratio) for Picture-in-Picture mode.
        // Note this call can happen even when the app is not in PiP mode. In that case, the
        // arguments will be used for at the next call of #enterPictureInPictureMode.
        setPictureInPictureParams(mPictureInPictureParamsBuilder.build())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pip_activity)

        // View references
        mMovieView = findViewById(R.id.movie)
        mScrollView = findViewById(R.id.scroll)

        val switchExampleButton = findViewById<Button>(R.id.switch_example)
        switchExampleButton.text = getString(R.string.switch_media_session)
        // Set up the video; it automatically starts.
        mMovieView.setMovieListener(mMovieListener)
        findViewById<Button>(R.id.pip).setOnClickListener { minimize() }
    }

    override fun onStop() {
        // On entering Picture-in-Picture mode, onPause is called, but not onStop.
        // For this reason, this is the place where we should pause the video playback.
        mMovieView.pause()
        super.onStop()
    }

    override fun onRestart() {
        super.onRestart()
        // Show the video controls so the video can be easily resumed.
        if (!isInPictureInPictureMode) {
            mMovieView.showControls()
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        adjustFullScreen(newConfig)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            adjustFullScreen(resources.configuration)
        }
    }

    override fun onPictureInPictureModeChanged(
            isInPictureInPictureMode: Boolean, newConfig: Configuration) {
        super.onPictureInPictureModeChanged(isInPictureInPictureMode, newConfig)
        if (isInPictureInPictureMode) {
            // Starts receiving events from action items in PiP mode.
            registerReceiver(mReceiver, IntentFilter(ACTION_MEDIA_CONTROL))
        } else {
            // We are out of PiP mode. We can stop receiving events from it.
            unregisterReceiver(mReceiver)
            // Show the video controls if the video is not playing
            if (!mMovieView.isPlaying) {
                mMovieView.showControls()
            }
        }
    }

    /**
     * Enters Picture-in-Picture mode.
     */
    internal fun minimize() {
        // Hide the controls in picture-in-picture mode.
        mMovieView.hideControls()
        // Calculate the aspect ratio of the PiP screen.
        mPictureInPictureParamsBuilder.setAspectRatio(Rational(mMovieView.width, mMovieView.height))
        enterPictureInPictureMode(mPictureInPictureParamsBuilder.build())
    }

    /**
     * Adjusts immersive full-screen flags depending on the screen orientation.

     * @param config The current [Configuration].
     */
    private fun adjustFullScreen(config: Configuration) {
        val decorView = window.decorView
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            mScrollView.visibility = View.GONE
            mMovieView.setAdjustViewBounds(false)
        } else {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            mScrollView.visibility = View.VISIBLE
            mMovieView.setAdjustViewBounds(true)
        }
    }
}
