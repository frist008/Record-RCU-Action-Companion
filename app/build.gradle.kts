import com.github.triplet.gradle.androidpublisher.ReleaseStatus
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.milliseconds

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.di.hilt)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.compose.compiler)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
    alias(libs.plugins.licensee)
    alias(libs.plugins.play.publisher)
    alias(libs.plugins.oss.licenses)
}

// Google Play: the bundle goes as a draft to the internal track, or to the track `-PplayTrack`
// names. The key of the service account lies in `play-credentials.json` of the root, outside git;
// without the file the plugin reads the key from the `ANDROID_PUBLISHER_CREDENTIALS` environment
// variable.
play {
    val credentials = rootProject.file("play-credentials.json")

    if (credentials.exists()) {
        serviceAccountCredentials.set(credentials)
    }

    defaultToAppBundles.set(true)
    track.set(providers.gradleProperty("playTrack").orElse("internal"))
    releaseStatus.set(ReleaseStatus.DRAFT)
}

licensee {
    allow("Apache-2.0")
    allow("MIT")
    allow("BSD-3-Clause")
    allowUrl("https://developer.android.com/studio/terms.html")
}

android {
    val currentTime = System.currentTimeMillis().milliseconds

    compileSdk {
        version = release(libs.versions.sdk.compile.asProvider().get().toInt()) {
            minorApiLevel = libs.versions.sdk.compile.minor.get().toInt()
        }
    }
    // uncomment for 37
    // compileSdkExtension = libs.versions.sdk.compile.extension.get().toInt()
    buildToolsVersion = libs.versions.build.tools.version.get()

    defaultConfig {
        applicationId = "ua.frist008.action.record"
        namespace = applicationId
        minSdk = libs.versions.sdk.min.get().toInt()
        targetSdk = libs.versions.sdk.target.get().toInt()
        versionCode = (currentTime - (365 * 54).days).inWholeMinutes.toInt()
        versionName = libs.versions.version.name.get()

        // https://developer.android.com/guide/topics/resources/app-languages#gradle-config
        androidResources.localeFilters += listOf(
            "en", "ru", "uk",
            "de", "nl", "fr", "es", "it", "pl", "sv",
            "tr", "el", "lv",
            "pt-rBR",
            "th", "hi", "fil",
            "ar",
            "zh-rCN", "zh-rTW",
        )

        testInstrumentationRunner = "ua.frist008.action.record.HiltTestRunner"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    signingConfigs {
        getByName(libs.versions.build.type.debug.get()) {
            storeFile = file("../keystore/${libs.versions.build.type.debug.get()}.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
        create(libs.versions.build.type.release.get()) {
            storeFile = file("../../keystore/${libs.versions.build.type.release.get()}.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-${libs.versions.build.type.debug.get()}"
            signingConfig = signingConfigs.getByName(libs.versions.build.type.debug.get())
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true

            resValue("bool", "firebase_performance_enabled", "true")
            resValue("bool", "firebase_analytics_enabled", "false")
            resValue("bool", "firebase_crashlytics_enabled", "false")
        }
        release {
            signingConfig = signingConfigs.getByName(libs.versions.build.type.release.get())
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            resValue("bool", "firebase_performance_enabled", "false")
            resValue("bool", "firebase_analytics_enabled", "true")
            resValue("bool", "firebase_crashlytics_enabled", "true")
        }
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        targetCompatibility(libs.versions.jvm.target.asProvider().get().toInt())
        sourceCompatibility(libs.versions.jvm.target.asProvider().get().toInt())
    }

    buildFeatures {
        buildConfig = true
        resValues = true
    }

    // https://developer.android.com/guide/topics/resources/app-languages#auto-localeconfig
    androidResources {
        generateLocaleConfig = true
    }

    testOptions {
        animationsDisabled = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvm.target.kotlin.get()))

        optIn.addAll(
            "androidx.compose.material3.ExperimentalMaterial3Api",
            "androidx.compose.foundation.ExperimentalFoundationApi",
            "androidx.compose.foundation.layout.ExperimentalLayoutApi",
            "androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi",
        )
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
    debugImplementation(libs.bundles.compose.tooling)

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
    implementation(libs.ads.asProvider())
    implementation(libs.ads.consent)
    implementation(libs.billing)
    implementation(platform(libs.kotlin.coroutines.bom))
    implementation(libs.bundles.coroutines.bom)
    implementation(libs.data.store)
    implementation(libs.ktor.network)

    // Util
    implementation(libs.oss.licenses)
    coreLibraryDesugaring(libs.jdk.desugar)
    implementation(libs.leakcanary)
    debugImplementation(libs.leakcanary.debug)
    implementation(libs.timber)

    // Test
    testImplementation(platform(libs.kotlin.coroutines.bom))
    testImplementation(libs.bundles.test)

    // AndroidTest
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.bundles.android.test)
    kspAndroidTest(libs.di.hilt.compiler)
    debugImplementation(libs.compose.test.manifest)
}
