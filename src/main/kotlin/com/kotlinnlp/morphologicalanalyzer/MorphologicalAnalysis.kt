/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer

import com.kotlinnlp.linguisticdescription.morphology.dictionary.MorphologyEntry

/**
 * The morphological analysis of a tokenized text.
 *
 * @property tokens the list of tokens morphologies
 * @property multiWords the list of multi-words morphologies
 */
data class MorphologicalAnalysis(
  val tokens: List<List<MorphologyEntry>?>,
  val multiWords: List<MultiWordsMorphology>
) {

  /**
   * Map the index of each token to the list of multi-words morphologies in which it is involved.
   */
  private val tokensToMultiWords: Map<Int, List<MultiWordsMorphology>> =
    mapOf(*(0 until this.tokens.size)
      .map { tokenIndex -> Pair(tokenIndex, this.multiWords.filter { tokenIndex in (it.startToken .. it.endToken) }) }
      .toTypedArray())

  /**
   * Get the list of multi-words morphologies in which a given token is involved.
   *
   * @param tokenIndex the index of a token
   *
   * @return a list of multi-words morphologies or null if no one has been found
   */
  fun getInvolvedMultiWords(tokenIndex: Int): List<MultiWordsMorphology>? = this.tokensToMultiWords[tokenIndex]
}
