/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.linguisticdescription.language.getLanguageByIso
import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.DateTime
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import utils.SimpleTokenizer
import utils.TestDateTimes
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test a [DateTimeProcessor].
 *
 * Texts are tokenized by chars, so the 'startToken' and the 'endToken' indices of a [DateTime] object are equal to
 * the position of the corresponding chars in the text.
 */
class DateTimeProcessorSpec : Spek({

  describe("a DateTimeProcessor") {

    TestDateTimes.tests.forEach { (lang, testGroups) ->
      testGroups.forEach { (testType, tests) ->

        context("[%s] %s".format(lang, testType)) {

          val processor = DateTimeProcessor(getLanguageByIso(lang))

          tests.forEach { (text, expectedDateTime) ->

            context("text: '$text'") {

              val tokens: List<RealToken> = SimpleTokenizer.tokenize(text)
              val dateTimes: List<DateTime> = processor.findDateTimes(text = text, tokens = tokens)

              if (expectedDateTime != null) {

                it("should return 1 DateTime object") {
                  assertEquals(1, dateTimes.size)
                }

                it("should return the expected DateTime object") {
                  assertEquals(expectedDateTime, dateTimes.single())
                }

              } else {

                it("should return an empty DateTimes list") {
                  assertTrue { dateTimes.isEmpty() }
                }
              }
            }
          }
        }
      }
    }
  }
})
