package com.mteam.base_ui.ui

import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.ContextThemeWrapper
import androidx.drawerlayout.widget.DrawerLayout
import com.mteam.base_ui.R
import com.mteam.base_ui.databinding.DebugActivityBinding
import com.mteam.base_ui.debug.DebugView
import com.mteam.base_ui.taptargetview.TapTarget
import com.mteam.base_ui.taptargetview.TapTargetView


class DebugViewContainer : ViewContainer {
    override fun forActivity(activity: BaseActivity): ViewGroup {
        val viewHolder = DebugActivityBinding.inflate(
            LayoutInflater.from(activity),
            activity.findViewById(android.R.id.content),
            false
        )
        activity.setContentView(viewHolder.root)

        val drawerContext = ContextThemeWrapper(activity, R.style.DebugDrawer)
        val debugView = DebugView(drawerContext, null)
        viewHolder.debugDrawer.addView(debugView)

        viewHolder.drawerLayout.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                debugView.onDrawerOpened()
            }
        })

        viewHolder.drawerLayout.openDrawer(Gravity.END)
        TapTargetView.showFor(activity,
            TapTarget.forView(
                debugView.icon,
                debugView.resources.getString(R.string.development_settings),
                debugView.resources.getString(R.string.debug_drawer_welcome)
            ).outerCircleColorInt(Color.parseColor("#EE222222"))
                .outerCircleAlpha(0.96f)
                .titleTextColorInt(Color.WHITE)
                .descriptionTextColorInt(Color.parseColor("#33FFFFFF"))
                .targetCircleColorInt(Color.WHITE)
                .drawShadow(true)
                .transparentTarget(true), // All options below are optional
            // Specify the target radius (in dp)
            object : TapTargetView.Listener() {
            })

        return viewHolder.debugContent
    }
}