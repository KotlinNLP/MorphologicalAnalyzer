/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.linguisticdescription.language.Language
import com.kotlinnlp.linguisticdescription.language.getLanguageByIso
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.MorphologicalAnalysis
import com.kotlinnlp.morphologicalanalyzer.MorphologicalAnalyzer
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor
import com.kotlinnlp.neuraltokenizer.NeuralTokenizerModel
import java.io.File
import java.io.FileInputStream
import utils.SimpleTokenizer

/**
 * Analyze the morphology of a text.
 *
 * Command line arguments:
 *   1. The iso-a2 code of the language in which to analyze the input
 *   2. The file path of the tokenizer serialized model.
 *   3. The file path of the serialized morphology dictionary.
 */
fun main(args: Array<String>) {

  val language: Language = getLanguageByIso("it")

  val str = """abc 123"""

  val numbersProcessor = NumbersProcessor(language)

  val res = numbersProcessor.findNumbers(str, SimpleTokenizer.tokenize(str))

  println(res)

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

  analysis.tokens.zip(tokens).forEach { (morphoEntries, token) ->
    println("`${token.form}`")
    morphoEntries?.forEach { println("\t$it") } ?: println("\tNo morphology found.")
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

    analysis.multiWords.forEach {
      println("`%s`".format(tokens.subList(it.startToken, it.endToken + 1).joinToString(" ") { it.form }))
      it.morphologies.forEach { println("\t$it") }
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

  if (analysis.dateTimes.isNotEmpty())

    analysis.dateTimes.forEach {
      println("`%s` [%s]".format(
        tokens.subList(it.startToken, it.endToken + 1).joinToString(" ") { it.form },
        it
      ))
    }

  else
    println("No one found.")
}
