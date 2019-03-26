/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.linguisticdescription.morphology.MorphologicalAnalysis
import com.kotlinnlp.linguisticdescription.sentence.RealSentence
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.SingleDateTime
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.intervals.Interval
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.intervals.LowerLimitedInterval
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.intervals.UpperLimitedInterval
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary
import com.kotlinnlp.morphologicalanalyzer.MorphologicalAnalyzer
import com.kotlinnlp.neuraltokenizer.NeuralTokenizer
import com.kotlinnlp.neuraltokenizer.NeuralTokenizerModel
import java.io.File
import java.io.FileInputStream
import java.lang.RuntimeException
import java.time.LocalDateTime

/**
 * Analyze the morphology of a text.
 *
 * Command line arguments:
 *   1. The file path of the tokenizer serialized model.
 *   2. The file path of the serialized morphology dictionary.
 */
fun main(args: Array<String>) {

  require(args.size == 2) {
    "Required 2 arguments: <tokenizer_model_filename> <morpho_dictionary_filename>."
  }

  val tokenizer: NeuralTokenizer = args[0].let {
    println("Loading tokenizer model from '$it'...")
    NeuralTokenizer(NeuralTokenizerModel.load(FileInputStream(File(it))))
  }

  val analyzer: MorphologicalAnalyzer = args[1].let {
    println("Loading serialized dictionary from '$it'...")
    MorphologicalAnalyzer(dictionary = MorphologyDictionary.load(FileInputStream(File(it))))
  }

  while (true) {

    val inputText = readValue()

    if (inputText.isEmpty()) {
      break

    } else {

      tokenizer.tokenize(inputText).forEach { sentence ->

        @Suppress("UNCHECKED_CAST")
        val analysis = analyzer.analyze(sentence = sentence as RealSentence<RealToken>)

        printAnalysis(tokens = sentence.tokens, analysis = analysis)
      }
    }
  }

  println("\nExiting...")
}

/**
 * Read a value from the standard input.
 *
 * @return the string read
 */
private fun readValue(): String {

  print("\nAnalyze a text (empty to exit): ")

  return readLine()!!
}

/**
 * Print a given morphological analysis.
 *
 * @param tokens the list of tokens
 * @param analysis the morphological analysis of the [tokens]
 */
private fun printAnalysis(tokens: List<RealToken>, analysis: MorphologicalAnalysis) {

  println("\n*** Tokens ***\n")
  printTokens(tokens = tokens, analysis = analysis)

  println("\n*** Multi-words expressions ***\n")
  printMultiWords(tokens = tokens, analysis = analysis)

  println("\n*** Date-time expressions ***\n")
  printDateTimes(tokens = tokens, analysis = analysis)
}

/**
 * Print the tokens morphologies.
 *
 * @param tokens the list of tokens
 * @param analysis the morphological analysis of the [tokens]
 */
private fun printTokens(tokens: List<RealToken>, analysis: MorphologicalAnalysis) {

  analysis.tokensMorphologies.zip(tokens).forEach { (morphoEntries, token) ->
    println("`${token.form}`")
    morphoEntries.forEach { println("\t$it") }
  }
}

/**
 * Print the multi-words morphologies.
 *
 * @param tokens the list of tokens
 * @param analysis the morphological analysis of the [tokens]
 */
private fun printMultiWords(tokens: List<RealToken>, analysis: MorphologicalAnalysis) {

  if (analysis.multiWords.isNotEmpty())

    analysis.multiWords.forEach { mw ->
      println("`%s`".format(tokens.subList(mw.startToken, mw.endToken + 1).joinToString(" ") { it.form }))
      mw.morphologies.forEach { println("\t$it") }
    }

  else
    println("No one found.")
}

/**
 * Print the date-times.
 *
 * @param tokens the list of tokens
 * @param analysis the morphological analysis of the [tokens]
 */
private fun printDateTimes(tokens: List<RealToken>, analysis: MorphologicalAnalysis) {

  if (analysis.dateTimes.isNotEmpty()) {

    val ref: LocalDateTime = LocalDateTime.now()

    analysis.dateTimes.forEach { dateTime ->
      println("`%s` [%s] (%s)".format(
        tokens.subList(dateTime.startToken, dateTime.endToken + 1).joinToString(" ") { it.form },
        dateTime,
        when (dateTime) {
          is SingleDateTime -> dateTime.isoFormat(ref)
          is Interval -> "%s - %s".format(
            if (dateTime is LowerLimitedInterval) dateTime.from.isoFormat(ref) else "/",
            if (dateTime is UpperLimitedInterval) dateTime.to.isoFormat(ref) else "/"
          )
          else -> throw RuntimeException("Invalid date-time object.")
        }
      ))
    }

  } else {
    println("No one found.")
  }
}
