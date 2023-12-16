/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.wireless.android.vending.developer.signing.tools.extern.export;

import static com.google.wireless.android.vending.developer.signing.tools.extern.export.Utils.checkNotNull;
import static java.nio.charset.StandardCharsets.US_ASCII;
import static java.nio.charset.StandardCharsets.UTF_8;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.security.keymaster.lite.KeymaestroHybridEncrypter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateEncodingException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.bouncycastle.crypto.engines.AESWrapPadEngine;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;

/**
 * Tool for extracting a private key from a Java Keystore and then encrypting it (with hybrid public
 * key encryption) for secure transfer to Google.
 */
public class ExportEncryptedPrivateKeyTool {

  private static final String FLAG_KEYSTORE = "keystore";
  private static final String FLAG_ALIAS = "alias";
  private static final String FLAG_RSA_AES_ENCRYPTION = "rsa-aes-encryption";
  private static final String FLAG_ENCRYPTION_KEY_PATH = "encryption-key-path";
  private static final String FLAG_ENCRYPTION_KEY = "encryptionkey";
  private static final String FLAG_OUTPUT = "output";
  private static final String FLAG_SIGNING_KEYSTORE = "signing-keystore";
  private static final String FLAG_SIGNING_KEY_ALIAS = "signing-key-alias";
  private static final String FLAG_KEYSTORE_PWD = "keystore-pass";
  private static final String FLAG_KEY_PWD = "key-pass";
  private static final String FLAG_INCLUDE_CERT = "include-cert";
  private static final ImmutableList<String> SUPPORTED_SIGNING_ALGORITHMS =
      ImmutableList.of("RSA", "DSA");

  private static final String HELP_PAGE = "help.txt";
  private static final String LICENSE_PAGE = "license.txt";

  private final KeystoreHelper keystoreHelper;

  public static void main(String[] args) {
    Security.addProvider(new BouncyCastleProvider());
    if (args.length == 0 || args[0].equals("--help")) {
      printUsage();
      return;
    }
    if (args[0].equals("--license")) {
      printLicense();
      return;
    }

    boolean useRsaAesEncryption = false;

    // Parse flags
    String keystoreFile = null;
    String alias = null;
    String rsaAesEncryption = null;
    String encryptionPublicKey = null;
    String outputFile = null;
    String signingKeyAlias = null;
    String signingKeystoreFile = null;
    String keystorePassword = null;
    String keyPassword = null;
    String includeCert = null;
    try {
      Map<String, String> parsedFlags = Utils.processArgs(args);
      keystoreFile = getFlagValue(parsedFlags, FLAG_KEYSTORE);
      alias = getFlagValue(parsedFlags, FLAG_ALIAS);
      outputFile = getFlagValue(parsedFlags, FLAG_OUTPUT);
      if (parsedFlags.containsKey(FLAG_RSA_AES_ENCRYPTION)) {
        rsaAesEncryption = getFlagValue(parsedFlags, FLAG_RSA_AES_ENCRYPTION);
      }
      useRsaAesEncryption = Boolean.parseBoolean(rsaAesEncryption);
      if (useRsaAesEncryption) {
        encryptionPublicKey = getFlagValue(parsedFlags, FLAG_ENCRYPTION_KEY_PATH);
      } else {
        encryptionPublicKey = getFlagValue(parsedFlags, FLAG_ENCRYPTION_KEY);
      }
      if (parsedFlags.containsKey(FLAG_SIGNING_KEY_ALIAS)) {
        signingKeyAlias = parsedFlags.remove(FLAG_SIGNING_KEY_ALIAS);
        // If signing key alias is provided then signing key keystore is required.
        signingKeystoreFile = getFlagValue(parsedFlags, FLAG_SIGNING_KEYSTORE);
      } else if (parsedFlags.containsKey(FLAG_INCLUDE_CERT)) {
        includeCert = getFlagValue(parsedFlags, FLAG_INCLUDE_CERT);
      }
      if (parsedFlags.containsKey(FLAG_KEYSTORE_PWD)) {
        keystorePassword = getFlagValue(parsedFlags, FLAG_KEYSTORE_PWD);
      }
      if (parsedFlags.containsKey(FLAG_KEY_PWD)) {
        keyPassword = getFlagValue(parsedFlags, FLAG_KEY_PWD);
      }
      if (keyPassword == null) {
        keyPassword = keystorePassword;
      }
      if (!parsedFlags.isEmpty()) {
        throw new IllegalArgumentException("Unrecognized flags: " + parsedFlags);
      }
    } catch (RuntimeException e) {
      System.err.println("Error: Unable to parse the input: " + Arrays.toString(args));
      e.printStackTrace();
      printUsage();
      System.exit(1);
    }

    // Run tool
    try {
      ExportEncryptedPrivateKeyTool tool = new ExportEncryptedPrivateKeyTool(new KeystoreHelper());
      KeystoreKey keyToExport =
          keystorePassword != null && keyPassword != null
              ? new KeystoreKey(
                  Paths.get(keystoreFile),
                  alias,
                  keystorePassword.toCharArray(),
                  keyPassword.toCharArray())
              : new KeystoreKey(Paths.get(keystoreFile), alias);
      Optional<KeystoreKey> keyToSignWith =
          signingKeystoreFile != null && signingKeyAlias != null
              ? Optional.of(new KeystoreKey(Paths.get(signingKeystoreFile), signingKeyAlias))
              : Optional.empty();
      boolean includeCertificate = Boolean.parseBoolean(includeCert);
      tool.run(
          useRsaAesEncryption,
          encryptionPublicKey,
          outputFile,
          keyToExport,
          keyToSignWith,
          includeCertificate);
    } catch (Exception e) {
      System.err.println("Error: Unable to export or encrypt the private key");
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static String getFlagValue(Map<String, String> parsedFlags, String flagName) {
    return checkNotNull(parsedFlags.remove(flagName), "--" + flagName + " must be specified");
  }

  ExportEncryptedPrivateKeyTool(KeystoreHelper keystoreHelper) {
    this.keystoreHelper = keystoreHelper;
  }

  public ExportEncryptedPrivateKeyTool() {
    this.keystoreHelper = new KeystoreHelper();
  }

  /**
   * Extracts a private key from a Java Keystore and then encrypts it (with hybrid public key
   * encryption). If signing key is specified the encrypted private key is additionally signed with
   * that key and the output is a zip file containing the signature and the encrypted private key.
   *
   * <p>If the signing key uses algorithm different then RSA or DSA an UnsupportedAlgorithmException
   * is thrown.
   */
  public void run(
      boolean useRsaAesEncryption,
      String encryptionPublicKey,
      String outputFile,
      KeystoreKey keyToExport,
      Optional<KeystoreKey> keyToSignWith,
      boolean includeCertificate)
      throws Exception {
    KeyStore keyStoreForKeyToExport = keystoreHelper.getKeystore(keyToExport);
    PrivateKey privateKeyToExport =
        keystoreHelper.getPrivateKey(keyStoreForKeyToExport, keyToExport);
    byte[] encryptedPrivateKey;
    if (useRsaAesEncryption) {
      encryptedPrivateKey =
          encryptPrivateKeyWithCkmRsaAesKeyWrapEncryption(
              Files.readAllBytes(Paths.get(encryptionPublicKey)), privateKeyToExport);
    } else {
      encryptedPrivateKey =
          encryptPrivateKeyWithEcP256AesGcmHybridEncryption(
              fromHex(encryptionPublicKey), privateKeyToExport);
    }
    if (keyToSignWith.isPresent() || includeCertificate) {
      Certificate certificate = keystoreHelper.getCertificate(keyStoreForKeyToExport, keyToExport);
      Optional<byte[]> signature =
          keyToSignWith.isPresent()
              ? Optional.of(sign(encryptedPrivateKey, keyToSignWith.get()))
              : Optional.empty();
      writeToZipFile(outputFile, signature, encryptedPrivateKey, certificateToPem(certificate));
    } else {
      Files.write(Paths.get(outputFile), encryptedPrivateKey);
    }
  }

  public byte[] sign(byte[] payload, KeystoreKey signingKey) throws Exception {
    KeyStore keyStoreOfSigningKey = keystoreHelper.getKeystore(signingKey);
    PrivateKey pk = keystoreHelper.getPrivateKey(keyStoreOfSigningKey, signingKey);
    if (!SUPPORTED_SIGNING_ALGORITHMS.contains(pk.getAlgorithm())) {
      throw new UnsupportedAlgorithmException(
          String.format(
              "The signing key uses an unsupported algorithm. The tool only supports %s .",
              SUPPORTED_SIGNING_ALGORITHMS));
    }
    Signature sig = Signature.getInstance("SHA512with" + pk.getAlgorithm());
    sig.initSign(pk);
    sig.update(payload);
    return sig.sign();
  }

  // Visible for testing
  static byte[] privateKeyToPem(PrivateKey privateKey) {
    String pemString =
        "-----BEGIN PRIVATE KEY-----\n"
            + formatBase64String(Base64.getEncoder().encodeToString(privateKey.getEncoded()))
            + "\n-----END PRIVATE KEY-----\n";
    return pemString.getBytes(US_ASCII);
  }

  private static byte[] certificateToPem(Certificate certificate)
      throws CertificateEncodingException {
    return ("-----BEGIN CERTIFICATE-----\n"
            + formatBase64String(Base64.getEncoder().encodeToString(certificate.getEncoded()))
            + "\n-----END CERTIFICATE-----\n")
        .getBytes(US_ASCII);
  }

  // Visible for testing
  static String formatBase64String(String string) {
    return String.join("\n", Splitter.fixedLength(64).splitToList(string));
  }

  private byte[] encryptPrivateKeyWithEcP256AesGcmHybridEncryption(
      byte[] encryptionPublicKey, PrivateKey privateKey) throws GeneralSecurityException {
    byte[] privateKeyPem = privateKeyToPem(privateKey);
    return new KeymaestroHybridEncrypter(encryptionPublicKey).encrypt(privateKeyPem);
  }

  private byte[] encryptPrivateKeyWithCkmRsaAesKeyWrapEncryption(
      byte[] encryptionPublicKey, PrivateKey privateKey) throws Exception {
    // Generate random AES 256 key.
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(256);
    SecretKey randomAesKey = keyGenerator.generateKey();

    // Encrypt AES key with the encryption public key using RSAES-OAEP.
    PublicKey publicKey = readPublicKey(encryptionPublicKey);
    Cipher rsaesOaepCipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA1AndMGF1Padding");
    rsaesOaepCipher.init(Cipher.ENCRYPT_MODE, publicKey);
    byte[] encryptedAesKey = rsaesOaepCipher.doFinal(randomAesKey.getEncoded());

    // Encrypt private key with AES key using AES Key Wrap with Padding.
    AESWrapPadEngine aesWrapPadEngine = new AESWrapPadEngine();
    aesWrapPadEngine.init(/* forWrapping= */ true, new KeyParameter(randomAesKey.getEncoded()));
    byte[] privateKeyBytes = privateKey.getEncoded();
    byte[] encryptedPrivateKey = aesWrapPadEngine.wrap(privateKeyBytes, 0, privateKeyBytes.length);

    // Combine both encrypted AES key and encrypted private key.
    return ByteBuffer.allocate(encryptedAesKey.length + encryptedPrivateKey.length)
        .put(encryptedAesKey)
        .put(encryptedPrivateKey)
        .array();
  }

  private PublicKey readPublicKey(byte[] pemPublicKey) throws Exception {
    KeyFactory factory = KeyFactory.getInstance("RSA");
    try (InputStreamReader keyReader =
            new InputStreamReader(new ByteArrayInputStream(pemPublicKey), UTF_8);
        PemReader pemReader = new PemReader(keyReader)) {
      PemObject pemObject = pemReader.readPemObject();
      byte[] content = pemObject.getContent();
      X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(content);
      return factory.generatePublic(pubKeySpec);
    }
  }

  private void writeToZipFile(
      String outputFile,
      Optional<byte[]> signature,
      byte[] encryptedPrivateKey,
      byte[] pemEncodedCertificate)
      throws Exception {
    Path tempFile = Files.createFile(Paths.get(outputFile));
    try (ZipOutputStream zipOutputStream =
        new ZipOutputStream(new FileOutputStream(tempFile.toString()))) {
      if (signature.isPresent()) {
        zipOutputStream.putNextEntry(new ZipEntry("encryptedPrivateKeySignature"));
        zipOutputStream.write(signature.get());
      }
      zipOutputStream.closeEntry();
      zipOutputStream.putNextEntry(new ZipEntry("encryptedPrivateKey"));
      zipOutputStream.write(encryptedPrivateKey);
      zipOutputStream.closeEntry();
      zipOutputStream.putNextEntry(new ZipEntry("certificate.pem"));
      zipOutputStream.write(pemEncodedCertificate);
      zipOutputStream.closeEntry();
    }
  }

  private static byte[] fromHex(String s) {
    int len = s.length();
    if (len % 2 != 0) {
      throw new IllegalArgumentException(
          "Hex encoded byte array must have even length but instead has length: "
              + len
              + ". Hex encoded string: "
              + s);
    }
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
      data[i / 2] =
          (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
    }
    return data;
  }

  private static void printUsage() {
    printFile(HELP_PAGE);
  }

  private static void printLicense() {
    printFile(LICENSE_PAGE);
  }

  private static void printFile(String fileName) {
    try (BufferedReader in =
        new BufferedReader(
            new InputStreamReader(
                ExportEncryptedPrivateKeyTool.class.getResourceAsStream(fileName),
                StandardCharsets.UTF_8))) {
      String line;
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }
    } catch (IOException e) {
      throw new IllegalStateException("Failed to read " + fileName + " resource");
    }
  }
}
