# State Machine

This is a public Maven library which help users to make stateful stuffs to change their state
easily.

## Latest Release

// TODO

## Prerequisites

* GitHub Access Token
  ### How do we generate a read-only GitHub token?
    1. Go to your personal GitHub page and click the upper-right profile.
    2. Click "Settings"
    3. On the sidebar, click "Developer Settings"
    4. Click "Token (Classic)" under the toggle of "Personal Access Token"
    5. Click "Generate new token (classic)"
    6. Set the expiration period & remember to choose `read:packages`
    7. Keep this token and it will be used afterward

## Installation

1. Import the dependency
    * For Maven users (`pom.xml`),
      ```xml
      <dependencies>
        <dependency>
          <groupId>org.general.state</groupId>
          <artifactId>state-machine</artifactId>
          <version>1.1.0</version>
        </dependency>
      </dependencies>
      ```

    * For Gradle users, (`build.gradle.kts` & `build.gradle`)
      ```
      dependencies {
        implementation("org.general.state:state-machine:1.2.0")
      }
      ```
      ```
      dependencies {
        implementation 'org.general.state:state-machine:1.2.0'
      }
      ```

2. Specify the repository
    * For maven users (`pom.xml`)
      ```xml
      <repositories>
        <repository>
          <id>carricaner-state-machine</id>
          <url>https://maven.pkg.github.com/Carricaner/state-machine</url>
          <name>carricaner-state-machine</name>
          <snapshots>
            <enabled>true</enabled>
          </snapshots>
        </repository>
      </repositories>
      ```

    * For Gradle users, (`build.gradle.kts` & `build.gradle`)
      ```
      repositories {
        mavenLocal()
        maven {
          name = "carricaner-state-machine"
          url = uri("https://maven.pkg.github.com/Carricaner/state-machine")
          credentials {
            username = project.findProperty("gpr.user") as String?
            password = project.findProperty("gpr.key") as String?
          }
        }
      }
      ```
      ```
      repositories {
        mavenLocal()
        maven {
          name = 'carricaner-state-machine'
          url = uri('https://maven.pkg.github.com/Carricaner/state-machine')
          credentials {
            username = project.findProperty('gpr.user') as String?
            password = project.findProperty('gpr.key') as String?
          }
        }
      }
      ```

3. Provide your GitHub username & GitHub Access Token
    * For maven users (`pom.xml`)
        1. Create a file named `custom.properties` under the root
        2. Inside `custom.properties` set below properties
           ```
           gpr.user=<Your GitHub Username>
           gpr.key=<GitHub Access Token>
           ```
        3. Use the properties inside `pom.xml`,
           ```
           <properties>
             <gpr.user>${property.from.external.file}</gpr.user>
           </properties>
           ```
    * For Gradle users, (`build.gradle.kts` & `build.gradle`)
        1. Create a file named `gradle.properties` under the root
        2. Inside `gradle.properties` set below properties
           ```
           gpr.user=<Your GitHub Username>
           gpr.key=<GitHub Access Token>
           ```

## Documentation

Please refer to the GitHub wiki
