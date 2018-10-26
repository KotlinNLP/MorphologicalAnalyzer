/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer

import com.kotlinnlp.linguisticdescription.language.Language
import com.kotlinnlp.linguisticdescription.morphology.Morphology
import com.kotlinnlp.morphologicalanalyzer.dictionary.Entry
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary
import com.kotlinnlp.linguisticdescription.morphology.morphologies.discourse.Punctuation
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Noun
import com.kotlinnlp.linguisticdescription.morphology.morphologies.things.Number as NumberMorpho
import com.kotlinnlp.linguisticdescription.morphology.properties.Number as NumberProp
import com.kotlinnlp.linguisticdescription.morphology.properties.Gender
import com.kotlinnlp.linguisticdescription.sentence.Sentence
import com.kotlinnlp.linguisticdescription.sentence.properties.MultiWords
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
import com.kotlinnlp.morphologicalanalyzer.multiwords.MultiWordsHandler
import com.kotlinnlp.morphologicalanalyzer.numbers.Number
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor

/**
 * The morphological analyzer.
 *
 * @property language the language in which to analyze the text
 * @param dictionary a morphology dictionary
 */
class MorphologicalAnalyzer(val language: Language, private val dictionary: MorphologyDictionary) {

  companion object {

    /**
     * A regex that matches a punctuation token.
     */
    private val punctRegex = Regex("^[.,;:#!?|/\\\\$%&=~*\\-–_\"“”‘'`^()\\[\\]{}]+$")

    /**
     * Build the default morphology for punctuation tokens.
     *
     * @param form the form of the token
     *
     * @return a new punctuation morphology
     */
    private fun buildPunctMorpho(form: String): Morphology = Morphology(Punctuation(lemma = form))

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
    private fun buildProperNounMorpho(form: String): Morphology = Morphology(Noun.Proper.Base(lemma = form))
  }

  /**
   * The processor of date-time expressions.
   */
  private val dateTimeProcessor = DateTimeProcessor(this.language)

  /**
   * The processor of number expressions.
   */
  private val numbersProcessor = NumbersProcessor(this.language)

  /**
   * Perform the morphological analysis of a sentence.
   *
   * @param sentence the sentence
   *
   * @return the morphological analysis of the given sentence
   */
  fun analyze(sentence: Sentence<RealToken>): MorphologicalAnalysis {

    val text = sentence.buildText()
    val numbers: List<Number> = this.numbersProcessor.findNumbers(text = text, tokens = sentence.tokens)
    val oneTokenNumbers: List<Number> = numbers.filter { it.startToken == it.endToken }
    val multiWordsNumbers: List<Number> = numbers.filter { it.startToken != it.endToken }
    val numbersByIndex: Map<Int, Number> = mapOf(*oneTokenNumbers.map { it.startToken to it }.toTypedArray())

    return MorphologicalAnalysis(
      tokens = sentence.tokens.mapIndexed { i, it -> this.getTokenMorphologies(it, numberToken = numbersByIndex[i]) },
      multiWords = this.buildMultiWords(tokens = sentence.tokens, multiWordsNumbers = multiWordsNumbers),
      dateTimes = this.dateTimeProcessor.findDateTimes(text = text, tokens = sentence.tokens)
    )
  }

  /**
   * @param token a token
   * @param numberToken the number token in case the token is part of a numeric expression, otherwise null
   *
   * @return the list of the possible morphologies of the given [token] or null if no one has been found
   */
  private fun getTokenMorphologies(token: RealToken, numberToken: Number?): List<Morphology>? {

    val dictionaryEntry: Entry? = this.dictionary[token.form]

    val morphologies: MutableList<Morphology> = dictionaryEntry?.morphologies?.toMutableList() ?: mutableListOf()

    when {
      token.isPunct() -> morphologies.add(buildPunctMorpho(token.form))
      numberToken != null ->
        morphologies.add(buildNumberMorpho(lemma = numberToken.asWord, numericForm = numberToken.value))
    }

    if (token.isTitleCase() && morphologies.none { it.components.any { component -> component is Noun } })
      morphologies.add(buildProperNounMorpho(token.form))

    return if (morphologies.isNotEmpty()) morphologies else null
  }

  /**
   * @return true if this token contains a punctuation form, otherwise false
   */
  private fun RealToken.isPunct(): Boolean = punctRegex.matches(this.form)

  /**
   * @return true if this token form is title case (only first letter upper case), otherwise false
   */
  private fun RealToken.isTitleCase(): Boolean = this.form.let {
    it.first().isUpperCase() && (it.length == 1 || it.subSequence(1, it.length).all { char -> char.isLowerCase() })
  }

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
        morphologies = listOf(buildNumberMorpho(lemma = it.asWord, numericForm = it.value)))
    }

    return MultiWordsHandler(this.dictionary).getMultiWordsMorphologies(tokens) + multiWordsFromNumbers
  }
}
