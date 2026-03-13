plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)

    id("dagger.hilt.android.plugin")
    kotlin("kapt")
}



android {
    namespace = "com.example.network"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

}



dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Hilt WorkManager

    api(libs.androidx.hilt.work)
    api(libs.androidx.work.runtime.ktx)


    // DI
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Network
    api(libs.retrofit)
    api(libs.converter.gson)
    api(libs.logging.interceptor)

    debugImplementation(libs.library)


    implementation(project(":core:common"))
    implementation(project(":domain"))

}

