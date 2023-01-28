import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.6"
	id("io.spring.dependency-management") version "1.1.0"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
}

group = "com.fx.santander"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
	mavenCentral()
}

sourceSets.create("testIntegration") {
	java.srcDir("$projectDir/src/integration/kotlin")
	resources.srcDirs("$projectDir/src/integration/resources")
	compileClasspath += sourceSets.main.get().output
	runtimeClasspath += sourceSets.main.get().output
}

val testIntegrationImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.testImplementation.get())
}

configurations["testIntegrationRuntimeOnly"].extendsFrom(configurations.testRuntimeOnly.get())

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	testImplementation("org.springframework.boot:spring-boot-starter-test")

	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test")
	testIntegrationImplementation("com.squareup.okhttp:mockwebserver:1.2.1")
	testIntegrationImplementation("junit:junit:4.13.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

val integrationTest = task<Test>("testIntegration") {
	description = "Runs integration tests"
	group = "verification"

	testClassesDirs = sourceSets["testIntegration"].output.classesDirs
	classpath = sourceSets["testIntegration"].runtimeClasspath
	shouldRunAfter("test")
}

tasks.check { dependsOn(integrationTest) }