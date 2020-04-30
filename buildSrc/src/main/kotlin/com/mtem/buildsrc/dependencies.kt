package com.mtem.buildsrc

typealias DepsList = List<Pair<String,LibType>>

sealed class LibType {
    object Library: LibType()
    object Compiler: LibType()
    object TestLib: LibType()
    object AndroidTestLib: LibType()
}




object Versions {
    const val ktlint = "0.33.0"

}
object Dependencies{
    val androidLibs: DepsList = listOf(
        Libs.mvRx to LibType.Library
    )
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.1.0-alpha08"
    const val mvRx = "com.airbnb.android:mvrx:1.3.0"
    const val gravitySnapHelper = "com.github.rubensousa:gravitysnaphelper:2.2.0"

    const val junit = "junit:junit:4.12"
    const val robolectric = "org.robolectric:robolectric:4.3.1"
    const val mockK = "io.mockk:mockk:1.9.3"

    const val inboxRecyclerView = "me.saket:inboxrecyclerview:2.0.0-beta3"

    const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.0-beta-3"

    object Google {
        const val material = "com.google.android.material:material:1.1.0-alpha09"
        const val gmsGoogleServices = "com.google.gms:google-services:4.3.2"
    }

    object Kotlin {
        private const val version = "1.3.72"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.3.5-1.4-M1"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }
    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.1.0"
        const val browser = "androidx.browser:browser:1.0.0"
        const val collection = "androidx.collection:collection-ktx:1.1.0"
        const val palette = "androidx.palette:palette:1.0.0"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0-rc01"
        const val emoji = "androidx.emoji:emoji:1.0.0"
        const val swiperefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-alpha03"

        object Navigation {
            private const val version = "2.2.0"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Fragment {
            private const val version = "1.2.0-rc01"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
        }

        object Test {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"
            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
        }

        const val archCoreTesting = "androidx.arch.core:core-testing:2.1.0"

        object Paging {
            private const val version = "2.1.0"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime-ktx:$version"
        }

        const val preference = "androidx.preference:preference:1.1.0"

        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta3"

        const val coreKtx = "androidx.core:core-ktx:1.2.0-beta01"

        object Lifecycle {
            private const val version = "2.2.0-rc01"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$version"
            const val livedataCore = "androidx.lifecycle:lifecycle-livedata-core:$version"
        }

        object Room {
            private const val version = "2.2.1"
            const val common = "androidx.room:room-common:$version"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val testing = "androidx.room:room-testing:$version"
        }

        object Work {
            private const val version = "2.2.0"
            const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"
        }
    }

    object RxJava {
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
        const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.19"
        const val rxKotlin = "io.reactivex.rxjava2:rxkotlin:2.4.0"
    }

    object Dagger {
        private const val version = "2.25.2"
        const val dagger = "com.google.dagger:dagger:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object Retrofit {
        private const val version = "2.6.2"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val retrofit_rxjava_adapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
        const val gson = "com.google.code.gson:gson:2.8.6"
    }

    object OkHttp {
        private const val version = "4.2.2"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Epoxy {
        private const val version = "3.8.0"
        const val epoxy = "com.airbnb.android:epoxy:$version"
        const val paging = "com.airbnb.android:epoxy-paging:$version"
        const val dataBinding = "com.airbnb.android:epoxy-databinding:$version"
        const val processor = "com.airbnb.android:epoxy-processor:$version"
    }

    object Coil {
        private const val version = "0.8.0"
        const val coil = "io.coil-kt:coil:$version"
    }

    object AssistedInject {
        private const val version = "0.5.1"
        const val annotationDagger2 = "com.squareup.inject:assisted-inject-annotations-dagger2:$version"
        const val processorDagger2 = "com.squareup.inject:assisted-inject-processor-dagger2:$version"
    }
}
