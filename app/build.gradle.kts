plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.screenshot)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.quiltertask"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.quiltertask"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    experimentalProperties["android.experimental.enableScreenshotTest"] = true
    packaging {
        resources.excludes.addAll(
            listOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "/META-INF/{AL2.0,LGPL2.1}"
            )
        )
    }

    testOptions{
        unitTests{
            isIncludeAndroidResources = true
        }
    }
    kapt {
        correctErrorTypes = true
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))

    //ViewModel
    implementation(libs.androidx.viewmodel)
    implementation(libs.androidx.viewmodel.compose)
    implementation(libs.hilt.compose.viewmodel)

    //Hilt
    implementation(libs.hilt.android.core)
    kapt(libs.hilt.compiler)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.gson)
    implementation(libs.interceptor)
    implementation(libs.retrofit.adapter)

    //RxAndroidAndJava
    implementation(libs.androidx.rxandroid)
    implementation(libs.androidx.rxjava)
    //Coroutine
    implementation(libs.androidx.coroutine)
    implementation(libs.coroutine.core)
    implementation(libs.coroutine.rx3)
    //Coil
    implementation(libs.androidx.coil)
    //Testing
    testImplementation(libs.mockk)
    testImplementation(libs.kotlin.test.junit)
    androidTestImplementation(libs.androidx.mockk)
    androidTestImplementation(libs.androidx.test.runner)
    //ScreenShotTesting
    debugImplementation(libs.androidx.ui.tooling)
    screenshotTestImplementation(libs.androidx.ui.tooling)
    //ComposeTesting
    debugImplementation(libs.androidx.ui.test.manifest)
    androidTestImplementation(libs.androidx.ui.test.junit4)

    //ChuckerForLogging
    debugImplementation(libs.androidx.chucker)
    releaseImplementation(libs.androidx.no.chucker)
}