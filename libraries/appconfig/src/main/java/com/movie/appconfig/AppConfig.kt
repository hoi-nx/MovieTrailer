package com.movie.appconfig

interface AppConfig {
    val isDebug: Boolean
    val applicationId: String
    val buildType: String
    val versionCode: Long
    val versionName: String
    val timestamp: String
    val sdkInt: Int
    val metadata: Map<Any, Any?>
}

interface EmptyAppConfig : AppConfig {
    override val isDebug: Boolean get() = throw NotImplementedError()
    override val applicationId: String get() = throw NotImplementedError()
    override val buildType: String get() = throw NotImplementedError()
    override val versionCode: Long get() = throw NotImplementedError()
    override val versionName: String get() = throw NotImplementedError()
    override val timestamp: String get() = throw NotImplementedError()
    override val sdkInt: Int get() = throw NotImplementedError()
    override val metadata: Map<Any, Any?> get() = throw NotImplementedError()
}

fun <T> AppConfig.requireMetadata(key: Any): T {
    return requireNotNull(readMetadata(key))
}

@Suppress("UNCHECKED_CAST")
fun <T> AppConfig.readMetadata(key: Any): T? {
    return metadata[key] as T?
}
