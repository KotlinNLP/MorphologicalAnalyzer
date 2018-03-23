/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package utils

import com.beust.klaxon.*
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.*
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Date
import java.nio.file.Paths
import java.util.*

/**
 * Contains list of [DateTime]s for tests.
 */
object TestDateTimes {

  /**
   * A group of tests.
   *
   * @property type the type of tests ("date", "time", "offset", etc.)
   * @property tests the list of tests
   */
  data class TestGroup(val type: String, val tests: List<Test>)

  /**
   * A date-time test.
   *
   * @property text a text that can contain a date-time
   * @property dateTime the expected date-time (can be null)
   */
  data class Test(val text: String, val dateTime: DateTime? = null)

  /**
   * The name of the JSON file containing the test date times, placed in the 'resources', to be formatted with the
   * type of tests that contains.
   * The JSON in it is a list of objects representing a test.
   *
   * All tests have 'text' and 'type' fields. Depending on the value of 'type' they can contain adding specific fields.
   *
   * The 'type' field represents the type of [DateTime] contained in the 'text' and it can have one of the following
   * values: 'date', 'time', 'datetime', 'null'.
   */
  private const val RES_UNFORMATTED = "test_%s.json"

  /**
   * A padding text to put before a date-time text.
   */
  private const val PADDING_BEFORE = "Some padding before "

  /**
   * A padding text to put after a date-time text.
   */
  private const val PADDING_AFTER = " and some padding after."

  /**
   * List of test groups associated by language.
   */
  val tests: Map<String, List<TestGroup>>

  /**
   * Initialize the tests.
   */
  init {

    val testLangs = listOf("en")
    val testTypes = listOf("date", "time", "offset", "null")

    this.tests = testLangs.associate { lang ->
      lang to testTypes.map { type -> TestGroup(type = type, tests = loadTests(type = type, langCode = lang)) }
    }
  }

  /**
   * Load tests of a given type and language from the resources.
   *
   * @param type the type of test ("date", "time", "offset", etc.)
   * @param langCode the language iso-a2 code
   *
   * @return a list of tests
   */
  private fun loadTests(type: String, langCode: String): List<Test> {

    val formattedResName: String = this.RES_UNFORMATTED.format(type)
    val simpleResFilename: String = Paths.get(langCode, formattedResName).toString()
    val absResFilename: String = TestDateTimes::class.java.classLoader.getResource(simpleResFilename).file

    val jsonList: JsonArray<*> = Parser().parse(absResFilename) as JsonArray<*>

    return jsonList.flatMap { it as JsonObject

      if (type == "null")
        listOf(Test(text = it.string("text")!!))
      else
        this.getTests(jsonObj = it, type = type)
    }

  }

  /**
   * Get tests related to an object of the test file.
   * For each object 4 tests are added, applying combinations of padding texts before and after the 'text'.
   *
   * @param jsonObj a JSON object read from the test resource file
   * @param type the test type
   *
   * @return a list of tests
   */
  private fun getTests(jsonObj: JsonObject, type: String): List<Test> {

    val text: String = jsonObj.string("text")!!
    val startWithPad: Int = this.PADDING_BEFORE.length
    val endWithPad: Int = this.PADDING_BEFORE.length + text.lastIndex
    val allPaddedText: String = this.PADDING_BEFORE + text + this.PADDING_AFTER

    return listOf(
      this.buildTest(jsonObj, type = type, text = text, start = 0, end = text.lastIndex),
      this.buildTest(jsonObj, type = type, text = text + this.PADDING_AFTER, start = 0, end = text.lastIndex),
      this.buildTest(jsonObj, type = type, text = this.PADDING_BEFORE + text, start = startWithPad, end = endWithPad),
      this.buildTest(jsonObj, type = type, text = allPaddedText, start = startWithPad, end = endWithPad)
    )
  }

  /**
   * Build a [Test].
   *
   * @param jsonObj the JSON object of the test
   * @param type the test type
   * @param text the text of the test
   * @param start the char index at which the expected date-time starts in the [text] (inclusive)
   * @param end the char index at which the expected date-time ends in the [text] (inclusive)
   *
   * @return a test object
   */
  private fun buildTest(jsonObj: JsonObject, type: String, text: String, start: Int, end: Int) = Test(
    text = text,
    dateTime = when (type) {
      "date" -> this.buildDate(jsonObj = jsonObj, start = start, end = end)
      "time" -> this.buildTime(jsonObj = jsonObj, start = start, end = end)
      "datetime" -> this.buildDateTime(jsonObj = jsonObj, start = start, end = end)
      "offset" -> this.buildOffset(jsonObj = jsonObj, start = start, end = end)
      else -> throw RuntimeException("Invalid DateTime type: $type")
    }
  )

  /**
   * Build a [Date] object.
   *
   * @param jsonObj the JSON object containing the information of the date
   * @param start the char index at which the expected date starts in the text (inclusive)
   * @param end the char index at which the expected date ends in the text (inclusive)
   *
   * @return a date object
   */
  private fun buildDate(jsonObj: JsonObject, start: Int, end: Int) = Date(
    startToken = start,
    endToken = end,
    day = jsonObj.int("D"),
    weekDay = jsonObj.int("week-D"),
    month = jsonObj.int("M"),
    year = jsonObj.int("Y"),
    yearAbbr = jsonObj.boolean("Y-abbr") ?: false,
    holiday = jsonObj.string("holiday")?.let {
      Date.Holiday.values().first { h -> h.toString().toLowerCase() == it.toLowerCase() }
    }
  )

  /**
   * Build a [Time] object.
   *
   * @param jsonObj the JSON object containing the information of the time
   * @param start the char index at which the expected time starts in the text (inclusive)
   * @param end the char index at which the expected time ends in the text (inclusive)
   *
   * @return a time object
   */
  private fun buildTime(jsonObj: JsonObject, start: Int, end: Int) = Time(
    startToken = start,
    endToken = end,
    hour = jsonObj.int("h"),
    min = jsonObj.int("m"),
    sec = jsonObj.int("s"),
    millisec = jsonObj.int("ms"),
    timezone = jsonObj.string("tz")?.let { TimeZone.getTimeZone(it) }
  )

  /**
   * Build a [DateTimeSimple] object.
   *
   * @param jsonObj the JSON object containing the information of the date-time
   * @param start the char index at which the expected date-time starts in the text (inclusive)
   * @param end the char index at which the expected date-time ends in the text (inclusive)
   *
   * @return a date-time object
   */
  private fun buildDateTime(jsonObj: JsonObject, start: Int, end: Int): DateTimeSimple {

    val dateObj: JsonObject = jsonObj.obj("date")!!
    val timeObj: JsonObject = jsonObj.obj("time")!!

    return DateTimeSimple(
      startToken = start,
      endToken = end,
      date = this.buildDate(
        jsonObj = dateObj,
        start = start + dateObj.int("start")!!, // the field in the object is an offset
        end = start + dateObj.int("end")!! // the field in the object is an offset
      ),
      time = this.buildTime(
        jsonObj = timeObj,
        start = start + timeObj.int("start")!!, // the field in the object is an offset
        end = start + timeObj.int("end")!! // the field in the object is an offset
      )
    )
  }

  /**
   * Build an [Offset] object.
   *
   * @param jsonObj the JSON object containing the information of the offset
   * @param start the char index at which the expected offset starts in the text (inclusive)
   * @param end the char index at which the expected offset ends in the text (inclusive)
   *
   * @return an offset object
   */
  private fun buildOffset(jsonObj: JsonObject, start: Int, end: Int): Offset {

    val type: String = jsonObj.string("offset-type")!!
    val positive: Boolean = jsonObj.boolean("positive")!!
    val units: Int = jsonObj.int("units")!!

    return when (type) {

      "date" -> {

        val dateObj: JsonObject = jsonObj.obj("date")!!
        val date: Date = this.buildDate(
          jsonObj = dateObj,
          start = start + dateObj.int("start")!!, // the field in the object is an offset
          end = start + dateObj.int("end")!! // the field in the object is an offset
        )

        Offset.Date(startToken = start, endToken = end, positive = positive, units = units, value = date)
      }

      "hour" -> Offset.Hours(startToken = start, endToken = end, positive = positive, units = units)
      "min" -> Offset.Minutes(startToken = start, endToken = end, positive = positive, units = units)
      "sec" -> Offset.Seconds(startToken = start, endToken = end, positive = positive, units = units)
      "day" -> Offset.Days(startToken = start, endToken = end, positive = positive, units = units)
      "week" -> Offset.Weeks(startToken = start, endToken = end, positive = positive, units = units)
      "weekend" -> Offset.Weekends(startToken = start, endToken = end, positive = positive, units = units)
      "month" -> Offset.Months(startToken = start, endToken = end, positive = positive, units = units)
      "year" -> Offset.Years(startToken = start, endToken = end, positive = positive, units = units)

      else -> throw RuntimeException("Invalid offset type: $type")
    }
  }
}