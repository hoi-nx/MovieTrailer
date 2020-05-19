package com.movie.appconfig

interface AppConfigMetadataContributor {
  fun data(): Map<String, Any?>
}
