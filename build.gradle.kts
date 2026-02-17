plugins {
    id("java")
    id("org.jetbrains.gradle.plugin.idea-ext") version "1.3"
    id("com.gradleup.shadow") version "9.3.1"
    id("run-hytale")
}

group = findProperty("pluginGroup") as String? ?: "com.jjeanniard.plugins"
version = findProperty("pluginVersion") as String? ?: "1.0.0"
description = findProperty("pluginDescription") as String? ?: "A Hytale plugin template"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven {
        name = "hytale"
        url = uri("https://maven.hytale.com/release") // Or "hytale-pre-release" for pre-release versions
    }
}

dependencies {
    val isCi = System.getenv("CI") == "true"

    if (isCi) {
        // Pour GitHub Actions
        compileOnly("com.hypixel.hytale:Server:+")
    } else {
        // Pour votre environnement local
        compileOnly(files("C:\\Users\\jonat\\AppData\\Roaming\\Hytale\\install\\release\\package\\game\\latest\\Server\\HytaleServer.jar"))
    }

    // Dépendances communes
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("com.google.code.gson:gson:2.10.1")
}

// Configure server testing
runHytale {
    // TODO: Update this URL when Hytale server is available
    // Using Paper server as placeholder for testing the runServer functionality
    jarUrl =
        "C:\\Users\\jonat\\AppData\\Roaming\\Hytale\\install\\release\\package\\game\\latest\\Server\\HytaleServer.jar"
    assetsPath = "C:\\Users\\jonat\\AppData\\Roaming\\Hytale\\install\\release\\package\\game\\latest\\Assets.zip"
}

tasks {
    // Configure Java compilation
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release = 25
    }

    // Configure resource processing
    processResources {
        filteringCharset = Charsets.UTF_8.name()

        // Replace placeholders in manifest.json
        val props = mapOf(
            "group" to project.group,
            "version" to project.version,
            "description" to project.description
        )
        inputs.properties(props)

        filesMatching("manifest.json") {
            expand(props)
        }
    }

    // Configure ShadowJar (bundle dependencies)
    shadowJar {
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("")

        // Relocate dependencies to avoid conflicts
        relocate("com.google.gson", "com.example.myplugin.libs.gson")

        // Minimize JAR size (removes unused classes)
        minimize()
    }

    // Configure tests
    test {
        useJUnitPlatform()
    }

    // Make build depend on shadowJar
    build {
        dependsOn(shadowJar)
    }
}

// Configure Java toolchain
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
