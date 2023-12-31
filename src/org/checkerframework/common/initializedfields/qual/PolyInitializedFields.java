package org.checkerframework.common.initializedfields.qual;

import org.checkerframework.framework.qual.PolymorphicQualifier;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Polymorphic qualifier for the Initialized Fields type system.
 *
 * @checker_framework.manual #initialized-fields-checker Initialized Fields Checker
 */
@PolymorphicQualifier(InitializedFields.class)
@Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
public @interface PolyInitializedFields {}
