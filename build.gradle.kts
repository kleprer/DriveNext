// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.6.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.23" apply false  // CHANGED from 2.2.20 to 1.9.23
    id("com.google.devtools.ksp") version "1.9.23-1.0.20" apply false
    id("com.android.library") version "8.6.0" apply false  // UPDATED
    id("com.google.gms.google-services") version "4.4.0" apply false
}