import pl.allegro.tech.build.axion.release.domain.PredefinedVersionCreator.VERSION_WITH_BRANCH
import java.lang.System.getenv

plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.axion.release)
    alias(libs.plugins.nexus.publish)
    alias(libs.plugins.test.logger)
}

scmVersion {
    versionCreator = VERSION_WITH_BRANCH.versionCreator
}

allprojects {
    project.group = "pl.allegro.tech.jsoncache"
    project.version = rootProject.scmVersion.version
}

subprojects {
    val libs = rootProject.libs

    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = libs.plugins.test.logger)

    java {
        withSourcesJar()
        withJavadocJar()
        toolchain {
            languageVersion = libs.versions.java.map(JavaLanguageVersion::of)
        }
    }

    dependencies {
        testImplementation(platform(libs.junit.dependencies))
        testImplementation(libs.bundles.junit)

        testRuntimeOnly(libs.junit.platform.launcher)
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
        withType<Test> {
            useJUnitPlatform()
        }
        withType<Javadoc> {
            (options as StandardJavadocDocletOptions).tags = listOf("apiNote")
        }
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
                pom {
                    name = project.name
                    description = "JSON cache module: ${project.name}"
                    url = "https://github.com/allegro/json-cache"
                    inceptionYear = "2023"
                    licenses {
                        license {
                            name = "The Apache License, Version 2.0"
                            url = "http://www.apache.org/licenses/LICENSE-2.0.txt"
                        }
                    }
                    developers {
                        developer {
                            id = "jglaszka"
                            name = "Julia Glaszka"
                        }
                        developer {
                            id = "Anarak404"
                            name = "Anna Arak"
                        }
                        developer {
                            id = "pawelkowalski92"
                            name = "Paweł Kowalski"
                        }
                        developer {
                            id = "pawel-guzek"
                            name = "Paweł Guzek"
                        }
                        developer {
                            id = "tomasz-sieminski"
                            name = "Tomasz Siemiński"
                        }
                    }
                    scm {
                        url = "https://github.com/allegro/json-cache"
                        connection = "git@github.com:allegro/json-cache.git"
                        developerConnection = "git@github.com:allegro/json-cache.git"
                    }
                }
            }
        }
    }

    signing {
        val gpgKeyId = getenv("GPG_KEY_ID") ?: return@signing
        val gpgPrivateKey = getenv("GPG_PRIVATE_KEY") ?: return@signing
        val gpgPrivateKeyPassword = getenv("GPG_PRIVATE_KEY_PASSWORD") ?: return@signing

        useInMemoryPgpKeys(gpgKeyId, gpgPrivateKey, gpgPrivateKeyPassword)
        sign(publishing.publications["sonatype"])
    }
}

nexusPublishing {
    repositories {
        sonatype {
            nexusUrl = uri("https://ossrh-staging-api.central.sonatype.com/service/local/")
            snapshotRepositoryUrl = uri("https://central.sonatype.com/repository/maven-snapshots/")
            username = getenv("SONATYPE_USERNAME")
            password = getenv("SONATYPE_PASSWORD")
        }
    }
}

infix fun <T : PluginDependency> PluginAware.apply(plugin: Provider<T>) {
    apply(plugin = plugin.get().pluginId)
}
