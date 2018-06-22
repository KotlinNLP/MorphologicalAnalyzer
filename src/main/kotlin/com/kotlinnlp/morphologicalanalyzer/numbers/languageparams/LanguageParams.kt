/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.languageparams

/**
 * The object that contains the language-specific parameters
 *
 * @property language the two-letters iso code of the language
 * @property digitDecimalSeparator the separator between nteger and decimal part of digit numbers
 * @property wordDecimalSeparator the separator between nteger and decimal part of word numbers
 * @property thousandSeparator the thousand separator of digit numbers
 * @property generatorFlags flags that activate features of the digit to word generator
 * @property numbers map of numbers to their word representation in the current language
 * @property words map of specific words in the current language
 * @property suff map of the words to use to represent a certain number multiplier in the current language
 *                with sub-keys for singular and plural
 */
data class LanguageParams(
  val language: String,
  val digitDecimalSeparator: String,
  val wordDecimalSeparator: String,
  val thousandSeparator: String,
  val generatorFlags: GeneratorFlags,
  val numbers: Map<String, String>,
  val words: Map<String, String>,
  val suff: Map<String, Map<String, String>>
)