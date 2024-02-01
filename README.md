# XML parser

[![](https://jitpack.io/v/digitalservicebund/xml-parser.svg)](https://jitpack.io/#digitalservicebund/xml-parser)

The library has various handy helper functions that can be used in your Kotlin and Java projects to parse an XML file and extract information using xpaths.

### Usage

To use the library include the following code into your `build.gradle` file

```kotlin
repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
}

// ....

implementation("com.github.digitalservicebund:xml-parser:VERSION")

```

## Contributing

🇬🇧 Everyone is welcome to contribute the development of the _xml-parser_. You can contribute by opening pull request,
providing documentation or answering questions or giving feedback. Please always follow the guidelines and our
[Code of Conduct](CODE_OF_CONDUCT.md).

🇩🇪 Jede:r ist herzlich eingeladen, die Entwicklung des _xml-parser_ mitzugestalten. Du kannst einen Beitrag leisten,
indem du Pull-Requests eröffnest, die Dokumentation erweiterst, Fragen beantwortest oder Feedback gibst.
Bitte befolge immer die Richtlinien und unseren [Verhaltenskodex](CODE_OF_CONDUCT_DE.md).

## Contributing code

🇬🇧 Open a pull request with your changes and it will be reviewed by someone from the team. When you submit a pull request,
you declare that you have the right to license your contribution to the DigitalService and the community.
By submitting the patch, you agree that your contributions are licensed under the MIT license.

Please make sure that your changes have been tested before submitting a pull request.

🇩🇪 Nach dem Erstellen eines Pull Requests wird dieser von einer Person aus dem Team überprüft. Wenn du einen Pull-Request
einreichst, erklärst du dich damit einverstanden, deinen Beitrag an den DigitalService und die Community zu
lizenzieren. Durch das Einreichen des Patches erklärst du dich damit einverstanden, dass deine Beiträge unter der
MIT-Lizenz lizenziert sind.

Bitte stelle sicher, dass deine Änderungen getestet wurden, bevor du einen Pull-Request sendest.

## Releasing Versions

### Creating Tags for Releases

When preparing a release, ensure the creation of an appropriate tag in accordance with the existing schema, which is
`vMAJOR.MINOR` (e.g., `v1.2`). The version number should match the one defined in our [build.gradle.kts file](./build.gradle.kts).

#### Example:

```bash
git tag -a v1.2 -m "Release version 1.2"
git push origin v1.2
```

### Publishing Releases

After creating the tag, proceed to create a release version from this tag.
This version will automatically be published by [jitpack.io](https://www.jitpack.io/#digitalservicebund/xml-parser/).

Make sure that the release notes and any additional information are appropriately documented to keep users informed
about the changes in this version.

### JitPack Integration

Our releases are hosted and automatically published by [jitpack.io](https://www.jitpack.io/#digitalservicebund/xml-parser/).
Users can easily include the latest version of our library in their projects by referring to the jitpack.io repository
as mentioned in the beginning of this README.

**Note**: In addition to releases, each tag is also available as a verion in JitPack.
