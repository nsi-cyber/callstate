import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.android.kotlin.multiplatform.library)
    alias(libs.plugins.vanniktech.mavenPublish)
}

group = "io.github.nsi-cyber"
version = "1.0.0"

kotlin {
    androidLibrary {
        namespace = "io.github.nsicyber.callstate"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        withJava()
        withHostTestBuilder {}.configure {}
        withDeviceTestBuilder {
            sourceSetTreeName = "test"
        }

        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
    }
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
    }
}

mavenPublishing {
    publishToMavenCentral()

    signAllPublications()

    coordinates(group.toString(), "callstate", version.toString())

    pom {
        name = "CallState"
        description = "Kotlin Multiplatform library to observe whether the device is on an active call (Android and iOS)."
        inceptionYear = "2024"
        url = "https://github.com/nsi-cyber/callstate/"
        licenses {
            license {
                name = "XXX"
                url = "YYY"
                distribution = "ZZZ"
            }
        }
        developers {
            developer {
                id = "nsi-cyber"
                name = "nsi-cyber"
                url = "https://github.com/nsi-cyber"
            }
        }
        scm {
            url = "https://github.com/nsi-cyber/callstate"
            connection = "scm:git:git://github.com/nsi-cyber/callstate.git"
            developerConnection = "scm:git:ssh://git@github.com/nsi-cyber/callstate.git"
        }
    }
}
