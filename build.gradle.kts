plugins {
    id("java-library")
}

group = "net.pyrocube"
version = "v${project.property("version")}"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.jetbrains:annotations:26.0.2-1")
}

java {
    withSourcesJar()
    withJavadocJar()
}