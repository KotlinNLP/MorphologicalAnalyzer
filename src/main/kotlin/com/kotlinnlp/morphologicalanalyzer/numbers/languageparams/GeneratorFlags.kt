/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.languageparams

/**
 * Flags that activate features of the digit to word generator.
 *
 * @property elideDoubleVocal
 * @property spaceBetweenWords
 * @property hyphenBetweenTensAndUnit
 */
data class GeneratorFlags(
  val elideDoubleVocal: Boolean,
  val spaceBetweenWords: Boolean,
  val hyphenBetweenTensAndUnit: Boolean
)
