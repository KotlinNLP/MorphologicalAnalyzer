/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.multiwords

import com.kotlinnlp.linguisticdescription.morphology.Morphology
import com.kotlinnlp.linguisticdescription.sentence.properties.TokensRange

/**
 * A multi-words expression.
 *
 * @property startToken the index of the first token of the expression
 * @property endToken the index of the last token of the expression
 * @property morphologies the list of possible morphologies of the expression
 */
data class MultiWords(
  override val startToken: Int,
  override val endToken: Int,
  val morphologies: List<Morphology>
) : TokensRange
