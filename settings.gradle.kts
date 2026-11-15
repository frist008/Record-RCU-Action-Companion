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
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
                // Cronet of the Next-Gen Mobile Ads SDK
                includeGroupByRegex("org\\.chromium.*")
            }
        }
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = rootProject.projectDir.name

if (gradle.startParameter.taskNames.any { it.contains("dependencyUpdates") }) {
    gradle.startParameter.isParallelProjectExecutionEnabled = false
}

// Android
include(":app")
