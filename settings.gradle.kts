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
include(":core")
include(":fastlane")
include(":applications:TravelApp:core:network")
include(":applications:TravelApp:core:cache")
include(":core:location")
include(":navigation")
include(":applications:TravelApp:feature:home")
include(":applications:TravelApp:feature:search")
include(":applications:TravelApp:feature:favorites")
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

include(":applications:Travel Notification:feature:notification")
include(":applications:Travel Notification:core:pushing")
include(":applications:Travel Notification:core:uikit")
