/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.linguisticdescription.morphology.dictionary.MorphologyDictionary
import com.kotlinnlp.morphologicalanalyzer.MorphologicalAnalysis
import com.kotlinnlp.morphologicalanalyzer.MorphologicalAnalyzer
import com.kotlinnlp.neuraltokenizer.NeuralTokenizer
import com.kotlinnlp.neuraltokenizer.NeuralTokenizerModel
import com.kotlinnlp.neuraltokenizer.Token
import java.io.File
import java.io.FileInputStream

/**
 * Analyze the morphology of a text.
 *
 * Command line arguments:
 *   1. The file path of the tokenizer serialized model.
 *   2. The file path of the serialized morphology dictionary.
 */
fun main(args: Array<String>) {

  require(args.size == 2) { "Required 2 arguments: <tokenizer_model_filename> <morpho_dictionary_filename>." }

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

      val sentences = tokenizer.tokenize(inputText)
      val tokens = sentences.fold(mutableListOf<Token>()) { list, sentence -> list.addAll(sentence.tokens); list }

      val analysis = analyzer.analyze(text = inputText, tokens = tokens)

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

  analysis.tokens.zip(tokens).filterNot { (_, token) -> token.isSpace }.forEach { (morphoEntries, token) ->
    println("`${token.form}`")
    morphoEntries?.forEach { println("\t$it") } ?: println("\tNo morphology found.")
  }

  println("\n*** Multi-words expressions ***\n")

  if (analysis.multiWords.isNotEmpty())
    analysis.multiWords.forEach {
      println("`%s`".format(tokens.subList(it.startToken, it.endToken + 1).joinToString(separator = "") { it.form }))
      it.morphologies.forEach { println("\t$it") }
    }
  else
    println("No one found.")

  println("\n*** Date-time expressions ***\n")

  if (analysis.dateTimes.isNotEmpty())
    analysis.dateTimes.forEach {
      println("`%s`".format(tokens.subList(it.startToken, it.endToken + 1).joinToString(separator = "") { it.form }))
    }
  else
    println("No one found.")
}
