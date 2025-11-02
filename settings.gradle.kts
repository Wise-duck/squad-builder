rootProject.name = "SquadBuilder"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")

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
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
    }
}

include(":app")

include(":core:common")
include(":core:data:api")
include(":core:data:impl")
include(":core:datastore:api")
include(":core:datastore:impl")
include(":core:designsystem")
include(":core:model")
include(":core:ui")
include(":core:network")

include(":feature:main")
include(":feature:settings")
include(":feature:screens")
include(":feature:login")
include(":feature:home")
include(":feature:webview")
include(":feature:edit")
include(":feature:detail")

