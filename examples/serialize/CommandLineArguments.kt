/* Copyright 2017-present KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * -----------------------------------------------------------------------------*/

package serialize

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.default

/**
 * The interpreter of command line arguments.
 *
 * @param args the array of command line arguments
 */
internal class CommandLineArguments(args: Array<String>) {

  /**
   * The parser of the string arguments.
   */
  private val parser = ArgParser(args)

  /**
   * The path of the input file from which to read the dictionary in JSONL format.
   */
  val inputFilePath: String by parser.storing(
    "-i",
    "--input",
    help="the path of the input file from which to read the dictionary in JSONL format"
  )

  /**
   * The path of the output file into which to write the serialized dictionary.
   */
  val outputFilePath: String by parser.storing(
    "-o",
    "--output",
    help="the path of the output file into which to write the serialized dictionary"
  )

  /**
   * The ISO 639-1 code of the dictionary language (default Unknown).
   */
  val language: String by parser.storing(
    "-l",
    "--language",
    help="the ISO 639-1 code of the dictionary language (default Unknown)"
  ).default { "--" }

  /**
   * Allow to assign default values to grammatical properties that are not specified.
   */
  val defaultProperties: Boolean by parser.flagging(
    "-d",
    "--default-properties",
    help="allow to assign default values to grammatical properties that are not specified"
  )

  /**
   * Force parsing all arguments (only read ones are parsed by default).
   */
  init {
    parser.force()
  }
}
