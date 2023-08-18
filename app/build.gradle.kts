plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") // added for ksp plugin
    id("kotlin-kapt")  //added Kotlin kapt plugin
    id("androidx.navigation.safeargs") //safe args plugin for navigation component
    id("com.google.dagger.hilt.android") //added for HILT
    id("kotlin-parcelize")  //reqd for parcelable way of passing object from a->a or f->f
}

android {
    namespace = "com.rounak.testapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.rounak.testapp"
        minSdk = 21
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isDebuggable = true
            buildConfigField("String", "BASE_URL", "\"https://jsonplaceholder.typicode.com/\"")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isDebuggable = true
            buildConfigField("String", "BASE_URL", "\"https://jsonplaceholder.typicode.com/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    //enable data binding
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.appcompat:appcompat:1.6.1")

    //mdc
    implementation("com.google.android.material:material:1.6.1")

    //constraint layout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")


    //Navigation Component Dependencies-------------------------------
    implementation("androidx.navigation:navigation-fragment-ktx:2.6.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.6.0")

    //ssp
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    //sdp
    implementation("com.intuit.sdp:sdp-android:1.0.6")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    // kapt("com.github.bumptech.glide:compiler:4.14.2")
    ksp("com.github.bumptech.glide:ksp:4.14.2")

    //HILT
    implementation("com.google.dagger:hilt-android:2.47")
    kapt("com.google.dagger:hilt-compiler:2.47")

    //recycler view
    implementation("androidx.recyclerview:recyclerview:1.3.1")


    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")


    // Retrofit dependency
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //Gson dependency
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Logging interceptor dependency
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.1")

    //Room related dependencies--------------
    implementation("androidx.room:room-runtime:2.5.2")
    ksp("androidx.room:room-compiler:2.5.2")
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.5.2")


    // for testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

//added for HILT
kapt {
    correctErrorTypes = true
}

//added for HILT
hilt {
    enableAggregatingTask = true
}