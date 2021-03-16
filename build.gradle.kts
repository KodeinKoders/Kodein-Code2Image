plugins {
    kotlin("js") version "1.4.31"
}

group = "org.kodein.code2img"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
    jcenter()
}

dependencies {
    val reactVersion = "17.0.1"
    val styledVersion = "5.2.1"
    val kotlinWrapperVersion = "pre.149-kotlin-1.4.31"

    implementation("org.jetbrains:kotlin-react:$reactVersion-$kotlinWrapperVersion")
    implementation("org.jetbrains:kotlin-react-dom:$reactVersion-$kotlinWrapperVersion")
    implementation("org.jetbrains:kotlin-styled:$styledVersion-$kotlinWrapperVersion")
    implementation(npm("highlight.js", "10.6.0"))
    implementation(npm("html2canvas", "1.0.0-rc.7"))
}

kotlin {
    js(IR) {
        browser {
            binaries.executable()
        }
    }
}