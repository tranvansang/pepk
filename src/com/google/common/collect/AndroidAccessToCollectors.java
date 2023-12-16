/*
 * Copyright (C) 2020 The Guava Authors
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
import com.google.common.annotations.GwtCompatible;
import com.google.common.annotations.GwtIncompatible;
import com.google.errorprone.annotations.PreferStaticImport;
import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jspecify.nullness.NullMarked;
import org.jspecify.nullness.Nullable;

/**
 * An extension file for Android applications that are using the Java7 branch of Guava, but can use
 * Java8. This gives Android applications access to Guava’s collectors. Android libraries must
 * depend on an additional target, {@code collect8-android}, to use it.
 *
 * <p>Server (and GWT) applications generally have no need to depend on this file. However, library
 * code that can be used from both Android and server/GWT can safely rely on this file, rather than
 * the collectors found inside the collection classes.
 */
@GoogleInternal
@GwtCompatible(emulated = true)
@NullMarked
public final class AndroidAccessToCollectors {

  // Lists

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code
   * ImmutableList}, in encounter order.
   */
  @PreferStaticImport // MOE:strip_line
  public static <E> Collector<E, ?, ImmutableList<E>> toImmutableList() {
    return CollectCollectors.toImmutableList();
  }

  // Sets

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code
   * ImmutableSet}. Elements appear in the resulting set in the encounter order of the stream; if
   * the stream contains duplicates (according to {@link Object#equals(Object)}), only the first
   * duplicate in encounter order will appear in the result.
   */
  @PreferStaticImport // MOE:strip_line
  public static <E> Collector<E, ?, ImmutableSet<E>> toImmutableSet() {
    return CollectCollectors.toImmutableSet();
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code
   * ImmutableSortedSet}, ordered by the specified comparator.
   *
   * <p>If the elements contain duplicates (according to the comparator), only the first duplicate
   * in encounter order will appear in the result.
   */
  @PreferStaticImport // MOE:strip_line
  public static <E> Collector<E, ?, ImmutableSortedSet<E>> toImmutableSortedSet(
      Comparator<? super E> comparator) {
    return CollectCollectors.toImmutableSortedSet(comparator);
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code ImmutableSet}
   * with an implementation specialized for enums. Unlike {@link ImmutableSet#toImmutableSet}, the
   * resulting set will iterate over elements in their enum definition order, not encounter order.
   */
  @PreferStaticImport // MOE:strip_line
  public static <E extends Enum<E>> Collector<E, ?, ImmutableSet<E>> toImmutableEnumSet() {
    return CollectCollectors.toImmutableEnumSet();
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code
   * ImmutableRangeSet}. As in {@link ImmutableRangeSet.Builder}, overlapping ranges are not
   * permitted and adjacent ranges will be merged.
   */
  @PreferStaticImport // MOE:strip_line
  @GwtIncompatible
  public static <E extends Comparable<? super E>>
      Collector<Range<E>, ?, ImmutableRangeSet<E>> toImmutableRangeSet() {
    return CollectCollectors.toImmutableRangeSet();
  }

  // Multisets

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code
   * ImmutableMultiset}. Elements iterate in order by the <i>first</i> appearance of that element in
   * encounter order.
   */
  @PreferStaticImport // MOE:strip_line
  public static <E> Collector<E, ?, ImmutableMultiset<E>> toImmutableMultiset() {
    return CollectCollectors.toImmutableMultiset(Function.identity(), e -> 1);
  }

  /**
   * Returns a {@code Collector} that accumulates elements into an {@code ImmutableMultiset} whose
   * elements are the result of applying {@code elementFunction} to the inputs, with counts equal to
   * the result of applying {@code countFunction} to the inputs.
   *
   * <p>If the mapped elements contain duplicates (according to {@link Object#equals}), the first
   * occurrence in encounter order appears in the resulting multiset, with count equal to the sum of
   * the outputs of {@code countFunction.applyAsInt(t)} for each {@code t} mapped to that element.
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, E>
      Collector<T, ?, ImmutableMultiset<E>> toImmutableMultiset(
          Function<? super T, ? extends E> elementFunction,
          ToIntFunction<? super T> countFunction) {
    return CollectCollectors.toImmutableMultiset(elementFunction, countFunction);
  }

  /**
   * Returns a {@code Collector} that accumulates elements into a multiset created via the specified
   * {@code Supplier}, whose elements are the result of applying {@code elementFunction} to the
   * inputs, with counts equal to the result of applying {@code countFunction} to the inputs.
   * Elements are added in encounter order.
   *
   * <p>If the mapped elements contain duplicates (according to {@link Object#equals}), the element
   * will be added more than once, with the count summed over all appearances of the element.
   *
   * <p>Note that {@code stream.collect(toMultiset(function, e -> 1, supplier))} is equivalent to
   * {@code stream.map(function).collect(Collectors.toCollection(supplier))}.
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, E extends @Nullable Object, M extends Multiset<E>>
      Collector<T, ?, M> toMultiset(
          Function<? super T, E> elementFunction,
          ToIntFunction<? super T> countFunction,
          Supplier<M> multisetSupplier) {
    return CollectCollectors.toMultiset(elementFunction, countFunction, multisetSupplier);
  }

  // Maps

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableMap} whose keys
   * and values are the result of applying the provided mapping functions to the input elements.
   * Entries appear in the result {@code ImmutableMap} in encounter order.
   *
   * <p>If the mapped keys contain duplicates (according to {@link Object#equals(Object)}, an {@code
   * IllegalArgumentException} is thrown when the collection operation is performed. (This differs
   * from the {@code Collector} returned by {@link Collectors#toMap(Function, Function)}, which
   * throws an {@code IllegalStateException}.)
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction) {
    return CollectCollectors.toImmutableMap(keyFunction, valueFunction);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableMap} whose keys
   * and values are the result of applying the provided mapping functions to the input elements.
   *
   * <p>If the mapped keys contain duplicates (according to {@link Object#equals(Object)}), the
   * values are merged using the specified merging function. Entries will appear in the encounter
   * order of the first occurrence of the key.
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableMap<K, V>> toImmutableMap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction,
          BinaryOperator<V> mergeFunction) {
    return CollectCollectors.toImmutableMap(keyFunction, valueFunction, mergeFunction);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableSortedMap} whose
   * keys and values are the result of applying the provided mapping functions to the input
   * elements. The generated map is sorted by the specified comparator.
   *
   * <p>If the mapped keys contain duplicates (according to the specified comparator), an {@code
   * IllegalArgumentException} is thrown when the collection operation is performed. (This differs
   * from the {@code Collector} returned by {@link Collectors#toMap(Function, Function)}, which
   * throws an {@code IllegalStateException}.)
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(
          Comparator<? super K> comparator,
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction) {
    return CollectCollectors.toImmutableSortedMap(comparator, keyFunction, valueFunction);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableSortedMap} whose
   * keys and values are the result of applying the provided mapping functions to the input
   * elements.
   *
   * <p>If the mapped keys contain duplicates (according to the comparator), the values are merged
   * using the specified merging function. Entries will appear in the encounter order of the first
   * occurrence of the key.
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableSortedMap<K, V>> toImmutableSortedMap(
          Comparator<? super K> comparator,
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction,
          BinaryOperator<V> mergeFunction) {
    return CollectCollectors.toImmutableSortedMap(
        comparator, keyFunction, valueFunction, mergeFunction);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableBiMap} whose keys
   * and values are the result of applying the provided mapping functions to the input elements.
   * Entries appear in the result {@code ImmutableBiMap} in encounter order.
   *
   * <p>If the mapped keys or values contain duplicates (according to {@link Object#equals(Object)},
   * an {@code IllegalArgumentException} is thrown when the collection operation is performed. (This
   * differs from the {@code Collector} returned by {@link Collectors#toMap(Function, Function)},
   * which throws an {@code IllegalStateException}.)
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableBiMap<K, V>> toImmutableBiMap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction) {
    return CollectCollectors.toImmutableBiMap(keyFunction, valueFunction);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableMap} whose keys
   * and values are the result of applying the provided mapping functions to the input elements. The
   * resulting implementation is specialized for enum key types. The returned map and its views will
   * iterate over keys in their enum definition order, not encounter order.
   *
   * <p>If the mapped keys contain duplicates, an {@code IllegalArgumentException} is thrown when
   * the collection operation is performed. (This differs from the {@code Collector} returned by
   * {@link Collectors#toMap(Function, Function) Collectors.toMap(Function, Function)}, which throws
   * an {@code IllegalStateException}.)
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K extends Enum<K>, V>
      Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction) {
    return CollectCollectors.toImmutableEnumMap(keyFunction, valueFunction);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableMap} whose keys
   * and values are the result of applying the provided mapping functions to the input elements. The
   * resulting implementation is specialized for enum key types. The returned map and its views will
   * iterate over keys in their enum definition order, not encounter order.
   *
   * <p>If the mapped keys contain duplicates, the values are merged using the specified merging
   * function.
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K extends Enum<K>, V>
      Collector<T, ?, ImmutableMap<K, V>> toImmutableEnumMap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction,
          BinaryOperator<V> mergeFunction) {
    return CollectCollectors.toImmutableEnumMap(keyFunction, valueFunction, mergeFunction);
  }

  /**
   * Returns a {@code Collector} that accumulates the input elements into a new {@code
   * ImmutableRangeMap}. As in {@link ImmutableRangeMap.Builder}, overlapping ranges are not
   * permitted.
   */
  @PreferStaticImport // MOE:strip_line
  @GwtIncompatible
  public static <T extends @Nullable Object, K extends Comparable<? super K>, V>
      Collector<T, ?, ImmutableRangeMap<K, V>> toImmutableRangeMap(
          Function<? super T, Range<K>> keyFunction,
          Function<? super T, ? extends V> valueFunction) {
    return CollectCollectors.toImmutableRangeMap(keyFunction, valueFunction);
  }

  // Multimaps

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableListMultimap}
   * whose keys and values are the result of applying the provided mapping functions to the input
   * elements.
   *
   * <p>For streams with defined encounter order (as defined in the Ordering section of the {@link
   * java.util.stream} Javadoc), that order is preserved, but entries are <a
   * href="ImmutableMultimap.html#iteration">grouped by key</a>.
   *
   * <p>Example:
   *
   * <pre>{@code
   * static final Multimap<Character, String> FIRST_LETTER_MULTIMAP =
   *     Stream.of("banana", "apple", "carrot", "asparagus", "cherry")
   *         .collect(toImmutableListMultimap(str -> str.charAt(0), str -> str.substring(1)));
   *
   * // is equivalent to
   *
   * static final Multimap<Character, String> FIRST_LETTER_MULTIMAP =
   *     new ImmutableListMultimap.Builder<Character, String>()
   *         .put('b', "anana")
   *         .putAll('a', "pple", "sparagus")
   *         .putAll('c', "arrot", "herry")
   *         .build();
   * }</pre>
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableListMultimap<K, V>> toImmutableListMultimap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction) {
    return CollectCollectors.toImmutableListMultimap(keyFunction, valueFunction);
  }

  /**
   * Returns a {@code Collector} accumulating entries into an {@code ImmutableListMultimap}. Each
   * input element is mapped to a key and a stream of values, each of which are put into the
   * resulting {@code Multimap}, in the encounter order of the stream and the encounter order of the
   * streams of values.
   *
   * <p>Example:
   *
   * <pre>{@code
   * static final ImmutableListMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
   *     Stream.of("banana", "apple", "carrot", "asparagus", "cherry")
   *         .collect(
   *             flatteningToImmutableListMultimap(
   *                  str -> str.charAt(0),
   *                  str -> str.substring(1).chars().mapToObj(c -> (char) c));
   *
   * // is equivalent to
   *
   * static final ImmutableListMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
   *     ImmutableListMultimap.<Character, Character>builder()
   *         .putAll('b', Arrays.asList('a', 'n', 'a', 'n', 'a'))
   *         .putAll('a', Arrays.asList('p', 'p', 'l', 'e'))
   *         .putAll('c', Arrays.asList('a', 'r', 'r', 'o', 't'))
   *         .putAll('a', Arrays.asList('s', 'p', 'a', 'r', 'a', 'g', 'u', 's'))
   *         .putAll('c', Arrays.asList('h', 'e', 'r', 'r', 'y'))
   *         .build();
   * }
   * }</pre>
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableListMultimap<K, V>> flatteningToImmutableListMultimap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
    return CollectCollectors.flatteningToImmutableListMultimap(keyFunction, valuesFunction);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into an {@code ImmutableSetMultimap}
   * whose keys and values are the result of applying the provided mapping functions to the input
   * elements.
   *
   * <p>For streams with defined encounter order (as defined in the Ordering section of the {@link
   * java.util.stream} Javadoc), that order is preserved, but entries are <a
   * href="ImmutableMultimap.html#iteration">grouped by key</a>.
   *
   * <p>Example:
   *
   * <pre>{@code
   * static final Multimap<Character, String> FIRST_LETTER_MULTIMAP =
   *     Stream.of("banana", "apple", "carrot", "asparagus", "cherry")
   *         .collect(toImmutableSetMultimap(str -> str.charAt(0), str -> str.substring(1)));
   *
   * // is equivalent to
   *
   * static final Multimap<Character, String> FIRST_LETTER_MULTIMAP =
   *     new ImmutableSetMultimap.Builder<Character, String>()
   *         .put('b', "anana")
   *         .putAll('a', "pple", "sparagus")
   *         .putAll('c', "arrot", "herry")
   *         .build();
   * }</pre>
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableSetMultimap<K, V>> toImmutableSetMultimap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction) {
    return CollectCollectors.toImmutableSetMultimap(keyFunction, valueFunction);
  }

  /**
   * Returns a {@code Collector} accumulating entries into an {@code ImmutableSetMultimap}. Each
   * input element is mapped to a key and a stream of values, each of which are put into the
   * resulting {@code Multimap}, in the encounter order of the stream and the encounter order of the
   * streams of values.
   *
   * <p>Example:
   *
   * <pre>{@code
   * static final ImmutableSetMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
   *     Stream.of("banana", "apple", "carrot", "asparagus", "cherry")
   *         .collect(
   *             flatteningToImmutableSetMultimap(
   *                  str -> str.charAt(0),
   *                  str -> str.substring(1).chars().mapToObj(c -> (char) c));
   *
   * // is equivalent to
   *
   * static final ImmutableSetMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
   *     ImmutableSetMultimap.<Character, Character>builder()
   *         .putAll('b', Arrays.asList('a', 'n', 'a', 'n', 'a'))
   *         .putAll('a', Arrays.asList('p', 'p', 'l', 'e'))
   *         .putAll('c', Arrays.asList('a', 'r', 'r', 'o', 't'))
   *         .putAll('a', Arrays.asList('s', 'p', 'a', 'r', 'a', 'g', 'u', 's'))
   *         .putAll('c', Arrays.asList('h', 'e', 'r', 'r', 'y'))
   *         .build();
   *
   * // after deduplication, the resulting multimap is equivalent to
   *
   * static final ImmutableSetMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
   *     ImmutableSetMultimap.<Character, Character>builder()
   *         .putAll('b', Arrays.asList('a', 'n'))
   *         .putAll('a', Arrays.asList('p', 'l', 'e', 's', 'a', 'r', 'g', 'u'))
   *         .putAll('c', Arrays.asList('a', 'r', 'o', 't', 'h', 'e', 'y'))
   *         .build();
   * }
   * }</pre>
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, K, V>
      Collector<T, ?, ImmutableSetMultimap<K, V>> flatteningToImmutableSetMultimap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends Stream<? extends V>> valuesFunction) {
    return CollectCollectors.flatteningToImmutableSetMultimap(keyFunction, valuesFunction);
  }

  /**
   * Returns a {@code Collector} accumulating entries into a {@code Multimap} generated from the
   * specified supplier. The keys and values of the entries are the result of applying the provided
   * mapping functions to the input elements, accumulated in the encounter order of the stream.
   *
   * <p>Example:
   *
   * <pre>{@code
   * static final ListMultimap<Character, String> FIRST_LETTER_MULTIMAP =
   *     Stream.of("banana", "apple", "carrot", "asparagus", "cherry")
   *         .collect(
   *             toMultimap(
   *                  str -> str.charAt(0),
   *                  str -> str.substring(1),
   *                  MultimapBuilder.treeKeys().arrayListValues()::build));
   *
   * // is equivalent to
   *
   * static final ListMultimap<Character, String> FIRST_LETTER_MULTIMAP;
   *
   * static {
   *     FIRST_LETTER_MULTIMAP = MultimapBuilder.treeKeys().arrayListValues().build();
   *     FIRST_LETTER_MULTIMAP.put('b', "anana");
   *     FIRST_LETTER_MULTIMAP.put('a', "pple");
   *     FIRST_LETTER_MULTIMAP.put('a', "sparagus");
   *     FIRST_LETTER_MULTIMAP.put('c', "arrot");
   *     FIRST_LETTER_MULTIMAP.put('c', "herry");
   * }
   * }</pre>
   */
  @PreferStaticImport // MOE:strip_line
  public static <
          T extends @Nullable Object,
          K extends @Nullable Object,
          V extends @Nullable Object,
          M extends Multimap<K, V>>
      Collector<T, ?, M> toMultimap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends V> valueFunction,
          Supplier<M> multimapSupplier) {
    return CollectCollectors.toMultimap(keyFunction, valueFunction, multimapSupplier);
  }

  /**
   * Returns a {@code Collector} accumulating entries into a {@code Multimap} generated from the
   * specified supplier. Each input element is mapped to a key and a stream of values, each of which
   * are put into the resulting {@code Multimap}, in the encounter order of the stream and the
   * encounter order of the streams of values.
   *
   * <p>Example:
   *
   * <pre>{@code
   * static final ListMultimap<Character, Character> FIRST_LETTER_MULTIMAP =
   *     Stream.of("banana", "apple", "carrot", "asparagus", "cherry")
   *         .collect(
   *             flatteningToMultimap(
   *                  str -> str.charAt(0),
   *                  str -> str.substring(1).chars().mapToObj(c -> (char) c),
   *                  MultimapBuilder.linkedHashKeys().arrayListValues()::build));
   *
   * // is equivalent to
   *
   * static final ListMultimap<Character, Character> FIRST_LETTER_MULTIMAP;
   *
   * static {
   *     FIRST_LETTER_MULTIMAP = MultimapBuilder.linkedHashKeys().arrayListValues().build();
   *     FIRST_LETTER_MULTIMAP.putAll('b', Arrays.asList('a', 'n', 'a', 'n', 'a'));
   *     FIRST_LETTER_MULTIMAP.putAll('a', Arrays.asList('p', 'p', 'l', 'e'));
   *     FIRST_LETTER_MULTIMAP.putAll('c', Arrays.asList('a', 'r', 'r', 'o', 't'));
   *     FIRST_LETTER_MULTIMAP.putAll('a', Arrays.asList('s', 'p', 'a', 'r', 'a', 'g', 'u', 's'));
   *     FIRST_LETTER_MULTIMAP.putAll('c', Arrays.asList('h', 'e', 'r', 'r', 'y'));
   * }
   * }</pre>
   */
  @PreferStaticImport // MOE:strip_line
  public static <
          T extends @Nullable Object,
          K extends @Nullable Object,
          V extends @Nullable Object,
          M extends Multimap<K, V>>
      Collector<T, ?, M> flatteningToMultimap(
          Function<? super T, ? extends K> keyFunction,
          Function<? super T, ? extends Stream<? extends V>> valueFunction,
          Supplier<M> multimapSupplier) {
    return CollectCollectors.flatteningToMultimap(keyFunction, valueFunction, multimapSupplier);
  }

  // Tables

  /**
   * Returns a {@code Collector} that accumulates elements into an {@code ImmutableTable}. Each
   * input element is mapped to one cell in the returned table, with the rows, columns, and values
   * generated by applying the specified functions.
   *
   * <p>The returned {@code Collector} will throw a {@code NullPointerException} at collection time
   * if the row, column, or value functions return null on any input.
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, R, C, V>
      Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(
          Function<? super T, ? extends R> rowFunction,
          Function<? super T, ? extends C> columnFunction,
          Function<? super T, ? extends V> valueFunction) {
    return TableCollectors.toImmutableTable(rowFunction, columnFunction, valueFunction);
  }

  /**
   * Returns a {@code Collector} that accumulates elements into an {@code ImmutableTable}. Each
   * input element is mapped to one cell in the returned table, with the rows, columns, and values
   * generated by applying the specified functions. If multiple inputs are mapped to the same row
   * and column pair, they will be combined with the specified merging function in encounter order.
   *
   * <p>The returned {@code Collector} will throw a {@code NullPointerException} at collection time
   * if the row, column, value, or merging functions return null on any input.
   */
  @PreferStaticImport // MOE:strip_line
  public static <T extends @Nullable Object, R, C, V>
      Collector<T, ?, ImmutableTable<R, C, V>> toImmutableTable(
          Function<? super T, ? extends R> rowFunction,
          Function<? super T, ? extends C> columnFunction,
          Function<? super T, ? extends V> valueFunction,
          BinaryOperator<V> mergeFunction) {
    return TableCollectors.toImmutableTable(
        rowFunction, columnFunction, valueFunction, mergeFunction);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into a {@code Table} created using the
   * specified supplier, whose cells are generated by applying the provided mapping functions to the
   * input elements. Cells are inserted into the generated {@code Table} in encounter order.
   *
   * <p>If multiple input elements map to the same row and column, an {@code IllegalStateException}
   * is thrown when the collection operation is performed.
   */
  @PreferStaticImport // MOE:strip_line
  public static <
          T extends @Nullable Object,
          R extends @Nullable Object,
          C extends @Nullable Object,
          V extends @Nullable Object,
          I extends Table<R, C, V>>
      Collector<T, ?, I> toTable(
          Function<? super T, ? extends R> rowFunction,
          Function<? super T, ? extends C> columnFunction,
          Function<? super T, ? extends V> valueFunction,
          Supplier<I> tableSupplier) {
    return TableCollectors.toTable(rowFunction, columnFunction, valueFunction, tableSupplier);
  }

  /**
   * Returns a {@link Collector} that accumulates elements into a {@code Table} created using the
   * specified supplier, whose cells are generated by applying the provided mapping functions to the
   * input elements. Cells are inserted into the generated {@code Table} in encounter order.
   *
   * <p>If multiple input elements map to the same row and column, the specified merging function is
   * used to combine the values. Like {@link Collectors#toMap(Function, Function, BinaryOperator,
   * Supplier)}, this Collector throws a {@code NullPointerException} on null values returned from
   * {@code valueFunction}, and treats nulls returned from {@code mergeFunction} as removals of that
   * row/column pair.
   */
  @PreferStaticImport // MOE:strip_line
  public static <
          T extends @Nullable Object,
          R extends @Nullable Object,
          C extends @Nullable Object,
          V extends @Nullable Object,
          I extends Table<R, C, V>>
      Collector<T, ?, I> toTable(
          Function<? super T, ? extends R> rowFunction,
          Function<? super T, ? extends C> columnFunction,
          Function<? super T, ? extends V> valueFunction,
          BinaryOperator<V> mergeFunction,
          Supplier<I> tableSupplier) {
    return TableCollectors.toTable(
        rowFunction, columnFunction, valueFunction, mergeFunction, tableSupplier);
  }

  private AndroidAccessToCollectors() {}
}
