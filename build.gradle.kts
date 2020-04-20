// Top-level build file where you can add configuration options common to all sub-projects/modules.
import com.mtem.buildsrc.Libs
buildscript {
val kotlin_version by extra("1.3.71")
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

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        jcenter()

    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
