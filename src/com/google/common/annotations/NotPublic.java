// Copyright 2009 Google Inc. All Rights Reserved.

package com.google.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Signifies that a program element is not released publicly to open-source libraries, although it
 * lives inside a class or package which is.
 *
 * <p>To exclude a program element from public release and from the internal Android version of
 * Guava, use {@link GoogleInternal @GoogleInternal} instead.
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({
  ElementType.ANNOTATION_TYPE,
  ElementType.CONSTRUCTOR,
  ElementType.FIELD,
  ElementType.METHOD,
  ElementType.TYPE
})
@NotPublic // by design, of course
@GwtCompatible
public @interface NotPublic {}
