import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.0.0"
    kotlin("plugin.serialization") version "2.0.0"
    id("maven-publish")
    id("java-library")
    application
}

group = "com.aiengine"
version = "1.0.0"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    // Kotlin Coroutines for async processing
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    
    // Kotlin Serialization for data handling
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    
    // Logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
    implementation("ch.qos.logback:logback-classic:1.4.14")
    
    // Text processing and NLP utilities
    implementation("org.apache.commons:commons-text:1.11.0")
    implementation("org.apache.commons:commons-lang3:3.14.0")
    
    // Unicode and character manipulation
    implementation("com.ibm.icu:icu4j:74.2")
    
    // JSON processing
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    
    // HTTP client for API calls
    implementation("io.ktor:ktor-client-core:2.3.8")
    implementation("io.ktor:ktor-client-cio:2.3.8")
    implementation("io.ktor:ktor-client-content-negotiation:2.3.8")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.8")
    
    // Caching
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")
    
    // Testing dependencies
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("io.mockk:mockk:1.13.9")
    testImplementation("org.assertj:assertj-core:3.25.3")
    
    // Performance testing
    testImplementation("org.openjdk.jmh:jmh-core:1.37")
    testImplementation("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-Xjsr305=strict",
            "-Xcontext-receivers",
            "-Xopt-in=kotlin.RequiresOptIn",
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
    withSourcesJar()
    withJavadocJar()
}

application {
    mainClass.set("com.aiengine.filterblocker.FilterBlockerKt")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            pom {
                name.set("AI Filter Blocker")
                description.set("Advanced filter bypass module for AI engines")
                url.set("https://github.com/aiengine/filter-blocker")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("aiengine-team")
                        name.set("AI Engine Development Team")
                        email.set("dev@aiengine.com")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/aiengine/filter-blocker.git")
                    developerConnection.set("scm:git:ssh://github.com:aiengine/filter-blocker.git")
                    url.set("https://github.com/aiengine/filter-blocker")
                }
            }
        }
    }
}

// Custom tasks for filter blocker operations
tasks.register("runFilterTest") {
    group = "filter-blocker"
    description = "Run filter bypass tests"
    dependsOn("test")
}

tasks.register("buildFilterModule") {
    group = "filter-blocker"
    description = "Build complete filter blocker module"
    dependsOn("build", "publishToMavenLocal")
}

tasks.register("generateFilterDocs") {
    group = "filter-blocker"
    description = "Generate filter blocker documentation"
    dependsOn("javadoc")
}

// Performance optimization
tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf(
        "-Xlint:unchecked",
        "-Xlint:deprecation"
    ))
}

// JAR configuration
tasks.jar {
    manifest {
        attributes(
            "Implementation-Title" to "AI Filter Blocker",
            "Implementation-Version" to project.version,
            "Main-Class" to "com.aiengine.filterblocker.FilterBlockerKt"
        )
    }
    
    // Include dependencies in fat JAR
    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

