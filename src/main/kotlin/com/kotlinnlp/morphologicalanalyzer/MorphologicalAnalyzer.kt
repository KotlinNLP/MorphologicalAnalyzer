/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer

import com.kotlinnlp.linguisticdescription.morphology.MorphologicalAnalysis
import com.kotlinnlp.linguisticdescription.morphology.Morphologies
import com.kotlinnlp.linguisticdescription.morphology.Morphology
import com.kotlinnlp.morphologicalanalyzer.dictionary.Entry
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary
import com.kotlinnlp.linguisticdescription.morphology.morphologies.discourse.Punctuation
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Noun
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Number as NumberMorpho
import com.kotlinnlp.linguisticdescription.morphology.properties.Number as NumberProp
import com.kotlinnlp.linguisticdescription.morphology.properties.Gender
import com.kotlinnlp.linguisticdescription.sentence.RealSentence
import com.kotlinnlp.linguisticdescription.sentence.properties.MultiWords
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.DateTime
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
import com.kotlinnlp.morphologicalanalyzer.multiwords.MultiWordsHandler
import com.kotlinnlp.morphologicalanalyzer.numbers.Number
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor

/**
 * The morphological analyzer.
 *
 * @param dictionary a morphology dictionary
 * @param processDateTimes whether to process date-times
 * @param processNumbers whether to process numbers
 */
class MorphologicalAnalyzer(
  private val dictionary: MorphologyDictionary,
  processDateTimes: Boolean = true,
  processNumbers: Boolean = true
) {

  companion object {

    /**
     * A regex that matches a punctuation token.
     */
    private val punctRegex = Regex("^[….,;:#!?|/\\\\$%&=~*\\-–_\"“”″‘'`^()<>«»\\[\\]{}]+$")

    /**
     * Build the default morphology for punctuation tokens.
     *
     * @param form the form of the token
     *
     * @return a new punctuation morphology
     */
    private fun buildPunctMorpho(form: String): Morphology = Morphology(Punctuation(lemma = form, oov = true))

    /**
     * Build the default morphology for number tokens.
     *
     * @param lemma the lemma of the number token
     * @param numericForm the numericForm of the number
     *
     * @return a new number morphology
     */
    private fun buildNumberMorpho(lemma: String, numericForm: kotlin.Number): Morphology = Morphology(NumberMorpho(
      lemma = lemma,
      oov = true,
      gender = Gender.Undefined,
      number = when (numericForm.toDouble()) {
        1.0 -> NumberProp.Singular
        else -> NumberProp.Undefined
      },
      numericForm = numericForm))

    /**
     * Build the default morphology for proper nouns tokens.
     *
     * @param form the form of the token
     *
     * @return a new proper noun morphology
     */
    private fun buildProperNounMorpho(form: String): Morphology = Morphology(Noun.Proper.Base(lemma = form, oov = true))
  }

  /**
   * The language of this morphological analyzer.
   * Currently given by the [dictionary].
   */
  private val language = this.dictionary.language

  /**
   * Whether the [NumbersProcessor] is available, or not.
   */
  private val isNumbersProcessorAvailable: Boolean = this.language in NumbersProcessor.AVAILABLE_LANGUAGES

  /**
   * Whether the [DateTimeProcessor] is available, or not.
   */
  private val isDateTimeProcessorAvailable: Boolean = this.language in DateTimeProcessor.AVAILABLE_LANGUAGES

  /**
   * The processor of date-time expressions.
   */
  private val dateTimeProcessor: DateTimeProcessor? = if (processDateTimes && this.isDateTimeProcessorAvailable)
    DateTimeProcessor(this.dictionary.language)
  else
    null

  /**
   * The processor of number expressions.
   */
  private val numbersProcessor: NumbersProcessor? = if (processNumbers && this.isNumbersProcessorAvailable)
    NumbersProcessor(this.dictionary.language)
  else
    null

  /**
   * Perform the morphological analysis of a sentence.
   *
   * @param sentence the sentence
   *
   * @return the morphological analysis of the given sentence
   */
  fun analyze(sentence: RealSentence<RealToken>): MorphologicalAnalysis {

    val numbers: List<Number>
    val dateTimes: List<DateTime>

    sentence.buildText().let { text ->
      numbers = this.findNumbers(text = text, sentence = sentence)
      dateTimes = this.findDateTimes(text = text, sentence = sentence)
    }

    return MorphologicalAnalysis(
      tokensMorphologies = this.buildSingleTokensMorphologies(
        tokens = sentence.tokens,
        singleNumbers = numbers.filterSingleNumbers()),
      multiWords = this.buildMultiWords(
        tokens = sentence.tokens,
        multiWordsNumbers = numbers.filterMultipleNumbers()),
      dateTimes = dateTimes
    )
  }

  /**
   * Find date-time expressions in a sentence.
   *
   * @param sentence the sentence
   *
   * @return the date-time expressions found in the given sentence
   */
  fun findDateTimes(sentence: RealSentence<RealToken>): List<DateTime> =
    this.findDateTimes(text = sentence.buildText(), sentence = sentence)

  /**
   * Find numerical expressions in a sentence.
   *
   * @param sentence the sentence
   *
   * @return the numerical expressions found in the given sentence
   */
  fun findNumbers(sentence: RealSentence<RealToken>): List<Number> =
    this.findNumbers(text = sentence.buildText(), sentence = sentence)

  /**
   * @param tokens the list of input tokens
   * @param singleNumbers the list of single-word numbers
   *
   * @return the list of single tokens morphologies
   */
  private fun buildSingleTokensMorphologies(tokens: List<RealToken>, singleNumbers: List<Number>): List<Morphologies> {

    val numbersByIndex: Map<Int, Number> = mapOf(*singleNumbers.map { it.startToken to it }.toTypedArray())

    return tokens.mapIndexed { i, it ->
      this.getTokenMorphologies(it, numberToken = numbersByIndex[i])
    }
  }

  /**
   * @param tokens the list of input tokens
   * @param multiWordsNumbers the list of multi-words numbers
   *
   * @return the list of multi-words morphologies
   */
  private fun buildMultiWords(tokens: List<RealToken>, multiWordsNumbers: List<Number>): List<MultiWords> =
    MultiWordsHandler(this.dictionary).getMultiWordsMorphologies(tokens) + multiWordsNumbers.toMultiWords()

  /**
   * @param text the text reconstructed from the sentence
   * @param sentence the sentence
   *
   * @return the date-time expressions found in the text
   */
  private fun findDateTimes(text: String, sentence: RealSentence<RealToken>) : List<DateTime> =
    this.dateTimeProcessor
      ?.findDateTimes(text = text, tokens = sentence.tokens, offset = sentence.position.start)
      ?: listOf()

  /**
   * @param text the text reconstructed from the sentence
   * @param sentence the sentence
   *
   * @return the numerical expressions found in the text
   */
  private fun findNumbers(text: String, sentence: RealSentence<RealToken>) : List<Number> =
    this.numbersProcessor
      ?.findNumbers(text = text, tokens = sentence.tokens, offset = sentence.position.start)
      ?: listOf()

  /**
   * Transform this list of numbers in a list of multi-words.
   *
   * @return a list of multi-words
   */
  private fun List<Number>.toMultiWords(): List<MultiWords> = this.map {
    MultiWords(
      startToken = it.startToken,
      endToken = it.endToken,
      morphologies = listOf(buildNumberMorpho(lemma = it.asWord, numericForm = it.value)))
  }

  /**
   * @return the numbers that are composed by multiple tokens
   */
  private fun List<Number>.filterMultipleNumbers() = this.filter { it.startToken != it.endToken }

  /**
   * @return the numbers that are composed by a single token
   */
  private fun List<Number>.filterSingleNumbers() = this.filter { it.startToken == it.endToken }

  /**
   * @param token a token
   * @param numberToken the number token in case the token is part of a numeric expression, otherwise null
   *
   * @return the list of the possible morphologies of the given [token] (can be empty if no one has been found)
   */
  private fun getTokenMorphologies(token: RealToken, numberToken: Number?): Morphologies {

    val dictionaryEntry: Entry? = this.dictionary[token.form]

    val morphologies: MutableList<Morphology> = dictionaryEntry?.morphologies?.toMutableList() ?: mutableListOf()

    when {
      token.isPunct() -> morphologies.add(buildPunctMorpho(token.form))
      numberToken != null ->
        morphologies.add(buildNumberMorpho(lemma = numberToken.asWord, numericForm = numberToken.value))
    }

    if (token.isFirstUpperCase() && morphologies.none { it.components.any { component -> component is Noun.Proper } })
      morphologies.add(buildProperNounMorpho(token.form))

    return Morphologies(morphologies)
  }

  /**
   * @return true if this token contains a punctuation form, otherwise false
   */
  private fun RealToken.isPunct(): Boolean = punctRegex.matches(this.form)

  /**
   * @return true if the first char of this token form is upper case, otherwise false
   */
  private fun RealToken.isFirstUpperCase(): Boolean = this.form.first().isUpperCase()
}
