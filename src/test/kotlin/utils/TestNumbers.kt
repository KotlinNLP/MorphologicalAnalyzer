/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package utils

import com.beust.klaxon.*
import com.kotlinnlp.morphologicalanalyzer.numbers.Number
import java.io.FileNotFoundException
import java.nio.file.Paths

/**
 * Contains list of numbers tests.
 */
object TestNumbers {

  /**
   * A number test.
   *
   * @property text a text that can contain a date-time
   * @property tokens the list of numbers to recognize in the [text]
   */
  data class Test(val text: String, val tokens: List<Number>)

  /**
   * A list of padding texts to put before and after a text to generate adding tests.
   */
  private val paddings: List<Pair<String, String>> = listOf(
    Pair("Padding before the number ", " and padding after."),
    Pair("Padding before the number   \t ", "  \t  and padding after."),
    Pair("Padding before the number \"", "\" and padding after.")
  )

  /**
   * The list of supported test languages.
   */
  private val supportedLanguages: List<String> = listOf("en", "it")

  /**
   * List of test groups associated by language.
   */
  val tests: Map<String, List<Test>> = this.supportedLanguages.associate { it to loadTests(langCode = it) }

  /**
   * Load tests of a given type and language from the resources.
   *
   * @param langCode the language iso-a2 code
   *
   * @return a list of tests
   */
  private fun loadTests(langCode: String): List<Test> {

    val resPath = "numbers/$langCode/test_numbers.json"
    val absResPath: String = try {
      TestNumbers::class.java.classLoader.getResource(resPath).file
    } catch (e: NullPointerException) {
      throw FileNotFoundException(resPath)
    }

    val jsonList = Parser().parse(absResPath) as JsonArray<*>

    return jsonList.flatMap { it as JsonObject

      val paddingTests = this.paddings.flatMap { (paddingBefore, paddingAfter) ->
        this.getPaddingTests(jsonObj = it, paddingBefore = paddingBefore, paddingAfter = paddingAfter)
      }

      paddingTests + this.buildTest(it, originalText = it.string("text")!!, text = it.string("text")!!, offset = 0)
    }
  }

  /**
   * Get padding tests related to an item of the test file.
   * For each object 3 tests are built, applying combinations of padding texts before and after the 'text'.
   *
   * @param jsonObj a JSON object read from the test resource file
   * @param paddingBefore a string to add before the text to generate an adding test
   * @param paddingAfter a string to add after the text to generate an adding test
   *
   * @return a list of tests
   */
  private fun getPaddingTests(jsonObj: JsonObject, paddingBefore: String, paddingAfter: String): List<Test> {

    val text: String = jsonObj.string("text")!!
    val allPaddedText: String = paddingBefore + text + paddingAfter

    return listOf(
      this.buildTest(jsonObj, originalText = text, text = text + paddingAfter, offset = 0),
      this.buildTest(jsonObj, originalText = text, text = paddingBefore + text, offset = paddingBefore.length),
      this.buildTest(jsonObj, originalText = text, text = allPaddedText, offset = paddingBefore.length)
    )
  }

  /**
   * Build a [Test].
   *
   * @param jsonObj the JSON object of the test
   * @param originalText the original text of the test without padding
   * @param text the text of the test
   * @param offset the offset of chars added as padding
   *
   * @return a test object
   */
  private fun buildTest(jsonObj: JsonObject, originalText: String, text: String, offset: Int): Test =
    Test(
      text = text,
      tokens = jsonObj.array<JsonObject>("tokens")!!.map {
        buildNumber(jsonObj = it, originalText = originalText, offset = offset)
      }
    )

  /**
   * Build a [Number].
   *
   * @param jsonObj the JSON object of the number token
   * @param originalText the original text of the test without padding
   * @param offset the offset of chars added as padding
   *
   * @return a new number token
   */
  private fun buildNumber(jsonObj: JsonObject, originalText: String, offset: Int): Number {

    val originalStr: String = jsonObj.string("original") ?: originalText
    val start: Int = offset + (jsonObj.int("start") ?: 0)

    return Number(
      startToken = start,
      endToken = start + originalStr.lastIndex,
      value = jsonObj.getValue("value") as kotlin.Number,
      asWord = jsonObj.string("asWord") ?: originalStr,
      original = originalStr
    )
  }
}
