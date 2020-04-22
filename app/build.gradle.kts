import com.android.build.gradle.internal.api.BaseVariantOutputImpl
import com.mtem.buildsrc.Libs

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}


val useReleaseKeystore = rootProject.file("release/app-release.jks").exists()

android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")
    defaultConfig {
        applicationId = "com.mteam.movietrailer"
        minSdkVersion(21)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        ndkBuild {
            setPath("../jni/Android.mk")

        }
    }
    splits {
        density {
            isEnable = true
            reset()
            include("mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi")
        }
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = true
        }
    }

    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
            isDebuggable = true
            isJniDebuggable = true
        }
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            isJniDebuggable = false
            isShrinkResources = true;
        }
    }

//    sourceSets {
//        getByName("debug") {
//            //  jniLibs.srcDir("../jniLibs")
//        }
//        getByName("release") {
//
//        }
//    }

    lintOptions {
        // Disable lintVital. Not needed since lint is run on CI
        isCheckReleaseBuilds = false
        // Allow lint to check dependencies
        isCheckDependencies = true
        // Ignore any tests
        isIgnoreTestSources = true
    }

    applicationVariants.all {
        outputs.all {
            var appName = (this as BaseVariantOutputImpl).outputFileName
            if (appName.contains("debug")) {
                appName = "MovieTrailer_Debug.apk"
            } else if (appName.contains("release")) {
                appName = "MovieTrailer.apk"
            }
            outputFileName = appName
        }
    }
    //Cach 1
    //export ANDROID_SDK=/Users/stealer/Library/Android/sdk
    //export ANDROID_NDK=/Users/stealer/Library/Android/sdk/ndk/21.0.6113669
    //export PATH="$PATH:$ANDROID_SDK/tools:$ANDROID_SDK/platform-tools:$ANDROID_NDK"
    //ndk-build NDK_PROJECT_PATH=/Users/stealer/Desktop/MovieTrailer
    //=================================================
    //Cach 2
//    task ndkBuild(type: Exec, description: "Compile JNI source via NDK") {
//        def ndkDir = android.ndkDirectory.getAbsolutePath()
//        commandLine "/Users/stealer/Library/Android/sdk/ndk/21.0.6113669/ndk-build",
//                "NDK_PROJECT_PATH=/Users/stealer/Desktop/MovieTrailer",
//                "NDK_LIBS_OUT=../jniLibs",
//                "APP_BUILD_SCRIPT=../jni/Android.mk",
//                "NDK_APPLICATION_MK=../jni/Application.mk"
//    }
//
//    val CompileJNI = tasks.register<Exec>("CompileJNI") {
//        logger.log(LogLevel.LIFECYCLE, "Compile JNI source via NDK")
//        commandLine(
//            "/Users/stealer/Library/Android/sdk/ndk/21.0.6113669/ndk-build",
//            "NDK_PROJECT_PATH=/Users/stealer/Desktop/MovieTrailer",
//            "NDK_LIBS_OUT=../jniLibs",
//            "APP_BUILD_SCRIPT=../jni/Android.mk",
//            "NDK_APPLICATION_MK=../jni/Application.mk"
//        )
//    }
//    tasks.withType(org.jetbrains.kotlin.gradle.tasks.KotlinCompile::class) {
//        dependsOn(CompileJNI)
//    }
//    tasks.named("preBuild") {
//        dependsOn(CompileJNI)
//    }

}
dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(project(":libraries:base-ui"))
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
    implementation(Libs.mvRx)

    testImplementation("junit:junit:4.13")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
if (file("google-services.json").exists()) {
    plugins {
        id("com.google.gms.google-services")
    }

}

