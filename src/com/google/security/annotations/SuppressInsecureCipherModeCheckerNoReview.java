package com.google.security.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.crypto.Cipher;

/**
 * This annotation is used to disable the InsecureCipherMode Error Prone checker for legacy code
 * that didn't undergo a security review by ISE.
 *
 * <p>A {@link Cipher} object is created using one of the overloads of the
 * {@link Cipher#getInstance()} method. This method takes a specification of the transformer either
 * as a triple "Algorithm/Mode/Padding" or just "Algorithm", using the provider's default settings.
 * The InsecureCipherMode checker implemented in Error Prone flags all call sites of
 * {@link Cipher#getInstance()}, where either the insecure ECB mode or the provider's default mode
 * is used. This method annotation is used to suppress the Error Prone checker for legacy code
 * without review by ISE. The annotation is BUILD-visibility restricted and every use must be vetted
 * by the ISE team.
 *
 * <p>Example of usage:
 * <pre>
 * {@code
 * @SuppressInsecureCipherModeCheckerNoReview
 * private String decrypt(String[] input) {
 * Cipher aesCipher = Cipher.getInstance("AES");
 * aesCipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(rawKeyMaterial, "AES"));
 * // ...
 * }
 * }
 * </pre>
 *
 * @author avenet@google.com (Arnaud J. Venet)
 *
 */
@Documented
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.CONSTRUCTOR,
    ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.SOURCE)
public @interface SuppressInsecureCipherModeCheckerNoReview {}
