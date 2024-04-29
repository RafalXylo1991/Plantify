plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.plantify"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.plantify"
        minSdk = 24
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        getByName("main") {
            res {
                srcDirs(
                    "src\\main\\res",
                    "src\\main\\res\\notices",
                    "src/main/res/layouts/notices/text",
                    "src/main/res/layouts/notices/picture")
            }
        }
    }
    buildFeatures {
        viewBinding = true
    }
}



dependencies {
    implementation("org.wso2.apache.httpcomponents:httpclient:4.3.1.wso2v1")

    implementation("com.drewnoakes:metadata-extractor:2.18.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation("com.jsibbold:zoomage:1.3.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-logging:commons-logging:1.3.1")
    implementation("org.apache.directory.studio:org.apache.commons.io:2.4")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.12")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("org.jsoup:jsoup:1.7.2")
    implementation("androidx.credentials:credentials:1.3.0-alpha01")
    implementation("com.github.bumptech.glide:glide:4.11.0")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.android.gms:play-services-cronet:18.0.1")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.facebook.android:facebook-android-sdk:16.3.0")
    implementation("androidx.core:core-splashscreen:1.0.0-alpha01")
    implementation("com.facebook.android:facebook-share:16.3.0")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0-alpha01")
    implementation("com.github.skydoves:balloon:1.6.3")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.google.android.material:material:1.3.0-alpha02")
    implementation("javax.json:javax.json-api:1.0-b01")
    implementation("com.tomergoldst.android:tooltips:1.0.11")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("pl.droidsonroids.gif:android-gif-drawable:1.2.3")
    implementation("com.applandeo:material-calendar-view:1.9.0-rc04")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.navigation:navigation-fragment:2.7.4")
    implementation("androidx.navigation:navigation-ui:2.7.4")
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.2")
    implementation("io.reactivex.rxjava3:rxjava:3.1.5")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}