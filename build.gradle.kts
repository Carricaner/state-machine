plugins {
    id("java")
    id("maven-publish")
    // It is prohibited to add `version` for any plugins added here
}

group = "org.general.state"
version = "1.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")

    // Functional programming
    val vavrVersion = "0.10.4";
    implementation("io.vavr:vavr:$vavrVersion")
    implementation("io.vavr:vavr-test:$vavrVersion")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    repositories {
        maven {
            name = "state-machine"
            url = uri("https://maven.pkg.github.com/Carricaner/state-machine")
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