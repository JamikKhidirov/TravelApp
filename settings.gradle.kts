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
include(":applications:TravelApp:core:network")
include(":applications:TravelApp:core:cache")
include(":core:location")
include(":navigation")
include(":features")
include(":applications:TravelApp:feature:home")
include(":applications:TravelApp:feature:search")
include(":applications:TravelApp:feature:favorites")
include(":features:TravelApp")
include(":features:TravelNotification")
include(":core:common")
include(":core:uikit")
include(":core:sync")
include(":core:worker")
include(":applications")
include(":applications:Travel Notification")
include(":applications:TravelApp:feature")
include(":applications:TravelApp:navigation")
include(":applications:TravelApp:core")
include(":applications:Travel Notification:core")
include(":applications:Travel Notification:feature")


