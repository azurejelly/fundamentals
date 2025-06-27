import java.util.*

plugins {
    alias(libs.plugins.runPaper)
    alias(libs.plugins.shadow)
    alias(libs.plugins.bukkitPluginYAML)
    alias(libs.plugins.indra)
}

val pluginName = findProperty("pluginName").toString()
val pluginDescription = findProperty("pluginDescription")?.toString()
val pluginWebsite = findProperty("pluginWebsite")?.toString()

if (pluginName.isEmpty()) {
    throw GradleException("The pluginName property cannot be empty")
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.triumphteam.dev/snapshots/")
    maven("https://jitpack.io")
}

dependencies {
    implementation(project(":api"))
    implementation(libs.triumph.cmd)
    implementation(libs.fastboard)

    compileOnly(libs.vault)
    compileOnly(libs.miniplaceholders)
    compileOnly(libs.paper)
}

tasks {
    jar {
        finalizedBy("shadowJar")
        archiveFileName.set(pluginName + "-" + archiveVersion.get() + "-original.jar")

        manifest.attributes(mapOf<String, String>(
            "Build-Date" to (Date().toString()),
            "Git-Revision" to (if (indraGit.isPresent) (indraGit.commit()?.name() ?: "") else ""),
            "Git-Branch" to (if (indraGit.isPresent) indraGit.branchName() ?: "" else ""),
            "Build-Number" to (System.getenv("GITHUB_RUN_NUMBER") ?: ""),
            "Build-Origin" to (
                    if (System.getenv("RUNNER_NAME") != null)
                        "GitHub Actions: " + System.getenv("RUNNER_NAME")
                    else (System.getProperty("user.name") ?: "Unknown"))
        ))
    }

    shadowJar {
        archiveFileName.set(pluginName + "-" + archiveVersion.get() + ".jar")
        archiveClassifier.set("shaded")

        mustRunAfter("build")
        mergeServiceFiles()

        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
        exclude("META-INF/maven/**")
    }

    runServer {
        minecraftVersion("1.21.5")
        jvmArgs("-Dcom.mojang.eula.agree=true")

        downloadPlugins {
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
        }
    }
}

bukkit {
    name = pluginName
    description = pluginDescription
    version = project.version.toString()
    main = "${project.group}.${pluginName}"
    website = pluginWebsite
    apiVersion = "1.13"
    authors = listOf("azurejelly")
    softDepend = listOf("Vault", "MiniPlaceholders")
}