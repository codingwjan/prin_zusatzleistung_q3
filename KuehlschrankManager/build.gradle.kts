plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("com.mysql:mysql-connector-j:8.3.0")
    implementation("org.slf4j:slf4j-simple:2.0.7") // Optional, suppresses SLF4J warning
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("fridge.Main")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}