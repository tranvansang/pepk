/*
 * Copyright 2018-2020 The JSpecify Authors.
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
package org.jspecify.nullness;

import static java.lang.annotation.ElementType.PACKAGE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that within the annotated scope (class, package, or module), type usages
 * <i>generally</i> do <i>not</i> include {@code null} as a value, unless they are individually
 * marked otherwise using {@link Nullable} (or checker-specific other annotations). Without this
 * annotation, unmarked type usages would instead have <i>unspecified nullness</i>. Several
 * exceptions to this rule and an explanation of unspecified nullness are covered in the <a
 * href="http://jspecify.org/user-guide">JSpecify User Guide</a>.
 *
 * <p><b>Note to users of Kotlin or go/nullness:</b> Inside the scope of {@code NullMarked}, the
 * meaning of {@code class Foo<T>} changes: Inside {@code NullMarked}, that declaration
 * <i>forbids</i> nullable type arguments (like {@code Foo<@Nullable Bar>}. If you wish to permit
 * such type arguments, you must declare the parameter with {@code class Foo<T extends @Nullable
 * Object>}. This change is <a href="http://go/nullness-faq#implicitly-bounded">a departure</a> from
 * how the upstream Checker Framework treats type-parameter declarations.
 *
 * <p><b>WARNING:</b> Though this annotation is <a href="http://go/nullness-faq#which-nullable">safe
 * to use inside google3</a>, do <b>NOT</b> release libraries that use it publicly: While it is
 * unlikely to change, we have not officially finalized it, so we want to retain the ability to
 * update all its usages if necessary.
 */
@Documented
@Target({TYPE, PACKAGE})
@Retention(RUNTIME)
public @interface NullMarked {}
