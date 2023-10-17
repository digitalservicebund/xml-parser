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