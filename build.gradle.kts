import com.smushytaco.lwjgl_gradle.Preset
import groovy.xml.XmlSlurper
import groovy.xml.slurpersupport.GPathResult
import groovy.xml.slurpersupport.NodeChild
import groovy.xml.slurpersupport.NodeChildren
import java.io.FileNotFoundException

plugins {
    alias(libs.plugins.loom)
    alias(libs.plugins.lwjgl)
    java
    `maven-publish`
}

val modVersion: Provider<String> = providers.gradleProperty("mod_version")
val modGroup: Provider<String> = providers.gradleProperty("mod_group")
val modName: Provider<String> = providers.gradleProperty("mod_name")

val javaVersion: Provider<Int> = libs.versions.java.map { it.toInt() }

base.archivesName = "cc-bta-${libs.versions.bta.get()}"
group = modGroup.get()
version = modVersion.get()

val shade: Configuration by configurations.creating

configurations {
    implementation.get().extendsFrom(shade)
}

loom {
    noIntermediateMappings()
    customMinecraftMetadata.set("https://downloads.betterthanadventure.net/bta-client/${libs.versions.btaChannel.get()}/v${libs.versions.bta.get()}/manifest.json")
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://maven.glass-launcher.net/babric") { name = "Babric" }
    maven("https://maven.fabricmc.net/") { name = "Fabric" }
    maven("https://maven.thesignalumproject.net/infrastructure") { name = "SignalumMavenInfrastructure" }
    maven("https://maven.thesignalumproject.net/releases") { name = "SignalumMavenReleases" }
    maven("https://squiddev.cc/maven") { name = "SquidDev" }
    ivy("https://github.com/Better-than-Adventure") {
        patternLayout {
            artifact("[organisation]/releases/download/v[revision]/[module].jar")
            setM2compatible(true)
        }
        metadataSources { artifact() }
    }
    ivy("https://downloads.betterthanadventure.net/bta-client/${libs.versions.btaChannel.get()}/") {
        patternLayout {
            artifact("/v[revision]/client.jar")
            setM2compatible(true)
        }
        metadataSources { artifact() }
    }
    ivy("https://downloads.betterthanadventure.net/bta-server/${libs.versions.btaChannel.get()}/") {
        patternLayout {
            artifact("/v[revision]/server.jar")
            setM2compatible(true)
        }
        metadataSources { artifact() }
    }
    ivy("https://piston-data.mojang.com") {
        patternLayout {
            artifact("v1/[organisation]/[revision]/[module].jar")
            setM2compatible(true)
        }
        metadataSources { artifact() }
    }
}

lwjgl {
    version = libs.versions.lwjgl
    implementation(Preset.MINIMAL_OPENGL)
}

dependencies {
    minecraft("::${libs.versions.bta.get()}")
    mappings(loom.layered {})

    modRuntimeOnly(libs.clientJar)
    modImplementation(libs.loader)

    modImplementation(libs.halplibe)

    implementation(libs.slf4jApi)
    implementation(libs.log4j.slf4j2.impl)

    shade(libs.guava)
    implementation(libs.gson)
    implementation(libs.log4j.core)
    implementation(libs.log4j.api)
    implementation(libs.log4j.api12)

    implementation(libs.commonsLang3)
    include(libs.commonsLang3)

    modImplementation(libs.legacyLwjgl)

    shade(libs.cobalt)

    shade(libs.netty.buffer)
    shade(libs.netty.common)
    shade(libs.netty.transport)
    shade(libs.netty.handler)
    shade(libs.netty.codec)
    shade(libs.netty.codec.http)

    shade(libs.nightConfig.core)
    shade(libs.nightConfig.toml)

    implementation(libs.commonsCodec)
    include(libs.commonsCodec)
}

java {
    toolchain {
        languageVersion = javaVersion.map { JavaLanguageVersion.of(it) }
        vendor = JvmVendorSpec.ADOPTIUM
    }
    sourceCompatibility = JavaVersion.toVersion(javaVersion.get())
    targetCompatibility = JavaVersion.toVersion(javaVersion.get())
    withSourcesJar()
}

val licenseFile = run {
    val rootLicense = layout.projectDirectory.file("LICENSE")
    val parentLicense = layout.projectDirectory.file("../LICENSE")
    when {
        rootLicense.asFile.exists() -> {
            logger.lifecycle("Using LICENSE from project root: {}", rootLicense.asFile)
            rootLicense
        }
        parentLicense.asFile.exists() -> {
            logger.lifecycle("Using LICENSE from parent directory: {}", parentLicense.asFile)
            parentLicense
        }
        else -> {
            logger.warn("No LICENSE file found in project or parent directory.")
            null
        }
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        sourceCompatibility = javaVersion.get().toString()
        targetCompatibility = javaVersion.get().toString()
        if (javaVersion.get() > 8) options.release = javaVersion
    }
    named<UpdateDaemonJvm>("updateDaemonJvm") {
        languageVersion = libs.versions.gradleJava.map { JavaLanguageVersion.of(it.toInt()) }
        vendor = JvmVendorSpec.ADOPTIUM
    }
    withType<JavaExec>().configureEach { defaultCharacterEncoding = "UTF-8" }
    withType<Javadoc>().configureEach { options.encoding = "UTF-8" }
    withType<Test>().configureEach { defaultCharacterEncoding = "UTF-8" }
    withType<Jar>().configureEach {
        licenseFile?.let {
            from(it) {
                rename { original -> "${original}_${archiveBaseName.get()}" }
            }
        }
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(shade.map { if (it.isDirectory) it else zipTree(it) })
    }
    processResources {
        val resourceMap = mapOf(
            "version" to modVersion.get()
        )
        inputs.properties(resourceMap)
        filesMatching("fabric.mod.json") { expand(resourceMap) }
    }
}

configurations.configureEach {
    exclude(group = "org.lwjgl.lwjgl")
}

publishing {
    val publishVersion = "${modVersion.get()}-${libs.versions.bta.get()}"

    if(checkVersion("gungun974", modName.get(), publishVersion)) {
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
                artifactId = modName.get()
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
