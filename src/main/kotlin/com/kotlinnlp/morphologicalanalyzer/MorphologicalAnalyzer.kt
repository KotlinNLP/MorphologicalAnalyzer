/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer

import com.kotlinnlp.linguisticdescription.morphology.Morphology
import com.kotlinnlp.morphologicalanalyzer.dictionary.Entry
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary
import com.kotlinnlp.linguisticdescription.morphology.morphologies.discourse.Punctuation
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Number as NumberMorpho
import com.kotlinnlp.linguisticdescription.morphology.properties.Number as NumberEnum
import com.kotlinnlp.linguisticdescription.morphology.properties.Gender
import com.kotlinnlp.linguisticdescription.sentence.RealSentence
import com.kotlinnlp.linguisticdescription.sentence.properties.MultiWords
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
import com.kotlinnlp.morphologicalanalyzer.multiwords.MultiWordsHandler
import com.kotlinnlp.morphologicalanalyzer.numbers.Number
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor

/**
 * The morphological analyzer.
 *
 * @property langCode ISO 639-2 code of the language in which to analyze the text
 * @param dictionary a morphology dictionary
 */
class MorphologicalAnalyzer(val langCode: String, private val dictionary: MorphologyDictionary) {

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
    private fun buildPunctMorpho(form: String): List<Morphology> = listOf(
      Morphology(type = Morphology.Type.Single, list = listOf(Punctuation(lemma = form)))
    )

    /**
     * Build the default morphology entries list for number tokens (it contains only one single morphology).
     *
     * @param lemma the lemma of the number token
     * @param numericForm the numericForm of the number
     *
     * @return a morphology entries list
     */
    private fun buildNumberMorpho(lemma: String, numericForm: kotlin.Number): List<Morphology> = listOf(
      Morphology(
        type = Morphology.Type.Single,
        list = listOf(NumberMorpho(
          lemma = lemma,
          gender = Gender.Undefined,
          number = when (numericForm.toDouble()) {
            1.0 -> NumberEnum.Singular
            else -> NumberEnum.Undefined
          },
          numericForm = numericForm))
      )
    )
  }

  /**
   * The processor of date-time expressions.
   */
  private val dateTimeProcessor = DateTimeProcessor(this.langCode)

  /**
   * The processor of number expressions.
   */
  private val numbersProcessor = NumbersProcessor(this.langCode)

  /**
   * Check language ISO code.
   */
  init {
    require(langCode.length == 2) { "The language code must have length 2." }
  }

  /**
   * Perform the morphological analysis of a sentence.
   *
   * @param sentence the sentence
   *
   * @return the morphological analysis of the given sentence
   */
  fun analyze(sentence: RealSentence<RealToken>): MorphologicalAnalysis {

    val text = sentence.buildText()
    val numbers: List<Number> = this.numbersProcessor.findNumbers(text = text, tokens = sentence.tokens)
    val oneTokenNumbers: List<Number> = numbers.filter { it.startToken == it.endToken }
    val multiWordsNumbers: List<Number> = numbers.filter { it.startToken != it.endToken }
    val numbersByIndex: Map<Int, Number> = mapOf(*oneTokenNumbers.map { it.startToken to it }.toTypedArray())

    return MorphologicalAnalysis(
      tokens = sentence.tokens.mapIndexed { i, it -> this.getTokenMorphology(it, numberToken = numbersByIndex[i]) },
      multiWords = this.buildMultiWords(tokens = sentence.tokens, multiWordsNumbers = multiWordsNumbers),
      dateTimes = this.dateTimeProcessor.findDateTimes(text = text, tokens = sentence.tokens)
    )
  }

  /**
   * @param token a token
   * @param numberToken the number token in case the token is part of a numeric expression, otherwise null
   *
   * @return the list of morphology entries of the given [token] or null if no one has been found
   */
  private fun getTokenMorphology(token: RealToken, numberToken: Number?): List<Morphology>? {

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

  /**
   * @param tokens the list of input tokens
   * @param multiWordsNumbers the list of multi-words numbers
   *
   * @return the list of multi-words morphologies
   */
  private fun buildMultiWords(tokens: List<RealToken>, multiWordsNumbers: List<Number>): List<MultiWords> {

    val multiWordsFromNumbers: List<MultiWords> = multiWordsNumbers.map {
      MultiWords(
        startToken = it.startToken,
        endToken = it.endToken,
        morphologies = buildNumberMorpho(lemma = it.asWord, numericForm = it.value))
    }

    return MultiWordsHandler(this.dictionary).getMultiWordsMorphologies(tokens) + multiWordsFromNumbers
  }
}
