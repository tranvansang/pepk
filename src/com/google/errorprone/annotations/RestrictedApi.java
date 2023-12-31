/*
 * Copyright 2016 The Error Prone Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.errorprone.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

// TODO(b/157082874): Allow restricting entire classes.
/**
 * Restrict this method to callsites with a allowlist annotation.
 *
 * <p>Callers that are not allowlisted will cause a configurable compiler diagnostic. Allowlisting
 * can either allow the call outright, or make the compiler emit a warning when the API is called.
 * Paths matching a regular expression, e.g. unit tests, can also be excluded.
 *
 * <p>The following example shows a hypothetical, potentially unsafe {@code Foo.bar} method. It is
 * marked with the {@code @RestrictedApi} annotations such that callers annotated with
 * {@code @LegacyUnsafeFooBar} raise a warning, whereas the {@code @ReviewedFooBar} annotation
 * silently allows the call.
 *
 * <p>The {@code @LegacyUnsafeFooBar} annotation can be used to allow existing call sites until they
 * are refactored, while prohibiting new call-sites. Call-sites determined to be acceptable, for
 * example through code review, could be marked {@code @ReviewedFooBar}.
 *
 * <pre>{@code
 * public {@literal @}interface LegacyUnsafeFooBar{}
 *
 * public {@literal @}interface ReviewedFooBar{
 *  public string reviewer();
 *  public string comments();
 * }
 *
 * public class Foo {
 *   {@literal @}RestrictedApi(
 *      explanation="You could shoot yourself in the foot with Foo.bar if you aren't careful",
 *      link="http://edsger.dijkstra/foo_bar_consider_harmful.html",
 *      allowedOnPath="testsuite/.*", // Unsafe behavior in tests is ok.
 *      allowlistAnnotations = {ReviewedFooBar.class},
 *      allowlistWithWarningAnnotations = {LegacyUnsafeFooBar.class})
 *   public void bar() {
 *      if (complicatedCondition) {
 *          shoot_your_foot();
 *      } else {
 *          solve_your_problem();
 *      }
 *   }
 *   boolean complicatedCondition = true;
 *
 *   {@literal @}ReviewedFooBar(
 *      reviewer="bangert",
 *      comments="Makes sure complicatedCondition isn't true, so bar is safe!"
 *   )
 *   public void safeBar() {
 *      if (!complicatedCondition) {
 *          bar();
 *      }
 *   }
 *
 *   {@literal @}LegacyUnsafeFooBar
 *   public void someOldCode() {
 *      // ...
 *      bar()
 *      // ...
 *   }
 * }
 * }</pre>
 */
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface RestrictedApi {
  /** Explanation why the API is restricted, to be inserted into the compiler output. */
  String explanation();

  /** Link explaining why the API is restricted */
  String link();

  /**
   * Allow the restricted API on paths matching this regular expression.
   *
   * <p>Leave empty (the default) to enforce the API restrictions on all paths.
   */
  String allowedOnPath() default "";

  /** Allow calls to the restricted API in methods or classes with this annotation. */
  Class<? extends Annotation>[] allowlistAnnotations() default {};

  /**
   * Emit warnings, not errors, on calls to the restricted API for callers with this annotation.
   *
   * <p>This should only be used if callers should aggressively move away from this API (or change
   * to a allowlist annotation after review). Too many warnings will lead to ALL warnings being
   * ignored, so tread very carefully.
   */
  Class<? extends Annotation>[] allowlistWithWarningAnnotations() default {};

  /* MOE:begin_strip */

  /**
   * Suggest adding this annotation to the class surrounding any violating code.
   *
   * <p>If this value is set, users that attempt to use this @RestrictedApi will see a suggestion to
   * annotate their code with {@code suggestedAllowlistAnnotation}. This annotation should also
   * appear in one of {@link #allowlistAnnotations} or {@link #allowlistWithWarningAnnotations}. Use
   * this automatically allowlist existing callers when introducing a new {@code @RestrictedApi}
   * annotation.
   *
   * <p>In order to run google3-wide refactoring tooling to automatically add allowlist annotations
   * to existing code,
   */
  Class<? extends Annotation> suggestedAllowlistAnnotation() default DontSuggestFixes.class;

  boolean allowedInTestonlyTargets() default false;

  /**
   * When true, emits a suggestion that can be picked up by refactoring tooling, instead of an
   * error.
   *
   * <p>In order to run google3-wide refactoring tooling to automatically add allowlist annotations
   * to existing code,
   */
  boolean warningOnlyForRefactoring() default false;
  /* MOE:end_strip */
}
