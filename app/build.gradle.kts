plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("io.objectbox")
}

android {
    namespace = "com.blood.bloodthings"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.blood.bloodthings"
        minSdk = 24
        targetSdk = 33
        versionCode = 2
        versionName = "1.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
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
        viewBinding = true
        buildConfig = true
    }
    packagingOptions.jniLibs.useLegacyPackaging = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.animation)
    implementation(project(":Youth"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("io.objectbox:objectbox-android:4.0.0")
    implementation("io.objectbox:objectbox-kotlin:4.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.0")
    implementation("com.github.megatronking.stringfog:xor:5.0.0")
    implementation("com.facebook.android:facebook-android-sdk:12.3.0")

    implementation("com.adjust.sdk:adjust-android:4.38.3")
    implementation("com.android.installreferrer:installreferrer:2.2")
    implementation("com.google.android.gms:play-services-appset:16.0.2")
}