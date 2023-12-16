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

import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated type usage includes {@code null} as a value. To understand the
 * nullness of <i>unannotated</i> type usages, check for {@link NullMarked} on the enclosing class,
 * package, or module. See the <a href="http://jspecify.org/user-guide">JSpecify User Guide</a> for
 * details.
 *
 * <p><b>WARNING:</b> Though this annotation is <a href="http://go/nullness-faq#which-nullable">safe
 * to use inside google3</a>, do <b>NOT</b> release libraries that use it publicly: While it is
 * unlikely to change, we have not officially finalized it, so we want to retain the ability to
 * update all its usages if necessary.
 */
@Documented
@Target(TYPE_USE)
@Retention(RUNTIME)
public @interface Nullable {}
