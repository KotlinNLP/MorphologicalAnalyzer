/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.listeners.helpers

/**
 * Collect the type/value pairs that have been annotated during the tree visit of the ANTLR grammar listener.
 */
internal class AnnotationsAccumulator {

  /**
   * Store pairs of <type, value> annotations.
   */
  private val values = mutableListOf<Pair<String, String>>()

  /**
   * The first value inserted.
   */
  val firstValue: String get() = this.values[0].second

  /**
   * Accumulate a new annotation pair.
   *
   * @param type the type of the annotation
   * @param value the value of the annotation
   */
  fun push(type: String, value: String) {

    this.values.add(Pair(type, value))
  }

  /**
   * Iterate over the annotation pairs.
   *
   * @param callback a callback called for each pair
   */
  fun forEachAnnotation(callback: (Pair<String, String>) -> Unit) = this.values.forEach { callback(it) }

  /**
   * @return the accumulated annotation values concatenated into a unique string
   */
  fun getConcatValues(): String = this.values.joinToString(separator = "") { it.second }
}
