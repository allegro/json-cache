rootProject.name = "json-cache"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

include("json-cache-test")
include("json-cache-core")
include("json-cache-jackson")
