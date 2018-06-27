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
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Number as NumberMorpho
import com.kotlinnlp.linguisticdescription.morphology.properties.Number as NumberEnum
import com.kotlinnlp.linguisticdescription.morphology.properties.Gender
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTime
import com.kotlinnlp.morphologicalanalyzer.multiwords.MultiWordsHandler
import com.kotlinnlp.morphologicalanalyzer.numbers.Number
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor

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
     * @param form the form of the token
     *
     * @return a morphology entries list
     */
    private fun buildPunctMorpho(form: String): List<MorphologyEntry> = listOf(
      MorphologyEntry(type = MorphologyEntry.Type.Single, list = listOf(Punctuation(lemma = form)))
    )

    /**
     * Build the default morphology entries list for number tokens (it contains only one single morphology).
     *
     * @param lemma the lemma of the number token
     * @param numericForm the numericForm of the number
     *
     * @return a morphology entries list
     */
    private fun buildNumberMorpho(lemma: String, numericForm: kotlin.Number): List<MorphologyEntry> = listOf(
      MorphologyEntry(
        type = MorphologyEntry.Type.Single,
        list = listOf(NumberMorpho(
          lemma = lemma,
          gender = Gender.Undefined,
          number = NumberEnum.Undefined,
          numericForm = numericForm))
      )
    )
  }

  /**
   * A map of [DateTimeProcessor]s associated by language code.
   */
  private val dateTimeProcessors = mutableMapOf<String, DateTimeProcessor>()

  /**
   * A map of [NumbersProcessor]s associated by language code.
   */
  private val numbersProcessors = mutableMapOf<String, NumbersProcessor>()

  /**
   * Analyze the morphology of a text, given as a list of tokens.
   *
   * @param text the input text
   * @param tokens a list of tokens that compose the [text]
   * @param langCode the iso-a2 code of the language in which to analyze the text
   *
   * @return the morphological analysis of the given text
   */
  fun analyze(text: String, tokens: List<RealToken>, langCode: String): MorphologicalAnalysis {

    require(langCode.length == 2) { "The language code must have length 2." }

    val dateTimeProcessor = this.dateTimeProcessors.getOrPut(langCode, defaultValue = { DateTimeProcessor(langCode) })
    val numbersProcessor = this.numbersProcessors.getOrPut(langCode, defaultValue = { NumbersProcessor(langCode) })

    val dateTimes: List<DateTime> = dateTimeProcessor.findDateTimes(text = text, tokens = tokens)
    val numbers: List<Number> = numbersProcessor.findNumbers(text = text, tokens = tokens)
    val numbersByIndex: Map<Int, Number> = mapOf(
      *numbers.flatMap { (it.startToken..it.endToken).map { i -> Pair(i, it) } }.toTypedArray()
    )

    return MorphologicalAnalysis(
      tokens = tokens.mapIndexed { i, it -> this.getTokenMorphology(it, numberToken = numbersByIndex[i]) },
      multiWords = MultiWordsHandler(this.dictionary).getMultiWordMorphologies(tokens),
      dateTimes = dateTimes
    )
  }

  /**
   * @param token a token
   * @param numberToken the number token in case the token is part of a numeric expression, otherwise null
   *
   * @return the list of morphology entries of the given [token] or null if no one has been found
   */
  private fun getTokenMorphology(token: RealToken, numberToken: Number?): List<MorphologyEntry>? {

    val dictionaryEntry: Entry? = this.dictionary[token.form]

    return when {

      token.isPunct() -> buildPunctMorpho(token.form) + (dictionaryEntry?.morphologies ?: listOf())

      numberToken != null -> buildNumberMorpho(
        lemma = numberToken.asWord,
        numericForm = numberToken.value
      ) + (dictionaryEntry?.morphologies ?: listOf())

      else -> dictionaryEntry?.morphologies
    }
  }

  /**
   * @return a boolean indicating if this token contains a punctuation form
   */
  private fun RealToken.isPunct(): Boolean = punctRegex.matches(this.form)
}
