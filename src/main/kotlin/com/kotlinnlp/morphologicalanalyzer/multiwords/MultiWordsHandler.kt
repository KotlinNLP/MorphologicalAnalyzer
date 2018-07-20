/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.multiwords

import com.kotlinnlp.linguisticdescription.sentence.multiwords.MultiWords
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary

/**
 * The multi-word expressions analyzer.
 *
 * @param dictionary a morphology dictionary
 */
internal class MultiWordsHandler(private val dictionary: MorphologyDictionary) {

  /**
   * @param tokens a list of input tokens
   *
   * @return the list of morphologies of the multi-words recognized in the given list of [tokens]
   */
  fun getMultiWordMorphologies(tokens: List<RealToken>): List<MultiWords> {

    val morphologies = mutableListOf<MultiWords>()

    (0 until tokens.size).forEach { tokenIndex ->

      this.getValidMultiWords(tokens = tokens, tokenIndex = tokenIndex).forEach { multiWord ->
        morphologies.add(
          MultiWords(
            startToken = tokenIndex,
            endToken = tokenIndex + multiWord.getNumOfSpaces(),
            morphologies = this.dictionary[multiWord]!!.morphologies)
        )
      }
    }

    return morphologies
  }

  /**
   * Get the list of multi-words expressions that start with the token at the given index of the [tokens] list.
   *
   * @param tokens a list of input tokens
   * @param tokenIndex the index of the currently focused token (within the [tokens] list)
   *
   * @return a list of multi-words expressions (empty if no one has been found)
   */
  private fun getValidMultiWords(tokens: List<RealToken>, tokenIndex: Int): List<String> {

    val validMultiWords = mutableListOf<String>()

    var candidates: Set<String> = this.dictionary.getMultiWordsIntroducedBy(tokens[tokenIndex].form).toSet()
    var followingTokenIndex: Int = tokenIndex

    while (candidates.size > 1 && ++followingTokenIndex < tokens.size) {

      val multiWords: List<String> = this.dictionary.getMultiWords(word = tokens[followingTokenIndex].form)

      if (multiWords.isEmpty()) break

      candidates = candidates.intersect(multiWords)

      val addingTokens = followingTokenIndex - tokenIndex

      candidates
        .filter { it.getNumOfSpaces() == addingTokens } // keep only the candidates that match exactly the sequence of
        .forEach { validMultiWords.add(it) }            // the current token and all the adding tokens
    }

    return validMultiWords
  }

  /**
   * @return the number of space chars (' ') in this string
   */
  private fun String.getNumOfSpaces(): Int = this.sumBy { if (it == ' ') 1 else 0 }
}