# Paper Template
A Minecraft server plugin template using [Google Guice](https://github.com/google/guice). Looking for a template with only one module? Check [this branch](https://github.com/azurejelly/paper-template/tree/single-module) out.

## Requirements
To use this template, you will need:
- Some experience with [Google Guice](https://github.com/google/guice);
- [Git](https://www.git-scm.com/) for Version Control;
- An IDE - preferably [IntelliJ IDEA](https://www.jetbrains.com/idea/);
- [JDK 21](https://adoptium.net/) or higher.
  - You can lower this requirement to Java 17, 11 or even 8 by modifying the source files accordingly.

## Getting started
Clone the repository using the following command on the command line:
```shell
$ git clone https://github.com/azurejelly/paper-template
$ cd paper-template
```

Alternatively, download the source files as a ZIP from [here](https://github.com/azurejelly/paper-template/archive/refs/heads/multi-module.zip). You will need to extract them to a folder using something like 7-Zip.

## Setup
Import the project to IntelliJ IDEA, or open it directly from the command line:
```shell
$ idea .
```

To change the plugin's name, description, group ID, version and some other properties, open up the `gradle.properties` file. By default, the file looks like this:
```properties
group=cool.azu.sample
version=1.0.0

# Sets the Minecraft version to use with the 'runServer' task.
serverVersion=1.21.3

# Sets the plugin name, description and website.
# The description and website are both optional and can be commented out.
pluginName=SamplePlugin
pluginDescription="It's as shrimple as that"
pluginWebsite=https://azu.cool
```

When changing the group ID, be sure to change the packages inside the source code as well. You can do this by right-clicking the packages on IntelliJ IDEA, then choosing `Refactor > Rename`. If the server yells at you when loading the plugin because it can't find the main class, try specifying it manually in the [`plugin/build.gradle.kts`](plugin/build.gradle.kts) file:
```kt
bukkit {
    /* ... */
    main = "${project.group}.${pluginName}"
    /* ... */
}
```

## Running an integrated server
You can run an integrated Paper server with the plugin by using the `gradlew runServer` task:
```shell
$ ./gradlew runServer
```
This also supports downloading plugin dependencies automatically - see the [run-task wiki](https://github.com/jpenilla/run-task/wiki/) for more information.

## Building for distribution
To build a version ready for distribution, simply run:
```shell
$ ./gradlew build
```
You should now have a file that you can distribute at the `plugin/build/libs/` directory (the one without the `-original` suffix!).