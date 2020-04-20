package com.mteam.base_ui

sealed class UiStatus

object UiIdle : UiStatus()

data class UiError(val exception: Throwable? = null) : UiStatus()

data class UiLoading(val fullRefresh: Boolean = true) : UiStatus()

object UiSuccess : UiStatus()