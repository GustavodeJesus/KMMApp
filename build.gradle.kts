buildscript {

    val kotlinVersion = "1.4.0"
    val sqlDelightVersion: String by project


    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.4.0")
        classpath("com.android.tools.build:gradle:4.0.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:$kotlinVersion")
        classpath("com.squareup.sqldelight:gradle-plugin:$sqlDelightVersion")
    }
}
group = "dev.gustavo.kmmapp"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}
