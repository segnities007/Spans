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

rootProject.name = "Spans"
include(":app")
include(":domain:model")
include(":domain:usecase")
include(":domain:repository")
include(":data:repository")
include(":presentation:navigation")
include(":presentation:mvi")
include(":presentation:ui:auth")
include(":presentation:ui:hub")
include(":presentation:ui:timeline")
include(":presentation:ui:post")
include(":presentation:ui:search")
include(":presentation:ui:profile")
include(":presentation:ui:settings")
include(":di")
include(":util")
include(":presentation:ui:component")
include(":data:local:db")
include(":data:remote")
