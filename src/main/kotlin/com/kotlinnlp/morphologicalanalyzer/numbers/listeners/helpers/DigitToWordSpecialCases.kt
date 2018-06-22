/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.numbers.listeners.helpers

import com.kotlinnlp.morphologicalanalyzer.numbers.languageparams.LanguageParams

/**
 * Helper for the DigitToWordConverter that provides methods to handle language specific special cases.
 *
 * @param langParams contains the language-specific parameters
 */
internal class DigitToWordSpecialCases(private val langParams: LanguageParams) {

  /**
   * Get the suffix for special cases.
   *
   * @param fragmentNumber the progressive number of the current fragment
   * @param digitFragment the fragment for which we are deciding the suffix
   *
   * @return null if no special case is triggered, otherwise the suffix for the current fragment
   */
  fun getSuffix(fragmentNumber: Int, digitFragment: String): String? =

    when (this.langParams.language) {

      "it" -> {
        if (fragmentNumber == 1)
          if (digitFragment.trimStart('0') == "1") "" else this.langParams.suff.getValue("thousand").getValue("plur")
        else
          null
      }

      else -> null
    }

  /**
   * Word fragment generation for special cases.
   *
   * @param wordFragment
   * @param fragmentNumber
   * @param digitFragment
   *
   * @return null if no special case is triggered, otherwise the converted string
   */
  fun specialCaseWordFragment(wordFragment: String, fragmentNumber: Int, digitFragment: String): String? =

    when (this.langParams.language) {

      "it" -> {

        var word: String = wordFragment

        if (fragmentNumber == 1 && digitFragment.trimStart('0') == "1") {
          word = this.langParams.suff.getValue("thousand").getValue("sing")
        }

        if (fragmentNumber > 0 && word.length >= 3 && word.endsWith(this.langParams.words["one"]!!)) {
          // Change the word "uno" with "un" if it is followed by a suffix even if there are more characters before.
          word = word.substring(0, word.length - 1)
        }

        word
      }

      else -> wordFragment
    }
}
