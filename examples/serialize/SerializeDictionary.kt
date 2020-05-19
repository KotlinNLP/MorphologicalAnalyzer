/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package serialize

import com.kotlinnlp.linguisticdescription.language.getLanguageByIso
import com.kotlinnlp.morphologicalanalyzer.dictionary.MorphologyDictionary
import com.kotlinnlp.utils.Timer
import com.xenomachina.argparser.mainBody
import java.io.File
import java.io.FileOutputStream

/**
 * Load a [MorphologyDictionary] from a file in JSONL format and serialize it to another file.
 *
 * Launch with the '-h' option for help about the command line arguments.
 */
fun main(args: Array<String>) = mainBody {

  val parsedArgs = CommandLineArguments(args)
  val timer = Timer()

  println("Loading morphology dictionary in JSONL format from '${parsedArgs.inputFilePath}'...")
  val m = MorphologyDictionary.load(
    filename = parsedArgs.inputFilePath,
    language = getLanguageByIso(parsedArgs.language),
    allowDefaultProperties = parsedArgs.defaultProperties)
  println("Elapsed time: %s".format(timer.formatElapsedTime()))

  println("\nNumber of elements in the dictionary: ${m.size} (+ ${m.alternativesCount} references).")

  println("\nSaving serialized dictionary to '${parsedArgs.outputFilePath}'...")
  timer.reset()
  m.dump(FileOutputStream(File(parsedArgs.outputFilePath)))
  println("Elapsed time: %s".format(timer.formatElapsedTime()))
}
