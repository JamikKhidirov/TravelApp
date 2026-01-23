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
include(":app")
include(":data")
include(":domain")
include(":core")
include(":core:network")
include(":core:cache")
include(":core:location")
include(":navigation")
include(":feature")
include(":feature:home")
include(":feature:search")
include(":core:common")
include(":core:uikit")
