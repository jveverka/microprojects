# Test artefact publishing

## Account setup
* Create account at [https://issues.sonatype.org](https://issues.sonatype.org/secure/Dashboard.jspa)
* Register groupId (org.microproject), get __ossrhUsername__ and __ossrhPassword__
* Create new GPG key
  ```
  gpg --gen-key
  gpg --export-secret-keys -o ~/.gnupg/secring.gpg
  gpg2 --list-keys  
  gpg2 --list-keys --keyid-format short
  ```
* Upload created GPG public key to public key servers, so others can verify.  
  ```
  gpg2 --keyserver http://keyserver.ubuntu.com:11371 --send-keys <YourKeyId>
  gpg2 --keyserver http://keys.gnupg.net:11371 --send-keys <YourKeyId>
  gpg2 --keyserver http://pool.sks-keyservers.net:11371 --send-keys <YourKeyId>
  ```
  YourKeyId = last 8 characters from public key ID
* Create property file ``~/.gradle/gradle.properties`` with content
  ```
  signing.keyId=<YourKeyId>
  signing.password=<YourPublicKeyPassword>
  signing.secretKeyRingFile=<PathToYourKeyRingFile>
  ossrhUsername=username
  ossrhPassword=********
  ```

## Compile & Test
```
gradle clean build test
```

## SNAPSHOT artefacts
Artefacts published to [oss.sonatype.org SNAPSHOT](https://oss.sonatype.org/content/repositories/snapshots) 
repository must have __-SNAPSHOT__ version suffix.

## Stage and Release
Artefacts published to [oss.sonatype.org stage](https://oss.sonatype.org/service/local/staging/deploy/maven2)
repository must NOT have __-SNAPSHOT__ version suffix.

## Publish
```
gradle publish
gradle publishToMavenLocal
```
[published artefact](https://oss.sonatype.org/content/repositories/snapshots/one/microproject/test/test-artefact/1.0.4-SNAPSHOT)

## Consume Artefact in maven
```
<project>
    ...
    <dependencies>
        <dependency>
            <groupId>one.microproject.test</groupId>
            <artifactId>test-artefact</artifactId>
            <version>1.0.4-SNAPSHOT</version>
        </dependency>
    </dependencies>
    <repositories>
        <repository>
            <id>sonatype-snapshot</id>
            <url>https://oss.sonatype.org/content/repositories/snapshots</url>
        </repository>
    </repositories>
</project>
```

## Consume Artefact in gradle
```
dependencies {
  implementation 'one.microproject.test:test-artefact:1.0.4-SNAPSHOT' 
}
repositories {
    maven {
        url "https://oss.sonatype.org/content/repositories/snapshots"
    }
}
```

## References
* [Publish into the Central repository](https://central.sonatype.org/pages/producers.html)
* [Gradle Signing Plugin](https://docs.gradle.org/current/userguide/signing_plugin.html)
* [Gradle Maven Publish Plugin](https://docs.gradle.org/current/userguide/publishing_maven.html)
* [Working with PGP Signatures](https://central.sonatype.org/pages/working-with-pgp-signatures.html)
* [Sign and publish on Maven Central](https://medium.com/@nmauti/sign-and-publish-on-maven-central-a-project-with-the-new-maven-publish-gradle-plugin-22a72a4bfd4b)