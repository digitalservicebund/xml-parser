import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    id("com.diffplug.spotless") version "6.22.0"
    id("jacoco")
    id("org.sonarqube") version "4.4.1.3373"
    id("com.adarshr.test-logger") version "4.0.0"
    id("maven-publish")
}

group = "de.bund.digitalservice"
version = "0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

jacoco {
    toolVersion = "0.8.8"
}

testlogger { theme = com.adarshr.gradle.testlogger.theme.ThemeType.MOCHA }

dependencies {
    val junitVersion = "5.10.0"
    val commonsTextVersion = "1.10.0"
    val assertJVersion = "3.24.2"
    val jacksonVersion = "2.15.2"

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("org.apache.commons:commons-text:$commonsTextVersion")

    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")

    testImplementation("org.assertj:assertj-core:$assertJVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$junitVersion")

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "17"
        }
    }

    withType<Test> {
        useJUnitPlatform {
            excludeTags("integration")
        }
    }

    register<Test>("integrationTest") {
        description = "Runs the integration tests."
        group = "verification"
        useJUnitPlatform {
            includeTags("integration")
        }

        // So that running integration test require running unit tests first,
        // and we won"t even attempt running integration tests when there are
        // failing unit tests.
        dependsOn(test)
        finalizedBy(jacocoTestReport)
    }
    check {
        dependsOn(getByName("integrationTest"))
    }

    jacocoTestReport {
        // Jacoco hooks into all tasks of type: Test automatically, but results for each of these
        // tasks are kept separately and are not combined out of the box... we want to gather
        // coverage of our unit and integration tests as a single report!
        executionData.setFrom(
            files(
                fileTree(project.buildDir.absolutePath) {
                    include("jacoco/*.exec")
                },
            ),
        )

        // Prevent Spring Boot + Kotlin compilation artifact skewing coverage!
        classDirectories.setFrom(
            files(
                classDirectories.files.map {
                    fileTree(it) {
                        exclude("**/constants/**")
                    }
                },
            ),
        )

        reports {
            xml.required.set(true)
            html.required.set(true)
        }

        dependsOn(getByName("integrationTest")) // All tests are required to run before generating a report..
    }

    sonarqube {
        // NOTE: sonarqube picks up combined coverage correctly without further configuration from:
        // build/reports/jacoco/test/jacocoTestReport.xml
        properties {
            property("sonar.projectKey", "digitalservice_xml-parser")
            property("sonar.organization", "digitalservice")
            property("sonar.host.url", "https://sonarcloud.io")
        }
    }
    getByName("sonarqube") {
        dependsOn(jacocoTestReport)
    }
}

spotless {
    kotlin {
        ktlint()
    }
    kotlinGradle {
        ktlint()
    }
    format("misc") {
        target(
            "**/*.js",
            "**/*.json",
            "**/*.md",
            "**/*.properties",
            "**/*.sh",
            "**/*.yml",
        )
        prettier(
            mapOf(
                "prettier" to "2.6.1",
                "prettier-plugin-sh" to "0.7.1",
                "prettier-plugin-properties" to "0.1.0",
            ),
        ).config(mapOf("keySeparator" to "="))
    }
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/digitalservicebund/xml-parser")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            groupId = "de.bund.digitalservice"
            artifactId = "xml-parser"
            version = "0.1"
        }
    }
}
