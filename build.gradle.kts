plugins {
    id("java")
}

group = "com.komarzz.KNN"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("tech.tablesaw:tablesaw-core:0.43.1")
    implementation("org.jfree:jfreechart:1.5.4")
}
