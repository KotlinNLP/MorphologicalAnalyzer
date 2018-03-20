/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

import com.kotlinnlp.morphologicalanalyzer.datetime.DateTimeProcessor
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTime
import com.kotlinnlp.neuraltokenizer.Token
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
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

    context("English language") {

      TestDateTimes.textsToDateTimes.forEach { (text, expectedDateTime) ->

        on("text: '$text'") {

          val tokens: List<Token> = SimpleTokenizer.tokenize(text)
          val dateTimes: List<DateTime> = DateTimeProcessor.getDateTimes(text = text, tokens = tokens, langCode = "en")

          it("should return 1 DateTime object") {
            assertEquals(1, dateTimes.size)
          }

          it("should return the expected DateTime object") {
            assertEquals(expectedDateTime, dateTimes.first())
          }
        }
      }

      TestDateTimes.emptyDateTimesTexts.forEach { text ->

        on("text: '$text'") {

          val tokens: List<Token> = SimpleTokenizer.tokenize(text)
          val dateTimes: List<DateTime> = DateTimeProcessor.getDateTimes(text = text, tokens = tokens, langCode = "en")

          it("should return an empty DateTimes list") {
            assertTrue { dateTimes.isEmpty() }
          }
        }
      }
    }
  }
})