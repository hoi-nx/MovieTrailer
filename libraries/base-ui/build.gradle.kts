import com.mtem.buildsrc.Libs

plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
}

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")
    defaultConfig {
        minSdkVersion(21)
        targetSdkVersion(29)

    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        isEnabled = true
    }
}
dependencies {
    api(project(":libraries:appconfig"))
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.constraintlayout)
    implementation(Libs.Google.material)
    implementation(Libs.Coroutines.core)
    implementation(Libs.okio)
    implementation(Libs.timber)
    implementation(Libs.OkHttp.okhttp)
    implementation(Libs.OkHttp.loggingInterceptor)
    implementation(Libs.autoDispose.core)
    implementation(Libs.autoDispose.android)
    implementation(Libs.autoDispose.androidArch)
    implementation(Libs.RxJava.relay)
    implementation(Libs.Dagger.dagger)
    implementation(Libs.tapTargetView)
    kapt(Libs.Dagger.compiler)


    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}