/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer

import com.kotlinnlp.linguisticdescription.morphology.dictionary.Entry
import com.kotlinnlp.linguisticdescription.morphology.dictionary.MorphologyDictionary
import com.kotlinnlp.linguisticdescription.morphology.dictionary.MorphologyEntry
import com.kotlinnlp.linguisticdescription.morphology.morphologies.discourse.Punctuation
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
import com.kotlinnlp.neuraltokenizer.Token

/**
 * The morphological analyzer.
 *
 * @param dictionary a morphology dictionary
 */
class MorphologicalAnalyzer(private val dictionary: MorphologyDictionary) {

  companion object {

    /**
     * A regex that matches a punctuation token.
     */
    private val punctRegex = Regex("^[.,;:#!?|/\\\\$%&=~*\\-_\"“”‘'`^()\\[\\]{}]+$")

    /**
     * Build the default morphology entries list for punctuation tokens (it contains only one single morphology).
     *
     * @return a morphology entries list
     */
    private fun buildPunctMorpho(form: String): List<MorphologyEntry> = listOf(
      MorphologyEntry(type = MorphologyEntry.Type.Single, list = listOf(Punctuation(lemma = form)))
    )
  }

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
      tokens = tokens.map { this.getTokenMorphology(it) },
      multiWords = this.getMultiWordMorphologies(tokens),
      dateTimes = DateTimeProcessor.getDateTimes(text = text, tokens = tokens, langCode = langCode)
    )
  }

  /**
   * @param token a token
   *
   * @return the list of morphology entries of the given [token] or null if no one has been found
   */
  private fun getTokenMorphology(token: Token): List<MorphologyEntry>? {

    val dictionaryEntry: Entry? = this.dictionary[token.form]

    return when {
      token.isSpace -> null
      token.isPunct() -> buildPunctMorpho(token.form) + (dictionaryEntry?.morphologies ?: listOf())
      dictionaryEntry != null -> dictionaryEntry.morphologies
      else -> null
    }
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
   * @return a boolean indicating if this token contains a punctuation form
   */
  private fun Token.isPunct(): Boolean = punctRegex.matches(this.form)

  /**
   * @return the number of space chars (' ') in this string
   */
  private fun String.getNumOfSpaces(): Int = this.sumBy { if (it == ' ') 1 else 0 }
}
