plugins {
    id("java")
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    `maven-publish`
}

group = "com.example"
version = "1.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "state-machine"
            url = uri("https://maven.pkg.github.com/Carricaner/my-first-library")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
            // Replace above credential with below one when you wanna publish it locally
            // 1. Add gradle.properties under the root
            // 2. Replace the credential with below one
            // credentials {
            //     username = project.findProperty("gpr.user") as String? ?: System.getenv("GPR_USER")
            //     password = project.findProperty("gpr.key") as String? ?: System.getenv("GPR_KEY")
            // }
        }
    }
    publications {
        register<MavenPublication>("state-machine") {
            from(components["java"])
        }
    }
}