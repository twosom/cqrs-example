plugins {
    id("java")
    id("org.springframework.boot") version ("2.7.5")
    id("io.spring.dependency-management") version ("1.0.15.RELEASE")
}

allprojects {
    group = "com.icloud"
    version = "0.0.1-SNAPSHOT"
}

subprojects {
    apply {
        plugin("java")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-amqp")
        implementation("org.springframework.boot:spring-boot-starter-web")
        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
        testImplementation("org.springframework.amqp:spring-rabbit-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

project(":command-service") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-jpa")
        runtimeOnly("com.mysql:mysql-connector-j")
    }
}

project(":query-service") {
    dependencies {
        implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    }
}




