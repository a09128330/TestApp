buildscript {
    dependencies {
        //Added for navigation component
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.6.0")

        //Added for HILT
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.0" apply false
    id("com.android.library") version "8.1.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.dagger.hilt.android") version "2.47" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}