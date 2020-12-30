# Test artefact publishing

## Account setup
* create account at [https://issues.sonatype.org](https://issues.sonatype.org/secure/Dashboard.jspa)
* create property file ``~/.gradle/gradle.properties`` with content
  ```
  signing.keyId=YourKeyId
  signing.password=YourPublicKeyPassword
  signing.secretKeyRingFile=PathToYourKeyRingFile
  ossrhUsername=username
  ossrhPassword=********
  ```

## References
* [Sign and publish on Maven Central](https://medium.com/@nmauti/sign-and-publish-on-maven-central-a-project-with-the-new-maven-publish-gradle-plugin-22a72a4bfd4b)