/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer

import com.kotlinnlp.morphologicalanalyzer.dictionary.Entry
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyEntry
import com.kotlinnlp.linguisticdescription.morphology.morphologies.discourse.Punctuation
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Number
import com.kotlinnlp.linguisticdescription.morphology.properties.Number as NumberEnum
import com.kotlinnlp.linguisticdescription.morphology.properties.Gender
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
import com.kotlinnlp.morphologicalanalyzer.multiwords.MultiWordsHandler
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor
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

    /**
     * Build the default morphology entries list for number tokens (it contains only one single morphology).
     *
     * @return a morphology entries list
     */
    private fun buildNumberMorpho(form: String): List<MorphologyEntry> = listOf(
      MorphologyEntry(
        type = MorphologyEntry.Type.Single,
        list = listOf(Number(lemma = form, gender = Gender.Masculine, number = NumberEnum.Singular)))
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
      multiWords = MultiWordsHandler(this.dictionary).getMultiWordMorphologies(tokens),
      dateTimes = DateTimeProcessor.findDateTimes(text = text, tokens = tokens, langCode = langCode),
      numbers = NumbersProcessor(langCode).findNumbers(text = text, tokens = tokens)
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
      token.isNumber() -> buildNumberMorpho(token.form) + (dictionaryEntry?.morphologies ?: listOf())
      dictionaryEntry != null -> dictionaryEntry.morphologies
      else -> null
    }
  }

  /**
   * @return a boolean indicating if this token contains a punctuation form
   */
  private fun Token.isPunct(): Boolean = punctRegex.matches(this.form)

  /**
   * TODO: fix
   *
   * @return a boolean indicating if this token contains a numeric form
   */
  private fun Token.isNumber(): Boolean =
    this.form.toDoubleOrNull() != null ||
      this.form.replace(",", ".").toDoubleOrNull() != null ||
      this.form.replace(".", "").toDoubleOrNull() != null ||
      this.form.replace(".", "").replace(",", ".").toDoubleOrNull() != null
}
