plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("com.google.dagger.hilt.android")
    id ("kotlin-kapt")
}

android {
    namespace = "com.gootax.feedme"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gootax.feedme"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    // VIEW BINDING
    buildFeatures{
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // DAGGER HILT
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-android-compiler:2.51.1")

    // RETROFIT
    implementation ("com.squareup.retrofit2:retrofit:2.10.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // GSON
    implementation ("com.google.code.gson:gson:2.11.0")

    // VIEW MODEL
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation ("androidx.activity:activity-ktx:1.9.3")

    // JETPACK NAVIGATION COMPONENT
    implementation ("androidx.navigation:navigation-fragment-ktx:2.8.4")
    implementation ("androidx.navigation:navigation-ui-ktx:2.8.4")
    implementation ("androidx.fragment:fragment-ktx:1.8.5")

    // COROUTINES
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")

    // ROOM
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}