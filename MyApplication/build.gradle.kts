// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("org.sonarqube") version "3.5.0.2730"
}


sonar {
    properties {
        property("sonar.projectKey", "DBL-App-Dev")
        property("sonar.host.url", "http://localhost:9000")
        property ("sonar.login", "sqp_e2b43b2e19ac1bf34567e454e4692762f088ecc2")
    }
}


