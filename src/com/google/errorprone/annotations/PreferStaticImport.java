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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Target;

/**
 * Indicates that the annotated member should be statically imported when used.
 *
 * <p>Annotated members should have names that are clear and unambiguous when used without the
 * enclosing class name. IDEs and refactoring tools are encouraged to respect static import
 * preferences when editing code.
 */
@GoogleInternal
@Documented
@IncompatibleModifiers(modifier = {Modifier.PRIVATE})
@RequiredModifiers(modifier = {Modifier.STATIC})
@Target({METHOD, FIELD, TYPE, ANNOTATION_TYPE})
public @interface PreferStaticImport {}
