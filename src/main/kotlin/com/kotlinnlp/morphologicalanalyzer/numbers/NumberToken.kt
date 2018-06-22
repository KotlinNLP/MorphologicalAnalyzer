/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers

/**
 * Represents a numeric expression recognized.
 *
 * @property startToken the index of the first token of this numeric expression, within the input tokens list
 * @property endToken the index of the last token of this numeric expression, within the input tokens list
 * @property asDigits the standard representation of the number in digits
 * @property asWord the standard representation of the number in letters
 * @property original the original string containing the number
 */
data class NumberToken(
  val startToken: Int,
  val endToken: Int,
  val asDigits: String,
  val asWord: String,
  val original: String
)
