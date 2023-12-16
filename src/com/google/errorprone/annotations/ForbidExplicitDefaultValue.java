/*
 * Copyright 2019 The Error Prone Authors.
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

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that an annotation type element should never be explicitly given the default value.
 *
 * <p>For example, given the following annotation
 *
 * <pre>
 * &#64;ForbidExplicitDefaultValue
 * &#64;interface Foo {
 *   int value() default 42;
 * }
 * </pre>
 *
 * <p>it is permitted to write {@code @Foo} (where {@code Foo#value} has an <em>implicit</em>
 * default value of {@code 42}), or {@code @Foo(43)} (where there is an explicit value other than
 * the default), but it is <em>not</em> permitted to write {@code @Foo(42)} (since there is an
 * explicit default value).
 */
@Target(METHOD)
@Retention(RUNTIME)
@GoogleInternal
public @interface ForbidExplicitDefaultValue {}
