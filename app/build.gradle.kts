plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
    id("org.jetbrains.kotlin.kapt") // This enables kapt
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.productfinder"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.productfinder"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.appcompat:appcompat:1.7.0")  // или актуальная версия



    implementation("com.vanniktech:android-image-cropper:4.6.0")

    implementation(libs.com.squareup.retrofit2)
    implementation(libs.json.converter)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp)
    implementation (libs.converter.moshi)

    implementation (libs.coil.compose)

    implementation (libs.hilt.android)
    kapt (libs.hilt.compiler)
    kapt(libs.room.compiler)

    implementation (libs.androidx.hilt.navigation.compose)
    implementation ("com.google.accompanist:accompanist-flowlayout:0.20.0")

    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    implementation("com.airbnb.android:lottie-compose:6.4.0")

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)


}
