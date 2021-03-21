plugins {
    kotlin("jvm") version "1.4.30"
    application
    idea

    id("io.gitlab.arturbosch.detekt") version "1.16.0"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
    mainClass.set("at.cnoize.gcs.app.AppKt")
}
