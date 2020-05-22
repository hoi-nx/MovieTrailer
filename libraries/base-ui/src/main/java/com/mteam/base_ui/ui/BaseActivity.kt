package com.mteam.base_ui.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.annotation.CheckResult
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.jakewharton.rxrelay2.BehaviorRelay
import com.uber.autodispose.lifecycle.CorrespondingEventsFunction
import com.uber.autodispose.lifecycle.LifecycleScopeProvider
import io.reactivex.Observable
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), LifecycleScopeProvider<ActivityEvent> {
    private val lifecycleRelay = BehaviorRelay.create<ActivityEvent>()
    protected var viewContainer = DebugViewContainer();


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleRelay.accept(ActivityEvent.CREATE)
    }

    @CallSuper
    override fun onStart() {
        super.onStart()
        lifecycleRelay.accept(ActivityEvent.START)
    }

    @CallSuper
    override fun onResume() {
        super.onResume()
        lifecycleRelay.accept(ActivityEvent.RESUME)
    }

    @CallSuper
    override fun onPause() {
        lifecycleRelay.accept(ActivityEvent.PAUSE)
        super.onPause()
    }

    @CallSuper
    override fun onStop() {
        lifecycleRelay.accept(ActivityEvent.STOP)
        super.onStop()
    }

    @CallSuper
    override fun onDestroy() {
        lifecycleRelay.accept(ActivityEvent.DESTROY)
        super.onDestroy()
    }

    @CheckResult
    protected inline fun <T : ViewBinding> ViewContainer.inflateBinding(
        inflate: (LayoutInflater, ViewGroup, Boolean) -> T
    ): T = inflate(layoutInflater, forActivity(this@BaseActivity), true)

    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleRelay
    }

    final override fun correspondingEvents(): CorrespondingEventsFunction<ActivityEvent> {
        return ActivityEvent.LIFECYCLE
    }

    final override fun peekLifecycle(): ActivityEvent? {
        return lifecycleRelay.value
    }

}