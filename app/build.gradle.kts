import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.dotodo"
    compileSdk = 34

    buildFeatures {
        buildConfig=true
        viewBinding=true
    }

    defaultConfig {
        applicationId = "com.example.dotodo"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())
        val geminiApiKey = properties.getProperty("GEMINI_API_KEY")
        buildConfigField("String", "GEMINI_API_KEY", "\"$geminiApiKey\"")

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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.google.code.gson:gson:2.9.0")
    implementation("androidx.room:room-runtime:2.5.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation("androidx.compose.ui:ui-text-google-fonts:1.5.4")
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    annotationProcessor("androidx.room:room-compiler:2.5.0")
    implementation ("androidx.recyclerview:recyclerview:1.3.2")
    implementation ("androidx.slidingpanelayout:slidingpanelayout:1.2.0")
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}