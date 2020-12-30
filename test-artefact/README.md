# Test artefact publishing

## Account setup
* create account at [https://issues.sonatype.org](https://issues.sonatype.org/secure/Dashboard.jspa)
* register groupId (org.microproject), get __ossrhUsername__ and __ossrhPassword__
* create GPG key
  ```
  gpg --gen-key
  gpg --list-keys  
  ```
* create property file ``~/.gradle/gradle.properties`` with content
  ```
  signing.keyId=YourKeyId
  signing.password=YourPublicKeyPassword
  signing.secretKeyRingFile=PathToYourKeyRingFile
  ossrhUsername=username
  ossrhPassword=********
  ```

## Compile & Test
```
gradle clean build test
```

## Publish
```
gradle publish
gradle publishToMavenLocal
```
[published artefact](https://oss.sonatype.org/content/repositories/snapshots/one/microproject/test-artefact/1.0.0-SNAPSHOT/maven-metadata.xml)

## References
* [Publish into the Central repository](https://central.sonatype.org/pages/producers.html)
* [Gradle Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html)
* [Gradle Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)
* [Sign and publish on Maven Central](https://medium.com/@nmauti/sign-and-publish-on-maven-central-a-project-with-the-new-maven-publish-gradle-plugin-22a72a4bfd4b)