plugins {
    id("com.gradleup.shadow")
}

dependencies {
    // Implementation scope ensures these modules are compiled and packed into the final JAR
    implementation(project(":api"))
    implementation(project(":module-admin"))
    implementation(project(":module-highlight"))
    implementation(project(":module-ranks"))
    implementation(project(":module-rules"))
    implementation(project(":module-lands"))
    implementation(project(":module-regions"))
    implementation(project(":module-wilderness"))
    implementation(project(":core"))
}

tasks {
    processResources {
        filteringCharset = "UTF-8"
        filesMatching("*.yml") {
            expand(
                "project" to project,
                "version" to project.version
            )
        }
    }

    shadowJar {
        // Keeps the output JAR name clean (e.g., ExcellentClaims-2.0.0.jar)
        archiveBaseName.set(rootProject.name)
        archiveClassifier.set("") 

        mergeServiceFiles()

        filesMatching("META-INF/services/**") {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }

    build {
        // Ensure the shadow task runs during a standard build
        dependsOn(shadowJar) 
    }
}