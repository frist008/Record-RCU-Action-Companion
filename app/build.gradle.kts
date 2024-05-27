plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.di.hilt)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.google.services)
}

android {
    compileSdk = libs.versions.sdk.compile.asProvider().get().toInt()
    compileSdkExtension = libs.versions.sdk.compile.extension.get().toInt()
    buildToolsVersion = libs.versions.build.tools.version.get()

    defaultConfig {
        applicationId = "ua.frist008.action.record"
        namespace = applicationId
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = libs.versions.version.asProvider().get().toInt()
        versionName = libs.versions.version.name.get()

        // https://developer.android.com/guide/topics/resources/app-languages#gradle-config
        resourceConfigurations += listOf("en", "ru", "ua")

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true

            resValue("bool", "firebase_performance_logcat_enabled", "true")
            resValue("bool", "firebase_analytics_logcat_enabled", "true")

        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            resValue("bool", "firebase_performance_logcat_enabled", "false")
            resValue("bool", "firebase_analytics_logcat_enabled", "false")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        targetCompatibility(libs.versions.jvm.target.asProvider().get().toInt())
        sourceCompatibility(libs.versions.jvm.target.asProvider().get().toInt())
    }

    kotlinOptions {
        jvmTarget = libs.versions.jvm.target.kotlin.get()
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Kotlin
    implementation(platform(libs.kotlin.bom))
    implementation(libs.bundles.kotlin.bom)
    implementation(libs.bundles.kotlin.common)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)

    // UI
    implementation(libs.bundles.common)
    implementation(libs.bundles.ui)
    implementation(libs.bundles.navigation)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.bundles.firebase.bom)

    // Framework
    implementation(libs.bundles.di)
    ksp(libs.bundles.di.compiler)
    implementation(libs.bundles.multithreading)
    implementation(libs.bundles.network)

    // Util
    coreLibraryDesugaring(libs.jdk.desugar)
    implementation(libs.leakcanary)
    debugImplementation(libs.leakcanary.debug)
    implementation(libs.timber)
}
