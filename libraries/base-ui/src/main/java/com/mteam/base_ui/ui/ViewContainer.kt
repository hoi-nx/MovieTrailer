package com.mteam.base_ui.ui

import android.view.ViewGroup

interface ViewContainer {

    /**
     * The root [ViewGroup] into which the activity should place its contents.
     */
    fun forActivity(activity: BaseActivity): ViewGroup

    companion object {
        /**
         * An [ViewContainer] which returns the normal activity content view.
         */
        val DEFAULT = object : ViewContainer {
            override fun forActivity(activity: BaseActivity): ViewGroup {
                return activity.findViewById(android.R.id.content)
            }
        }
    }
}