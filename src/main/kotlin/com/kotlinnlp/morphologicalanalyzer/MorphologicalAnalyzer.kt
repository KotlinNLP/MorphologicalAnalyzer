/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer

import com.kotlinnlp.linguisticdescription.morphology.dictionary.MorphologyDictionary
import com.kotlinnlp.neuraltokenizer.Token

/**
 * The morphological analyzer.
 *
 * @param dictionary a morphology dictionary
 */
class MorphologicalAnalyzer(private val dictionary: MorphologyDictionary) {

  /**
   * Analyze the morphology of a text, given as a list of tokens.
   *
   * @param tokens a list of tokens
   *
   * @return the morphological analysis of the given text
   */
  fun analyze(tokens: List<Token>) = MorphologicalAnalysis(
    tokens = tokens.map { this.dictionary[it.form]?.morphologies },
    multiWords = this.getMultiWordMorphologies(tokens)
  )

  /**
   * @param tokens a list of tokens
   *
   * @return the list of morphologies of the multi-words recognized in the given list of [tokens]
   */
  private fun getMultiWordMorphologies(tokens: List<Token>): List<MultiWordsMorphology> {

    val forms: List<String> = tokens.map { it.form }
    val morphologies = mutableListOf<MultiWordsMorphology>()

    (0 until tokens.size).forEach { tokenIndex ->

      this.getValidMultiWords(forms = forms, tokenIndex = tokenIndex).forEach { multiWord ->
        morphologies.add(
          MultiWordsMorphology(
            startToken = tokenIndex,
            endToken = tokenIndex + multiWord.getNumOfSpaces(), // the number of adding tokens
            morphologies = this.dictionary[multiWord]!!.morphologies)
        )
      }
    }

    return morphologies
  }

  /**
   * Get the list of multi-words expressions as sub-list of the given [forms], that start with the token at a given
   * index.
   *
   * @param forms the list of forms, one per input token
   * @param tokenIndex the index of the currently focused token (within the [forms] list)
   *
   * @return a list of multi-words expressions (empty if no one has been found)
   */
  private fun getValidMultiWords(forms: List<String>, tokenIndex: Int): List<String> {

    val validMultiWords = mutableListOf<String>()

    var candidates: Set<String> = this.dictionary.getMultiWordsIntroducedBy(forms[tokenIndex]).toSet()
    var distance = 0

    while (candidates.size > 1 && tokenIndex + ++distance < forms.size) {

      val multiWords: List<String> = this.dictionary.getMultiWords(forms[tokenIndex + distance])

      if (multiWords.isEmpty()) break

      candidates = candidates.intersect(multiWords)

      candidates
        .filter { it.getNumOfSpaces() == distance } // it takes only the candidates that match exactly from tokenIndex
        .forEach { validMultiWords.add(it) }        // until the current index
    }

    return validMultiWords
  }

  /**
   * @return the number of space chars (' ') in this string
   */
  private fun String.getNumOfSpaces(): Int = this.sumBy { if (it == ' ') 1 else 0 }
}
