import org.jetbrains.kotlin.resolve.calls.model.ResolvedCallArgument.DefaultArgument.arguments

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}


android {
    compileSdkVersion(29)
    buildToolsVersion("29.0.3")
    defaultConfig {
        applicationId = "com.mteam.movietrailer"
        minSdkVersion(16)
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

    sourceSets {
        getByName("debug") {
          //  jniLibs.srcDir("../jniLibs")
        }
        getByName("release") {

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
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.71")
    implementation("androidx.core:core-ktx:1.2.0")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("com.google.android.material:material:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}

