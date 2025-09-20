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
include(":data:local")
include(":data:remote")
include(":data:repository")
include(":presentation:auth")
include(":core:ui")
include(":presentation:plaza")
include(":presentation:setting")
include(":presentation:timeline")
include(":core:navigation")
include(":core:mvi")
