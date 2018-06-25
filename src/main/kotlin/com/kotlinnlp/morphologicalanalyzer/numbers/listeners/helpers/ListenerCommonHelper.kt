/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.listeners.helpers

import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParams
import com.kotlinnlp.morphologicalanalyzer.numbers.Number
import org.antlr.v4.runtime.tree.ParseTree

/**
 * Helper for the ANTLR listeners.
 *
 * @param langParams language-specific parameters
 */
internal class ListenerCommonHelper(langParams: LanguageParams) {

  /**
   * The digit to word converter for the given language.
   */
  val digitToWordConverter = DigitToWordConverter(langParams)

  /**
   * Maps the ANTLR nodes to their annotations.
   */
  val treeValues = mutableMapOf<ParseTree, Pair<String, String>>()

  /**
   * The list of numbers recognised in the input text.
   */
  val numbers = mutableListOf<Number>()

  /**
   * Regex to recognize zeroes in non-significant decimal trailing position.
   */
  val trailingZeroRegex = Regex("""(\d+%s(?:\d*[1-9])?)0*""".format(Regex.escape(langParams.digitDecimalSeparator)))

  /**
   * Regex to recognize zeroes in non-significant leading position.
   */
  val leadingZeroesRegex = Regex("""^0+([1-9]|0$|0%s)""".format(Regex.escape(langParams.digitDecimalSeparator)))

  /**
   * Regex to split a string in sequences of non-whitespace characters.
   */
  val spaceSplitterRegex = Regex("""(?:^|\s+)(\S+)""")

  /**
   * Regex to recognize a sequence of whitespace characters.
   */
  val whiteSpacesRegex = Regex("""\s""")

  /**
   * Regex to recognize a digit in the range from 2 to 9.
   */
  val number2to9Regex = Regex("""[2-9]""")

  /**
   * Regex to recognize a digit, optionally followed by a second one.
   */
  val oneOrTwoDigitsRegex = Regex("""\d\d?""")

  /**
   * Regex to recognize one or more spaces.
   */
  val spacesRegex = Regex("""\s+""")
}
