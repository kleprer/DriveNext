plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.kleprer.mobileapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kleprer.mobileapp"
        minSdk = 24  // CHANGED from 33 to support more devices
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        compose = true
    }


    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8  // CHANGED from 11 to 1_8
        targetCompatibility = JavaVersion.VERSION_1_8  // CHANGED from 11 to 1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"  // CHANGED from 11 to 1.8
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))  // CHANGED to compatible version

    // Room Database - UPDATED VERSIONS
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Coroutines & Lifecycle - UPDATED VERSIONS
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")

    // Networking - FIXED VERSIONS (Retrofit 3.0.0 doesn't exist!)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")  // CHANGED from 3.0.0
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")  // CHANGED from 3.0.0
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")  // UPDATED version

    // Google Sign-In
    implementation("com.google.android.gms:play-services-auth:20.7.0")  // UPDATED version

    // Remove the invalid ksp function at the bottom
}