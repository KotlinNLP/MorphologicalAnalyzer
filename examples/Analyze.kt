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

    val searchVal = readValue()

    if (searchVal.isEmpty()) {
      break

    } else {

      val sentences = tokenizer.tokenize(searchVal)
      val tokens = sentences.fold(mutableListOf<Token>()) { list, sentence -> list.addAll(sentence.tokens); list }
      val notSpaceTokens = tokens.filterNot { it.isSpace }

      val analysis = analyzer.analyze(notSpaceTokens)

      printAnalysis(forms = notSpaceTokens.map { it.form }, analysis = analysis)
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
 * @param forms the list of tokens forms
 * @param analysis a morphological analysis
 */
private fun printAnalysis(forms: List<String>, analysis: MorphologicalAnalysis) {

  println("\n*** Tokens ***\n")

  analysis.tokens.zip(forms).forEach { (entries, form) ->
    println("'$form'")
    entries?.forEach { println("\t$it") } ?: println("\tNo form found.")
  }

  println("\n*** Multi-words expressions ***\n")

  if (analysis.multiWords.isNotEmpty())
    analysis.multiWords.forEach {
      println("'%s'".format(forms.subList(it.startToken, it.endToken + 1).joinToString(separator = " ")))
      it.morphologies.forEach { println("\t$it") }
    }
  else
    println("No one found.")
}
