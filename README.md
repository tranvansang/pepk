# PEPK - Play Encrypt Private Key (PEPK) tool

Newer Java version imposes stricter security checks on JCE providers.

With Oracle Java (e.g., JDK 21), you will get this error:

```
Error: Unable to export or encrypt the private key
java.security.NoSuchAlgorithmException: Cannot find any provider supporting RSA/NONE/OAEPWithSHA1AndMGF1Padding
        at java.base/javax.crypto.Cipher.getInstance(Cipher.java:574)
        at com.google.wireless.android.vending.developer.signing.tools.extern.export.ExportEncryptedPrivateKeyTool.encryptPrivateKeyWithCkmRsaAesKeyWrapEncryption(ExportEncryptedPrivateKeyTool.java:284)
        at com.google.wireless.android.vending.developer.signing.tools.extern.export.ExportEncryptedPrivateKeyTool.run(ExportEncryptedPrivateKeyTool.java:213)
        at com.google.wireless.android.vending.developer.signing.tools.extern.export.ExportEncryptedPrivateKeyTool.main(ExportEncryptedPrivateKeyTool.java:165)
```

The source code in this repository adds this fix:

- Before:
```java
    Cipher rsaesOaepCipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding");
```

- After:
```java
    Cipher rsaesOaepCipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding", new BouncyCastleProvider());
```

Even with this fix, you will still get this error:

```
Error: Unable to export or encrypt the private key
java.lang.SecurityException: JCE cannot authenticate the provider BC
        at java.base/javax.crypto.Cipher.getInstance(Cipher.java:727)
        at com.google.wireless.android.vending.developer.signing.tools.extern.export.ExportEncryptedPrivateKeyTool.encryptPrivateKeyWithCkmRsaAesKeyWrapEncryption(ExportEncryptedPrivateKeyTool.java:284)
        at com.google.wireless.android.vending.developer.signing.tools.extern.export.ExportEncryptedPrivateKeyTool.run(ExportEncryptedPrivateKeyTool.java:213)
        at com.google.wireless.android.vending.developer.signing.tools.extern.export.ExportEncryptedPrivateKeyTool.main(ExportEncryptedPrivateKeyTool.java:165)
Caused by: java.util.jar.JarException: The JCE Provider file:/home/transang/pepk/build/libs/pepk-0.0.1.jar is not signed.
        at java.base/javax.crypto.JarVerifier.verifySingleJar(JarVerifier.java:469)
        at java.base/javax.crypto.JarVerifier.verifyJars(JarVerifier.java:320)
        at java.base/javax.crypto.JarVerifier.verify(JarVerifier.java:263)
        at java.base/javax.crypto.ProviderVerifier.verify(ProviderVerifier.java:130)
        at java.base/javax.crypto.JceSecurity.verifyProvider(JceSecurity.java:196)
        at java.base/javax.crypto.JceSecurity$2.apply(JceSecurity.java:222)
        at java.base/javax.crypto.JceSecurity$2.apply(JceSecurity.java:211)
        at java.base/java.util.concurrent.ConcurrentHashMap.computeIfAbsent(ConcurrentHashMap.java:1708)
        at java.base/javax.crypto.JceSecurity.getVerificationResult(JceSecurity.java:211)
        at java.base/javax.crypto.Cipher.getInstance(Cipher.java:723)
        ... 3 more
```

# Simple Solution

Use OpenJDK, which does not yet implement the stricter security checks.
```bash
PATH=/opt/open-jdk-21.0.1/bin:$PATH JAVA_HOME=/opt/open-jdk-21.0.1 java -jar pepk.0.1.jar <args>
```

# Related URLs

- IBM announcement on supporting the new security checks: https://www.ibm.com/support/pages/change-oracle-jce-code-signing-ca-ibm-jdk-80-sr6-fp25-71-sr4-fp75-70-sr10-fp75
- To sign your own JAR file with custom JCE provider: https://docs.oracle.com/javase/6/docs/technotes/guides/security/crypto/HowToImplAProvider.html#Step6
- Compiled jar provided by Google: https://www.gstatic.com/play-apps-publisher-rapid/signing-tool/prod/pepk.jar
- Source code provided by Google: https://www.gstatic.com/play-apps-publisher-rapid/signing-tool/prod/pepk-src.jar

# To Build:
```bash
gradle build
```

Jar file is exported in `build/libs/pepk-0.0.1.jar`.
