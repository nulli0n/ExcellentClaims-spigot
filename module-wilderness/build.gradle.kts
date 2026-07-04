dependencies {
    // Implementation scope ensures these modules are compiled and packed into the final JAR
    implementation(project(":api"))
    implementation(project(":core"))
}