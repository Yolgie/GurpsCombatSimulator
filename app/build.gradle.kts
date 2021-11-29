plugins {
    kotlin("jvm") version "1.4.32"
    application
    idea
    jacoco

    id("org.sonarqube") version "3.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.17.1"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(platform(kotlin("bom")))
    implementation(kotlin("stdlib-jdk8"))

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.7.1")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    testImplementation("io.mockk:mockk:1.11.0")
}

application {
    mainClass.set("at.cnoize.gcs.app.AppKt")
}

tasks.withType<Test> {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.isEnabled = true
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

sonarqube {
    properties {
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.projectKey", "GurpsCombatSimulator")
        property("sonar.organization", "yolgie")
    }
}
