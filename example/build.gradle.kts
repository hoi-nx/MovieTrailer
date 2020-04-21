import com.mtem.buildsrc.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}
android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")
    defaultConfig {
        applicationId = "com.mteam.example"
        minSdkVersion(21)
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


    implementation(Libs.Epoxy.epoxy)
    implementation(Libs.Epoxy.paging)
    implementation(Libs.Epoxy.dataBinding)

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}


