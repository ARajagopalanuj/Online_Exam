plugins {
    id("java")

}

group = "org.example"
version = "1.0-SNAPSHOT"



repositories {
    mavenCentral()
}

dependencies {
    // https://mvnrepository.com/artifact/com.thetransactioncompany/cors-filter
    implementation("com.thetransactioncompany:cors-filter:3.1")
    implementation("com.sun.activation:jakarta.activation:2.0.1")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    // https://mvnrepository.com/artifact/org.postgresql/postgresql
    implementation("org.postgresql:postgresql:42.7.7")
    // https://mvnrepository.com/artifact/jakarta.servlet/jakarta.servlet-api
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    // https://mvnrepository.com/artifact/org.mindrot/jbcrypt
    implementation("org.mindrot:jbcrypt:0.4")

    // Latest Jakarta Mail for Tomcat 10/11 compatibility
    implementation("com.sun.mail:jakarta.mail:2.0.1")
    // https://mvnrepository.com/artifact/org.json/json
    implementation("org.json:json:20250517")
}

tasks.test {
    useJUnitPlatform()
}


