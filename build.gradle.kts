buildscript {
    val agp_version by extra("8.0.0")
    val agp_version1 by extra("8.1.2")
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")
    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
}