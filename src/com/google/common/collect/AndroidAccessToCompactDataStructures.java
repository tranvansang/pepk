/*
 * Copyright (C) 2019 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.common.collect;

import com.google.common.annotations.GoogleInternal;
import com.google.common.annotations.GwtIncompatible;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * Provides access to {@link CompactHashMap}, {@link CompactHashSet}, {@link CompactLinkedHashMap},
 * and {@link CompactLinkedHashSet} under Android. They're not directly available there because we
 * currently expose only Guava-public classes to our internal Android users. Non-Android users
 * should use those classes directly.
 *
 * @deprecated From non-Android code, use {@link CompactHashMap}, {@link CompactHashSet}, {@link
 *     CompactLinkedHashMap}, and {@link CompactLinkedHashSet} directly.
 */
@Deprecated
@GoogleInternal
@GwtIncompatible
@NullMarked
public final class AndroidAccessToCompactDataStructures {
  /** Non-Android users should use {@link CompactHashMap#create}. */
  public static <K extends @Nullable Object, V extends @Nullable Object>
      Map<K, V> newCompactHashMap() {
    return CompactHashMap.create();
  }

  /** Non-Android users should use {@link CompactHashMap#createWithExpectedSize}. */
  public static <K extends @Nullable Object, V extends @Nullable Object>
      Map<K, V> newCompactHashMap(int expectedSize) {
    return CompactHashMap.createWithExpectedSize(expectedSize);
  }

  /** Non-Android users should use {@link CompactHashSet#create}. */
  public static <E extends @Nullable Object> Set<E> newCompactHashSet() {
    return CompactHashSet.create();
  }

  /** Non-Android users should use {@link CompactHashSet#create(Collection)}. */
  public static <E extends @Nullable Object> Set<E> newCompactHashSet(
      Collection<? extends E> collection) {
    return CompactHashSet.create(collection);
  }

  /** Non-Android users should use {@link CompactHashSet#create(E...)}. */
  @SafeVarargs
  public static <E extends @Nullable Object> Set<E> newCompactHashSet(E... elements) {
    return CompactHashSet.create(elements);
  }

  /** Non-Android users should use {@link CompactHashSet#createWithExpectedSize}. */
  public static <E extends @Nullable Object> Set<E> newCompactHashSet(int expectedSize) {
    return CompactHashSet.createWithExpectedSize(expectedSize);
  }

  /** Non-Android users should use {@link CompactLinkedHashMap#create}. */
  public static <K extends @Nullable Object, V extends @Nullable Object>
      Map<K, V> newCompactLinkedHashMap() {
    return CompactLinkedHashMap.create();
  }

  /** Non-Android users should use {@link CompactLinkedHashMap#createWithExpectedSize}. */
  public static <K extends @Nullable Object, V extends @Nullable Object>
      Map<K, V> newCompactLinkedHashMap(int expectedSize) {
    return CompactLinkedHashMap.createWithExpectedSize(expectedSize);
  }

  /** Non-Android users should use {@link CompactLinkedHashSet#create}. */
  public static <E extends @Nullable Object> Set<E> newCompactLinkedHashSet() {
    return CompactLinkedHashSet.create();
  }

  /** Non-Android users should use {@link CompactLinkedHashSet#create(Collection)}. */
  public static <E extends @Nullable Object> Set<E> newCompactLinkedHashSet(
      Collection<? extends E> collection) {
    return CompactLinkedHashSet.create(collection);
  }

  /** Non-Android users should use {@link CompactLinkedHashSet#create(E...)}. */
  @SafeVarargs
  public static <E extends @Nullable Object> Set<E> newCompactLinkedHashSet(E... elements) {
    return CompactLinkedHashSet.create(elements);
  }

  /** Non-Android users should use {@link CompactLinkedHashSet#createWithExpectedSize}. */
  public static <E extends @Nullable Object> Set<E> newCompactLinkedHashSet(int expectedSize) {
    return CompactLinkedHashSet.createWithExpectedSize(expectedSize);
  }

  private AndroidAccessToCompactDataStructures() {}
}
