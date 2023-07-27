plugins {
    kotlin("jvm") version "1.8.0"
}

group = "com.cosine.swamp"
version = "1.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("com.cosine.library", "SwampLibrary", "1.1")

    compileOnly("org.spigotmc", "spigot", "1.19.4-R0.1-SNAPSHOT")

    compileOnly("org.zeroturnaround", "zt-zip", "1.15")

    compileOnly("io.vertx", "vertx-web", "4.4.4")

    testImplementation(kotlin("test"))
}

tasks {
    test {
        useJUnitPlatform()
    }
    jar {
        archiveFileName.set("${rootProject.name}-${project.version}.jar")
        destinationDirectory.set(File("D:\\서버\\1.19.4 - 타르코프(개발)\\plugins"))
    }
}