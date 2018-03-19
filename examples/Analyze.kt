/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.linguisticdescription.morphology.dictionary.MorphologyDictionary
import com.kotlinnlp.morphologicalanalyzer.MorphologicalAnalysis
import com.kotlinnlp.morphologicalanalyzer.MorphologicalAnalyzer
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeSimple
import com.kotlinnlp.neuraltokenizer.NeuralTokenizer
import com.kotlinnlp.neuraltokenizer.NeuralTokenizerModel
import com.kotlinnlp.neuraltokenizer.Token
import java.io.File
import java.io.FileInputStream

/**
 * Analyze the morphology of a text.
 *
 * Command line arguments:
 *   1. The iso-a2 code of the language in which to analyze the input
 *   2. The file path of the tokenizer serialized model.
 *   3. The file path of the serialized morphology dictionary.
 */
fun main(args: Array<String>) {

  require(args.size == 3) {
    "Required 3 arguments: <lang_code> <tokenizer_model_filename> <morpho_dictionary_filename>."
  }

  val langCode: String = args[0]

  val tokenizer: NeuralTokenizer = args[1].let {
    println("Loading tokenizer model from '$it'...")
    NeuralTokenizer(NeuralTokenizerModel.load(FileInputStream(File(it))))
  }

  val analyzer: MorphologicalAnalyzer = args[2].let {
    println("Loading serialized dictionary from '$it'...")
    MorphologicalAnalyzer(dictionary = MorphologyDictionary.load(FileInputStream(File(it))))
  }

  while (true) {

    val inputText = readValue()

    if (inputText.isEmpty()) {
      break

    } else {

      val sentences = tokenizer.tokenize(inputText)
      val tokens = sentences.fold(mutableListOf<Token>()) { list, sentence -> list.addAll(sentence.tokens); list }

      val analysis = analyzer.analyze(text = inputText, tokens = tokens, langCode = langCode)

      printAnalysis(tokens = tokens, analysis = analysis)
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
private fun printAnalysis(tokens: List<Token>, analysis: MorphologicalAnalysis) {

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
private fun printTokens(tokens: List<Token>, analysis: MorphologicalAnalysis) {

  analysis.tokens.zip(tokens).filterNot { (_, token) -> token.isSpace }.forEach { (morphoEntries, token) ->
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
private fun printMultiWords(tokens: List<Token>, analysis: MorphologicalAnalysis) {

  if (analysis.multiWords.isNotEmpty())

    analysis.multiWords.forEach {
      println("`%s`".format(tokens.subList(it.startToken, it.endToken + 1).joinToString(separator = "") { it.form }))
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
private fun printDateTimes(tokens: List<Token>, analysis: MorphologicalAnalysis) {

  if (analysis.dateTimes.isNotEmpty())

    analysis.dateTimes.forEach {
      if (it is DateTimeSimple) {
        println("`%s` [%s]".format(
          tokens.subList(it.startToken, it.endToken + 1).joinToString(separator = "") { it.form },
          it
        ))
      }
    }

  else
    println("No one found.")
}
