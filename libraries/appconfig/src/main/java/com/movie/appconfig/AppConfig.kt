package com.movie.appconfig
interface AppConfig {
    val isDebug: Boolean
    val applicationId: String
    val buildType: String
    val versionCode: Long
    val versionName: String
    val timestamp: String
    val sdkInt: Int
}

interface EmptyAppConfig : AppConfig {
    override val isDebug: Boolean get() = throw NotImplementedError()
    override val applicationId: String get() = throw NotImplementedError()
    override val buildType: String get() = throw NotImplementedError()
    override val versionCode: Long get() = throw NotImplementedError()
    override val versionName: String get() = throw NotImplementedError()
    override val timestamp: String get() = throw NotImplementedError()
    override val sdkInt: Int get() = throw NotImplementedError()
}

