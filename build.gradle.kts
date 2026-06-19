plugins {
    java
    application
    id("com.diffplug.spotless") version "6.25.0"
    id("info.solidsoft.pitest") version "1.15.0"
}

group = "com.kotmin.soldevelo"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

application {
    mainClass.set("com.kotmin.soldevelo.alertrules.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

spotless {
    java {
        palantirJavaFormat("2.28.0")
        removeUnusedImports()
    }
}

pitest {
    junit5PluginVersion.set("1.2.1")
    targetClasses.set(setOf("com.kotmin.soldevelo.alertrules.*"))
    outputFormats.set(setOf("HTML"))
    mutationThreshold.set(0)
}
