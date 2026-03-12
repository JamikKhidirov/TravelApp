pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "TravelApp"
include(":applications:TravelApp")
include(":data")
include(":domain")
include(":core")
include(":core:network")
include(":core:cache")
include(":core:location")
include(":navigation")
include(":features")
include(":features:TravelApp:home")
include(":features:TravelApp:search")
include(":features:TravelApp:favorites")
include(":features:TravelApp")
include(":features:TravelNotification")
include(":core:common")
include(":core:uikit")
include(":core:sync")
include(":core:worker")
include(":applications")
include(":applications:Travel Notification")
