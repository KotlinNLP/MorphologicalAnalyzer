/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers

import com.kotlinnlp.linguisticdescription.sentence.properties.TokensRange
import com.kotlinnlp.utils.JSONSerializable

/**
 * Represents a numeric expression recognized.
 *
 * @property startToken the index of the first token of this numeric expression, within the input tokens list
 * @property endToken the index of the last token of this numeric expression, within the input tokens list
 * @property startToken the index of the first char of this numeric expression
 * @property endToken the index of the last char of this numeric expression
 * @property value the numeric value
 * @property asWord the standard representation of the number in letters
 * @property original the original string representing the number
 */
data class Number(
  override val startToken: Int,
  override val endToken: Int,
  override val startChar: Int,
  override val endChar: Int,
  val value: kotlin.Number,
  val asWord: String,
  val original: String
) : TokensRange, JSONSerializable
