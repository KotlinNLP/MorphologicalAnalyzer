/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.listeners.helpers

import com.kotlinnlp.linguisticdescription.Language
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParams
import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParamsFactory

/**
 * Convert digit numbers to their word representation in the current language.
 *
 * @param langParams contains the language-specific parameters
 * @param debug if is true enable printing debug messages on stderr
 */
internal class DigitToWordConverter(private val langParams: LanguageParams, private val debug: Boolean = false) {

  companion object {

    /**
     * Concatenate two strings (eventually null or empty) using a given separator when both are not empty.
     *
     * @param a the first string to concatenate
     * @param sep the separator to add
     * @param b the second string to concatenate
     *
     * @return the concatenated string
     */
    private fun concat(a: String?, sep: String, b: String?): String {

      var ret = a ?: ""

      if (b != null && b.isNotEmpty()) {

        if (ret.isNotEmpty()) ret += sep

        ret += b
      }

      return ret
    }
  }

  /**
   * Constructor to be used when passing the language as a string containing a two-letters ISO code.
   *
   * @param language the language of the text to process
   * @param debug if true enable debug messages on stderr
   */
  constructor(language: Language, debug: Boolean = false): this(
    langParams = LanguageParamsFactory.factory(language),
    debug = debug)

  /**
   * Regex pattern that is used to verify that the arguments are valid numbers and to split the
   * integer and decimal components.
   */
  private val numberPattern = Regex("""^(\d+)(?:%s(\d*[1-9])?0*)?$"""
    .format(Regex.escape(this.langParams.digitDecimalSeparator)))

  /**
   * The word to be used as the decimal separator.
   */
  private val wordDecSep = this.langParams.wordDecimalSeparator

  /**
   * Contains a space if it is required in the current language to separate the words that
   * forms a number with spaces.
   */
  private val spaceBtwWords = if (this.langParams.generatorFlags.spaceBetweenWords) " " else ""

  /**
   * Helper that provides methods for language specific special cases.
   */
  private val specialCasesHelper = DigitToWordSpecialCases(this.langParams)

  /**
   * Convert a number to a word representation.
   *
   * @param number the number to convert
   *
   * @return the word representation
   */
  fun convert(number: Long): String = this.numToWord(number.toString())

  /**
   * Convert a number to a word representation.
   *
   * @param number the number to convert
   *
   * @return the word representation
   */
  fun convert(number: Int): String = this.numToWord(number.toString())

  /**
   * Convert a number to a word representation.
   *
   * @param number the number to convert
   *
   * @return the word representation
   */
  fun convert(number: Double): String {

    val n = number.toString().split(".")

    return convert(integer = n[0], decimal = n[1])
  }

  /**
   * Convert a number to a word representation.
   *
   * @param number the number to convert
   *
   * @return the word representation
   */
  fun convert(number: Float): String {

    val n = number.toString().split(".")

    return convert(integer = n[0], decimal = n[1])
  }

  /**
   * Convert a number to a word representation.
   *
   * @param number the number to convert
   *
   * @return the word representation
   */
  fun convert(number: String): String {

    val matches: MatchResult = this.numberPattern.matchEntire(number) ?:
    throw IllegalArgumentException("Argument must be a valid number, given: '$number'")

    return convert(matches.groups[1]!!.value, matches.groups[2]?.value)
  }

  /**
   * Convert a number to a word representation.
   *
   * @param integer the integer portion of the number to be converted
   * @param decimal the optional decimal portion of the number to be converted
   *
   * @return the converted string
   */
  fun convert(integer: String, decimal: String?): String =
    this.numToWord(integer) + (decimal?.let { " %s%s".format(this.wordDecSep, this.decimalConvert(it)) } ?: "")

  /**
   * Print a debug message on std error.
   *
   * @param mex the message to print
   */
  private fun debugPrint(mex: String) {
    if (this.debug) System.err.println(mex)
  }

  /**
   * Execute the processing of the input string splitting the input in segments of three digits counting
   * from less significant digits.
   *
   * @param digitNumber the number to be converted into words
   *
   * @return the word representing the input number
   */
  private fun numToWord(digitNumber: String): String {

    val wordLen = digitNumber.length
    var cursor = wordLen
    var fragmentNumber = -1

    debugPrint("digit num: $digitNumber\n")

    if (digitNumber == "0") return "zero"

    var wordNumber = ""

    while (cursor > 0) {

      ++fragmentNumber

      val result = convertLoop(
        fragmentNumber = fragmentNumber,
        cursor = cursor,
        digitNumber = digitNumber,
        wordNumber = wordNumber
      )
      cursor = result.first
      wordNumber = result.second
    }

    return wordNumber
  }

  /**
   *
   */
  private fun convertLoop(fragmentNumber: Int,
                          cursor: Int,
                          digitNumber: String,
                          wordNumber: String): Pair<Int, String> {

    var cursor1 = cursor
    var wordNumber1 = wordNumber

    val len = if (cursor1 > 3) 3 else cursor1

    val digitFragment = digitNumber.substring(cursor1 - len, cursor1)

    cursor1 -= len

    debugPrint("cursor: %d len: %d curfrag: %s n_seg: %d\n".format(cursor1, len, digitFragment, fragmentNumber))

    val wordFragment = getWordFragment(digitFragment, fragmentNumber)

    val suffix = if (wordFragment.isEmpty()) "" else getSuffix(fragmentNumber, digitFragment)

    wordNumber1 = concat(
      a = concat(a = wordFragment, sep = spaceBtwWords, b = suffix),
      sep = spaceBtwWords,
      b = wordNumber1
    )

    return Pair(cursor1, wordNumber1)
  }

  /**
   * Returns the provided number fragment converted to words.
   *
   * @param digitFragment the fragment to be converted
   * @param fragmentNumber the progressive index of the current fragment
   *
   * @return the word representation of the digit fragment
   */
  private fun getWordFragment(digitFragment: String, fragmentNumber: Int): String {

    var wordFragment = fragmentToWords(digitFragment)

    wordFragment = this.specialCasesHelper.specialCaseWordFragment(
      wordFragment = wordFragment,
      digitFragment = digitFragment,
      fragmentNumber = fragmentNumber
    ) ?: wordFragment

    return wordFragment
  }

  /**
   * Obtain the correct suffix for the current fragment.
   *
   * @param fragmentNumber the progressive number of the current fragment
   * @param digitFragment the fragment for which we are deciding the suffix
   *
   * @return the suffix for the current fragment
   */
  private fun getSuffix(fragmentNumber: Int, digitFragment: String): String {

    val specialCaseSuffix: String? = this.specialCasesHelper.getSuffix(
      fragmentNumber = fragmentNumber,
      digitFragment = digitFragment)

    return specialCaseSuffix ?: digitFragment.trimStart('0').let { fragment ->

      val fragKey: String = if (fragment == "1") "sing" else "plur"

      when (fragmentNumber) {
        0 -> ""
        1 -> this.langParams.suff.getValue("thousand").getValue(fragKey)
        2 -> this.langParams.suff.getValue("million").getValue(fragKey)
        3 -> this.langParams.suff.getValue("billion").getValue(fragKey)
        4 -> this.langParams.suff.getValue("trillion").getValue(fragKey)
        5 -> this.langParams.suff.getValue("quadrillion").getValue(fragKey)
        6 -> this.langParams.suff.getValue("quintillion").getValue(fragKey)
        7 -> this.langParams.suff.getValue("sextillion").getValue(fragKey)
        8 -> this.langParams.suff.getValue("septillion").getValue(fragKey)
        9 -> this.langParams.suff.getValue("octillion").getValue(fragKey)
        10 -> this.langParams.suff.getValue("nonillion").getValue(fragKey)
        11 -> this.langParams.suff.getValue("decillion").getValue(fragKey)
        else -> TODO("Number is too big")
      }
    }
  }

  /**
   *  Assuming the input is the decimal portion of a number converts it to its word representation.
   *
   * @param digitNumber the sequence of digits to convert
   *
   * @return the word representation of the input
   */
  private fun decimalConvert(digitNumber: String): String {

    return digitNumber.toCharArray().fold("") { accum, elem -> accum + " " + digitToWord(elem.toString())}
  }

  /**
   * Convert a fragment of a number to a word representation.
   *
   * @param arg the fragment to convert
   *
   * @return the word representation of the fragment
   */
  private fun fragmentToWords(arg : String): String {

    var tmpArg = arg
    var wordHundred = ""

    if(tmpArg.length == 3) {

      val hundred = tmpArg.substring(0,1)
      tmpArg = tmpArg.substring(1)

      wordHundred = if (hundred == "0") "" else this.langParams.numbers.getValue(hundred + "00")
    }

    val wordTens = this.langParams.numbers[tmpArg.trimStart('0')] ?: ""

    return concat(a = wordHundred, sep = this.spaceBtwWords, b = wordTens)
  }

  /**
   * Convert a single or double digit to its word representation.
   *
   * @param digit the number to convert
   *
   * @return the word representation
   */
  private fun digitToWord(digit: String): String = langParams.numbers.getValue(digit)
}