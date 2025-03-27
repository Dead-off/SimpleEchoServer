import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2025.03"

project {
    description = "echo server project"

    // Define VCS root using DslContext.settingsRoot
    val vcsRoot = DslContext.settingsRoot

    // Define build configuration
    buildType {
        id("EchoServerBuild")
        name = "Build Echo Server"

        // VCS settings
        vcs {
            root(vcsRoot)
        }

        // Build steps
        steps {
            maven {
                name = "Build and Package"
                goals = "clean package"
                runnerArgs = "-Dmaven.test.failure.ignore=true"
                userSettingsSelection = "local-proxy"
            }
        }

        // Triggers
        triggers {
            vcs {
                branchFilter = "+:*"
            }
        }

        // Features
        features {
            perfmon {
            }
        }

        // Artifacts
        artifactRules = """
            target/*.jar
            target/*-jar-with-dependencies.jar => artifacts
        """.trimIndent()
    }
}
