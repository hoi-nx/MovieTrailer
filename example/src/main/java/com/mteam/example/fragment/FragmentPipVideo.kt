package com.mteam.example.fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mteam.example.R
import com.mteam.example.widget.MovieView
import kotlinx.android.synthetic.main.pip_video.*

class FragmentPipVideo : Fragment() {


    private val mMovieListener = object : MovieView.MovieListener() {

        override fun onMovieStarted() {

        }

        override fun onMovieStopped() {

        }

        override fun onMovieMinimized() {
            // The MovieView wants us to minimize it. We enter Picture-in-Picture mode now.

            if (!checkInlinePermissions()) {
                return
            }
        }

    }

    fun checkInlinePermissions(): Boolean {
        if (activity == null) {
            return false
        }
        if (Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(activity)
        ) {
            return true
        } else {
            activity!!.startActivity(
                Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + activity!!.packageName)
                )
            )
        }
        return false
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pip_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieView.setMovieListener(movieListener = mMovieListener)

    }
}