/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.beust.klaxon.JsonObject
import com.beust.klaxon.JsonObjectConverter
import com.beust.klaxon.Klaxon
import com.kotlinnlp.linguisticdescription.language.getLanguageByIso
import com.kotlinnlp.morphologicalanalyzer.numbers.NumbersProcessor
import com.kotlinnlp.morphologicalanalyzer.numbers.Number
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import utils.SimpleTokenizer
import utils.TestNumbers
import kotlin.test.assertEquals

object NumbersProcessorSpec : Spek({

  TestNumbers.tests.forEach { (lang, tests) ->

    describe("a NumbersProcessor for lang $lang") {

      tests.forEach { test ->

        context("number expression: '${test.text}'") {

          val processor = NumbersProcessor(getLanguageByIso(lang))
          val numbers: List<Number> =
            processor.findNumbers(text = test.text, tokens = SimpleTokenizer.tokenize(test.text), mode = "split")

          it("should contain %d elements".format(test.tokens.size)) {
            assertEquals(test.tokens.size, numbers.size)
          }

          numbers.zip(test.tokens).forEachIndexed { i, (number, testNumber) ->

            it("should match the value for the number n. ${i+1}") {
              assertEquals(testNumber.value, number.value)
            }

//            it("should match the word representation for the number n. ${i+1}") {
//              assertEquals(testNumber.asWord, number.asWord)
//            }

            it("should match the original string for the number n. ${i+1}") {
              assertEquals(testNumber.original, number.original)
            }

            it("should match the expected start token index for the number n. ${i+1}") {
              assertEquals(testNumber.startToken, number.startToken)
            }

            it("should match the expected end token index for the number n. ${i+1}") {
              assertEquals(testNumber.endToken, number.endToken)
            }
          }
        }
      }
    }
  }
})