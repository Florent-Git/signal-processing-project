plugins {
    id("java")
    application
}

group = "be.atf"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

application {
    mainClass.set("isilimageprocessing.IsilImageProcessing")
}

dependencies {
    implementation("org.jfree:jfreechart:1.0.19")
    implementation("org.jfree:jcommon:1.0.24")
    implementation("org.apache.commons:commons-math3:3.6.1")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}