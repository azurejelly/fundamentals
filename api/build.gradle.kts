plugins {
    `java-library`
}

repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    api(libs.jackson.databind)

    compileOnly(libs.paper) {
        // Excluded so that we do not import the wrong @Inject class.
        // Guice for some reason does not follow javax.inject annotations,
        // and any fields annotated with it will end up as null.
        exclude(group = "javax.inject", module = "javax.inject")
    }

    testImplementation(libs.bundles.junit)
}

tasks.test {
    useJUnitPlatform()
}