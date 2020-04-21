import com.mtem.buildsrc.Libs

plugins {
    kotlin("jvm")
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "1.8"
    }
}
dependencies {
    implementation(Libs.Kotlin.stdlib)
    implementation(Libs.AndroidX.coreKtx)
}