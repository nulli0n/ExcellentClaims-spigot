plugins {
    `java-library`
    `maven-publish`
    // Declare the Shadow plugin here but do not apply it globally. 
    // We only want to apply it to the final 'plugin' module.
    id("com.gradleup.shadow") version "9.4.1" apply false
}

allprojects {
    group = "su.nightexpress.excellentclaims"
    version = "2.0.0"

    // Redirect all build outputs to a centralized, hidden directory at the root.
    // This prevents VS Code from mistakenly linking to compiled submodule JARs.
    layout.buildDirectory.set(rootProject.layout.projectDirectory.dir(".gradle-out/${project.name}"))
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/") { name = "papermc" }
        maven("https://repo.nightexpressdev.com/releases") { name = "nightexpress-releases" }
        maven("https://jitpack.io") { name = "jitpack" }
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") { name = "placeholderapi" }
    }

    dependencies {
        compileOnly("su.nightexpress.nightcore:main:2.16.2")
        compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
        withSourcesJar()
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    configure<PublishingExtension> {
        publications {
            create<MavenPublication>("mavenJava") {
                artifact(tasks.named("jar"))
                artifact(tasks.named("sourcesJar")) {
                    classifier = "sources"
                }
                artifactId = project.name
            }
        }
        repositories {
            maven {
                name = "nightexpress"
                url = uri("https://repo.nightexpressdev.com/releases")
                credentials {
                    username = System.getenv("REPOSILITE_USER")
                    password = System.getenv("REPOSILITE_PASSWORD")
                }
            }
        }
    }
}