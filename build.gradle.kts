plugins {
    id("java")
}

allprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }

        // withJavadocJar()
        withSourcesJar()
    }

    repositories {
        mavenLocal()
        mavenCentral()
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }

        // javadoc {
        //     options.encoding = "UTF-8"
        //     options {
        //         (this as CoreJavadocOptions).addStringOption("Xdoclint:none", "-quiet")
        //     }
        // }
    }
}