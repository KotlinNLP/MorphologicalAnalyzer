/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer

import com.kotlinnlp.linguisticdescription.morphology.dictionary.MorphologyDictionary
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
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
   * @param text the input text
   * @param tokens a list of tokens that compose the [text]
   * @param langCode the iso-a2 code of the language in which to analyze the text
   *
   * @return the morphological analysis of the given text
   */
  fun analyze(text: String, tokens: List<Token>, langCode: String): MorphologicalAnalysis {

    require(langCode.length == 2) { "The language code must have length 2." }

    return MorphologicalAnalysis(
      tokens = tokens.map { if (it.isSpace) null else this.dictionary[it.form]?.morphologies },
      multiWords = this.getMultiWordMorphologies(tokens),
      dateTimes = DateTimeProcessor.getDateTimes(text = text, tokens = tokens, langCode = langCode)
    )
  }

  /**
   * @param tokens a list of input tokens
   *
   * @return the list of morphologies of the multi-words recognized in the given list of [tokens]
   */
  private fun getMultiWordMorphologies(tokens: List<Token>): List<MultiWordsMorphology> {

    val morphologies = mutableListOf<MultiWordsMorphology>()

    tokens.forEachIndexed { tokenIndex, token ->

      if (!token.isSpace) {
        this.getValidMultiWords(tokens = tokens, tokenIndex = tokenIndex).forEach { multiWord ->
          morphologies.add(
            MultiWordsMorphology(
              startToken = tokenIndex,
              endToken = tokenIndex + 2 * multiWord.getNumOfSpaces(), // the number of adding tokens
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
    var distance = 0

    while (candidates.size > 1 && tokenIndex + ++distance < tokens.size) {

      val token: Token = tokens[tokenIndex + distance]

      if (!token.isSpace) {

        val multiWords: List<String> = this.dictionary.getMultiWords(token.form)

        if (multiWords.isEmpty()) break

        candidates = candidates.intersect(multiWords)

        candidates
          .filter { 2 * it.getNumOfSpaces() == distance } // keep only the candidates that match exactly from
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
