import java.time.LocalDateTime
import java.io.ByteArrayOutputStream
import org.gradle.api.provider.Property 
plugins {
    java
    application
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral() 
}

dependencies {
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    implementation("org.slf4j:slf4j-api:2.0.9")
    
    implementation(project(":string-utils"))
    
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

application {
    mainClass.set("org.example.Main") 
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

tasks.shadowJar {
    manifest {
        attributes("Main-Class" to "org.example.Main")
    }
}

abstract class GenerateBuildInfoTask : DefaultTask() {
    
    @get:Input
    abstract val gitCommitHash: org.gradle.api.provider.Property<String>

    @TaskAction
    fun generate() {
        val counterFile = project.file("build-counter.txt")
        if (!counterFile.exists()) counterFile.writeText("0")
        
        val currentCount = counterFile.readText().trim().toInt()
        val nextCount = currentCount + 1
        counterFile.writeText(nextCount.toString())

        val outputFile = project.file("src/main/resources/build-passport.properties")
        outputFile.parentFile.mkdirs()
        
        val username = System.getenv("USERNAME") ?: System.getenv("USER") ?: "Unknown"
        val os = System.getProperty("os.name")
        val javaVer = System.getProperty("java.version")
        val timestamp = LocalDateTime.now().toString()

        outputFile.writeText("""
            build.number=$nextCount
            build.user=$username
            build.os=$os
            build.java.version=$javaVer
            build.date=$timestamp
            build.git.hash=${gitCommitHash.get()}
        """.trimIndent())
        
        println("Build #$nextCount generated with hash: ${gitCommitHash.get()}")
    }
}


val generateBuildInfo = tasks.register<GenerateBuildInfoTask>("generateBuildInfo") {
    gitCommitHash.set(providers.exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
    }.standardOutput.asText.map { it.trim() }.orElse("no-git"))
}

tasks.named("processResources") {
    dependsOn(generateBuildInfo)
}