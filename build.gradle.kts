@file:Suppress("UnstableApiUsage", "PropertyName")

import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import groovy.xml.slurpersupport.NodeChild
import groovy.xml.slurpersupport.NodeChildren
import org.apache.tools.ant.taskdefs.condition.Os
import java.io.FileNotFoundException

plugins {
    id("fabric-loom") version "1.10.0-bta"
    id("java")
    id("maven-publish")
}

val lwjglVersion = "3.3.4"

val lwjglNatives = when {
    Os.isFamily(Os.FAMILY_UNIX) && !Os.isFamily(Os.FAMILY_MAC) -> "natives-linux"
    Os.isFamily(Os.FAMILY_WINDOWS) -> "natives-windows"
    Os.isFamily(Os.FAMILY_MAC) -> "natives-macos"
    else -> error("Unsupported OS")
}

val mod_group: String by project
val mod_name: String by project
val mod_version: String by project

val bta_channel: String by project
val bta_version: String by project

val loader_version: String by project

val halplibe_version: String by project

val shade: Configuration by configurations.creating

group = mod_group
base.archivesName.set("cc-bta-$bta_version")
version = mod_version

loom {
    noIntermediateMappings()
    customMinecraftMetadata.set("https://downloads.betterthanadventure.net/bta-client/$bta_channel/v$bta_version/manifest.json")
}

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
    maven {
        name = "Babric"
        url = uri("https://maven.glass-launcher.net/babric")
    }
    maven {
        name = "Fabric"
        url = uri("https://maven.fabricmc.net/")
    }
    maven {
        name = "SignalumMavenInfrastructure"
        url = uri("https://maven.thesignalumproject.net/infrastructure")
    }
    maven {
        name = "SignalumMavenReleases"
        url = uri("https://maven.thesignalumproject.net/releases")
    }
    maven {
        name = "SquidDev"
        url = uri("https://squiddev.cc/maven")
    }
    ivy {
        url = uri("https://github.com/Better-than-Adventure")
        patternLayout {
            artifact("[organisation]/releases/download/v[revision]/[module].jar")
        }
        metadataSources { artifact() }
    }
    ivy {
        url = uri("https://downloads.betterthanadventure.net/bta-client/$bta_channel/")
        patternLayout {
            artifact("/v[revision]/client.jar")
        }
        metadataSources { artifact() }
    }
    ivy {
        url = uri("https://downloads.betterthanadventure.net/bta-server/$bta_channel/")
        patternLayout {
            artifact("/v[revision]/server.jar")
        }
        metadataSources { artifact() }
    }
    ivy {
        url = uri("https://piston-data.mojang.com")
        patternLayout {
            artifact("v1/[organisation]/[revision]/[module].jar")
        }
        metadataSources { artifact() }
    }

}

configurations {
    implementation {
        extendsFrom(shade)
    }
}

dependencies {
    minecraft("::${bta_version}")
    mappings(loom.layered {})

    modRuntimeOnly("objects:client:43db9b498cb67058d2e12d394e6507722e71bb45") // https://piston-data.mojang.com/v1/objects/43db9b498cb67058d2e12d394e6507722e71bb45/client.jar
    modImplementation("net.fabricmc:fabric-loader:$loader_version")

    modImplementation("turniplabs:halplibe:$halplibe_version")

    implementation("org.slf4j:slf4j-api:1.8.0-beta4")
    implementation("org.apache.logging.log4j:log4j-slf4j18-impl:2.16.0")

    shade("com.google.guava:guava:33.0.0-jre")
    implementation("com.google.code.gson:gson:2.10.1")
    val log4jVersion = "2.20.0"
    implementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    implementation("org.apache.logging.log4j:log4j-1.2-api:$log4jVersion")

    implementation("org.apache.commons:commons-lang3:3.12.0")
    include("org.apache.commons:commons-lang3:3.12.0")

    modImplementation("com.github.Better-than-Adventure:legacy-lwjgl3:1.0.5")
    implementation(platform("org.lwjgl:lwjgl-bom:$lwjglVersion"))

    runtimeOnly("org.lwjgl:lwjgl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-assimp::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-glfw::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-openal::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-opengl::$lwjglNatives")
    runtimeOnly("org.lwjgl:lwjgl-stb::$lwjglNatives")
    implementation("org.lwjgl:lwjgl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-assimp:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-glfw:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-openal:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-opengl:$lwjglVersion")
    implementation("org.lwjgl:lwjgl-stb:$lwjglVersion")

    shade("org.squiddev:Cobalt:0.5.2-SNAPSHOT")

    shade("io.netty:netty-buffer:4.1.95.Final")
    shade("io.netty:netty-common:4.1.95.Final")
    shade("io.netty:netty-transport:4.1.95.Final")
    shade("io.netty:netty-handler:4.1.95.Final")
    shade("io.netty:netty-codec:4.1.95.Final")
    shade("io.netty:netty-codec-http:4.1.95.Final")

    shade("com.electronwill.night-config:core:3.6.5")
    shade("com.electronwill.night-config:toml:3.6.5")

    implementation("commons-codec:commons-codec:1.16.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
    withSourcesJar()
}

tasks.compileJava {
    options.release.set(8)
}

configurations.configureEach {
    // Removes LWJGL2 dependencies
    exclude(group = "org.lwjgl.lwjgl")
}

tasks.jar {
    from("LICENSE") {
        rename { "${it}_${base.archivesName.get()}" }
    }

    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from(shade.map { zipTree(it) })
}

tasks.processResources {
    inputs.property("version", version)

    filesMatching("fabric.mod.json") {
        expand("version" to version)
    }
}

publishing {
    val publishVersion = "$mod_version-$bta_version"

    if(checkVersion("gungun974", mod_name, publishVersion)) {
        repositories {
            maven(url = uri("https://maven.thesignalumproject.net/releases")) {
                name = "signalumMaven"
                credentials(PasswordCredentials::class)
                authentication.create<BasicAuthentication>("basic")
            }
        }

        publications {
            create<MavenPublication>("maven") {
                groupId = "gungun974"
                artifactId = mod_name
                version = publishVersion

                from(components["java"])
            }
        }
    }
}

fun checkVersion(group: String, name: String, version: String): Boolean {
    try {
        val xml = uri("https://maven.thesignalumproject.net/releases/${group}/${name}/maven-metadata.xml").toURL().readText()
        val metadata = XmlSlurper().parseText(xml)

        val versioning: GPathResult = metadata["versioning"]
        val versions: NodeChildren = versioning["versions"]
        val versionValues = versions.children().map { (it as NodeChild).localText().first() }

        if (version in versionValues) {
            System.err.println("Version $version of $group.$name already exists!")
            return false
        }

        return true
    } catch (_: FileNotFoundException) {
        return true
    }
}

inline operator fun <reified T : Any> GPathResult.get(name: String): T = getProperty(name) as T
