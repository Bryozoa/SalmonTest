plugins {
    kotlin("jvm") version "1.8.22"
    id("io.qameta.allure") version "2.12.0"
    id("io.qameta.allure-adapter") version "2.11.2"
}

repositories {
    mavenCentral()
    maven { url = uri("https://plugins.gradle.org/m2/") }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("io.qameta.allure:allure-java-commons:2.22.0")
    implementation("io.qameta.allure:allure-rest-assured:2.22.0")
    testImplementation("io.qameta.allure:allure-junit5:2.22.0")
    testImplementation("io.rest-assured:rest-assured:5.3.0")
    implementation("commons-codec:commons-codec:1.15")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    testImplementation ("org.hamcrest:hamcrest:3.0")

    // RestAssured for API requests
    implementation("io.rest-assured:rest-assured:5.3.0")

    // Optional: Jackson for JSON processing
    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

    // SLF4J for logging (simple binding)
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("org.slf4j:slf4j-simple:2.0.9")

    // JUnit for testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")

    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")

}

tasks.test {
    useJUnitPlatform()
}

allure {
    version.set("2.22.0")
}