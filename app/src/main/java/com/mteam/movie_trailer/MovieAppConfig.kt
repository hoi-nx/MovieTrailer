package com.mteam.movie_trailer

import android.content.Context
import android.os.Build
import com.movie.appconfig.AppConfig
import com.movie.appconfig.AppConfigMetadataContributor
import com.mteam.base_ui.ui.versionInfo

class MovieAppConfig constructor(appContext: Context,
    metadataContributors: DaggerSet<AppConfigMetadataContributor>
) : AppConfig {
    private val versionInfo = appContext.versionInfo
    override val isDebug: Boolean = BuildConfig.DEBUG
    override val applicationId: String = BuildConfig.APPLICATION_ID
    override val buildType: String = BuildConfig.BUILD_TYPE
    override val versionCode: Long = versionInfo.code
    override val versionName: String = versionInfo.name
    override val timestamp: String = versionInfo.timestamp
    override val sdkInt: Int = Build.VERSION.SDK_INT
    override val metadata: Map<Any, Any?> = mutableMapOf<Any, Any?>()
        .apply { metadataContributors.forEach { putAll(it.data()) } }
        .toMap()
}
