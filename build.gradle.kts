plugins {
    id("java")
    id("org.springframework.boot") version "2.7.0"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("maven-publish")
}

val myGroupId = "com.example"
val myArtifactId = "state-machine"
val myVersion = "1.0.0"

group = myGroupId
version = myVersion

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

// publishing is not working now... :(
//publishing {
//    publications {
//        create<MavenPublication>("mavenJava") {
//            from(components["java"])
//            groupId = myGroupId
//            artifactId = myArtifactId
//            version = myVersion
//        }
//    }
//    repositories {
//        maven {
//            url = uri("${System.getProperty("user.home")}/.m2/repository")
//        }
//    }
//}