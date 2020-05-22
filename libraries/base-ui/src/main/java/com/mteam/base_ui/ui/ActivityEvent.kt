package com.mteam.base_ui.ui

import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleEndedException

/**
 * Activity lifecycle events.
 */
enum class ActivityEvent {
    CREATE, START, RESUME, PAUSE, STOP, DESTROY;

    companion object {
        val LIFECYCLE = CorrespondingEventsFunction { lastEvent: ActivityEvent ->
            return@CorrespondingEventsFunction when (lastEvent) {
                CREATE -> DESTROY
                START -> STOP
                RESUME -> PAUSE
                PAUSE -> STOP
                STOP -> DESTROY
                DESTROY -> throw LifecycleEndedException(
                    "Cannot bind to Activity lifecycle after it's been destroyed.")
            }
        }
    }
}