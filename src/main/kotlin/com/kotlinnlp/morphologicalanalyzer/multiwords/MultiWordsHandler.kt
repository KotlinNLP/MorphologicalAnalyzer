/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.multiwords

import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary
import com.kotlinnlp.neuraltokenizer.Token

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
  fun getMultiWordMorphologies(tokens: List<Token>): List<MultiWordsMorphology> {

    val morphologies = mutableListOf<MultiWordsMorphology>()

    tokens.forEachIndexed { tokenIndex, token ->

      if (!token.isSpace) {
        this.getValidMultiWords(tokens = tokens, tokenIndex = tokenIndex).forEach { multiWord ->

          val addingTokens: Int = multiWord.getNumOfSpaces()
          var followingTokens = 0
          val endIndex: Int = tokenIndex + 1 + tokens.subList(tokenIndex + 1, tokens.size).indexOfFirst {
            !it.isSpace && ++followingTokens == addingTokens
          }

          morphologies.add(
            MultiWordsMorphology(
              startToken = tokenIndex,
              endToken = endIndex,
              morphologies = this.dictionary[multiWord]!!.morphologies)
          )
        }
      }
    }

    return morphologies
  }

  /**
   * Get the list of multi-words expressions that start with the token at the given index of a [tokens] list.
   *
   * @param tokens a list of input tokens
   * @param tokenIndex the index of the currently focused token (within the [tokens] list)
   *
   * @return a list of multi-words expressions (empty if no one has been found)
   */
  private fun getValidMultiWords(tokens: List<Token>, tokenIndex: Int): List<String> {

    val validMultiWords = mutableListOf<String>()

    var candidates: Set<String> = this.dictionary.getMultiWordsIntroducedBy(tokens[tokenIndex].form).toSet()
    var followingTokenIndex: Int = tokenIndex
    var addingTokens = 0

    while (candidates.size > 1 && ++followingTokenIndex < tokens.size) {

      val token: Token = tokens[followingTokenIndex]

      if (!token.isSpace) {

        val multiWords: List<String> = this.dictionary.getMultiWords(token.form)

        if (multiWords.isEmpty()) break

        candidates = candidates.intersect(multiWords)

        addingTokens++

        candidates
          .filter { it.getNumOfSpaces() == addingTokens } // keep only the candidates that match exactly from
          .forEach { validMultiWords.add(it) }            // tokenIndex until the current index
      }
    }

    return validMultiWords
  }

  /**
   * @return the number of space chars (' ') in this string
   */
  private fun String.getNumOfSpaces(): Int = this.sumBy { if (it == ' ') 1 else 0 }
}