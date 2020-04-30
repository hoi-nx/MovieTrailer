import com.mtem.buildsrc.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    id("androidx.navigation.safeargs.kotlin")
    kotlin("kapt")

}

buildscript {
    val kotlin_version by extra("1.3.72")
    //    ext.kotlin_version = '1.3.61'
    repositories {
        google()
        jcenter()

    }
    dependencies {
        classpath(com.mtem.buildsrc.Libs.androidGradlePlugin)
        classpath(com.mtem.buildsrc.Libs.Kotlin.gradlePlugin)
        classpath(com.mtem.buildsrc.Libs.Kotlin.extensions)
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.2")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}
android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")
    defaultConfig {
        applicationId = "com.mteam.example"
        minSdkVersion(26)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.AndroidX.coreKtx)
    implementation(Libs.AndroidX.appcompat)
    implementation(Libs.AndroidX.constraintlayout)
    implementation(Libs.Google.material)

    implementation(Libs.AndroidX.browser)
    implementation(Libs.AndroidX.palette)
    implementation(Libs.AndroidX.recyclerview)
    implementation(Libs.AndroidX.emoji)
    implementation(Libs.AndroidX.preference)
    implementation(Libs.AndroidX.Fragment.fragment)
    implementation(Libs.AndroidX.Fragment.fragmentKtx)
    implementation(Libs.AndroidX.Navigation.fragment)
    implementation(Libs.AndroidX.Navigation.ui)

    implementation(Libs.Retrofit.retrofit)
    implementation(Libs.Retrofit.gsonConverter)
    implementation(Libs.Retrofit.retrofit_rxjava_adapter)
    implementation(Libs.Retrofit.gson)
    implementation(Libs.RxJava.rxJava)
    implementation(Libs.RxJava.rxAndroid)
    implementation(Libs.RxJava.rxKotlin)
    implementation(Libs.OkHttp.loggingInterceptor)
    implementation(Libs.OkHttp.okhttp)


    implementation(Libs.AndroidX.Room.common)
    kapt(Libs.AndroidX.Room.compiler)
    implementation(Libs.AndroidX.Room.ktx)
    implementation(Libs.AndroidX.Room.runtime)
    implementation(Libs.AndroidX.Work.runtimeKtx)



    implementation(Libs.Epoxy.epoxy)
    implementation(Libs.Epoxy.paging)
    implementation(Libs.Epoxy.dataBinding)
    implementation(Libs.mvRx)
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}


