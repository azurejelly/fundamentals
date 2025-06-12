# fundamentals
Fundamentals is a work in progress Essentials alternative for modern Minecraft versions. It should not be used in production yet.

## Project setup

You'll need:
- [Git](https://www.git-scm.com/)
- An IDE - preferably [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [JDK 21](https://adoptium.net/) or higher

Clone the repository using the following command on the command line:
```shell
$ git clone https://github.com/azurejelly/fundamentals
$ cd fundamentals
```

### Running a test server

To quickly spin up a Paper test server with the plugin, simply run:
```shell
$ ./gradlew runServer
```

### Building the plugin

To build the plugin, run the following task:
```shell
$ ./gradlew build
```

You can then find a distributable JAR under `plugin/build/libs`.

