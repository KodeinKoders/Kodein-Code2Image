plugins {
    kotlin("js") version "1.4.31"
    id("org.ajoberstar.git-publish") version "3.0.0"
    id("org.ajoberstar.grgit") version "4.1.0"
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

val auth = (project.findProperty("com.github.http.auth") as? String)?.split(":")
if (auth != null) {
    System.setProperty("org.ajoberstar.grgit.auth.username", auth[0])
    System.setProperty("org.ajoberstar.grgit.auth.password", auth[1])
}

gitPublish {
    repoUri.set("https://github.com/KodeinKoders/Kodein-Code2Image.git")
    branch.set("gh-pages")
    contents.apply {
        from("$projectDir/build/distributions")
    }
    val head = grgit.head()
    commitMessage.set("${head.abbreviatedId}: ${head.fullMessage}")
}

task("deployWebsiteToGithubPages") {
    group = "publishing"
    dependsOn("gitPublishPush")
}

tasks["gitPublishCopy"].dependsOn("browserDistribution")

tasks["gitPublishCommit"].doFirst {
    if (!grgit.status().isClean) {
        error("Refusing to commit new pages on a non-clean repo. Please commit first.\n${grgit.status()}")
    }
}
