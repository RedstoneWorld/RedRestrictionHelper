# RedRestrictionHelper

RedRestrictionHelper is a small Java toolkit for **Minecraft PaperMC plugin development**. It was designed to perform restriction checks via event calls or API queries to the restriction plugins installed on the server.

[![Developed by RedstoneWorld](https://redstoneworld.de/bilder/kooperation/RedstoneWorld-Logo_small.png)](https://redstoneworld.de)

# Development with RedRestrictionHelper

**Note:** Replace `%version%` with the latest version of RedRestrictionHelper:

[![Latest version of 'redrestrictionhelper' @ Cloudsmith](https://api-prd.cloudsmith.io/v1/badges/version/redstoneworld/redrestrictionhelper/maven/redrestrictionhelper/latest/a=noarch;xg=de.redstoneworld.redrestrictionhelper/?render=true&show_latest=true)](https://cloudsmith.io/~redstoneworld/repos/redrestrictionhelper/packages/detail/maven/redrestrictionhelper/latest/a=noarch;xg=de.redstoneworld.redrestrictionhelper/)

## Usage with Maven

Add the following to your Java project `pom.xml` file:

```xml
<repositories>
  <repository>
    <id>redstoneworld-redrestrictionhelper</id>
    <url>https://dl.cloudsmith.io/public/redstoneworld/redrestrictionhelper/maven/</url>
  </repository>
</repositories>

<repositories>
  <dependency>
    <groupId>de.redstoneworld.redrestrictionhelper</groupId>
    <artifactId>redrestrictionhelper</artifactId>
    <version>%version%</version>
  </dependency>
</repositories>
```

## Usage with Gradle

Add the following to your Java project `build.gradle` file:

```text
repositories {
  maven {
    url "https://dl.cloudsmith.io/public/redstoneworld/redrestrictionhelper/maven/"
  }
}

dependencies {
  implementation 'de.redstoneworld.redrestrictionhelper:redrestrictionhelper:%version%'
}
```

# Documentation

- [Java-Doc](https://redstoneworld.github.io/RedRestrictionHelper/apidocs)
- [Project Dependencies](https://redstoneworld.github.io/RedRestrictionHelper/dependencies.html)