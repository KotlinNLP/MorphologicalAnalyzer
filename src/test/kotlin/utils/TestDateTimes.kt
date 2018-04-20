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
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.intervals.CloseInterval
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.intervals.Interval
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.intervals.OpenFromInterval
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.intervals.OpenToInterval
import java.io.FileNotFoundException
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
   * A list of padding texts to put before and after a text to generate adding tests.
   */
  private val paddings: List<Pair<String, String>> = listOf(
    Pair("Padding before the datetime ", " and padding after."),
    Pair("Padding before the datetime   \t ", "  \t  and padding after."),
    Pair("Padding before the datetime \"", "\" and padding after.")
  )

  /**
   * The list of supported test languages.
   */
  private val supportedLanguages: List<String> = listOf("en")

  /**
   * The list of supported test types.
   */
  private val supportedTypes: List<String> = listOf(
    "null",
    "date",
    "time",
    "date_time_simple",
    "offset",
    "date_offset",
    "date_ordinal",
    "interval"
  )

  /**
   * List of test groups associated by language.
   */
  val tests: Map<String, List<TestGroup>>

  /**
   * Initialize the tests.
   */
  init {

    this.tests = this.supportedLanguages.associate { lang ->
      lang to this.supportedTypes.map { type ->
        TestGroup(type = type, tests = loadTests(type = type, langCode = lang))
      }
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

    val absResFilename: String = try {
      TestDateTimes::class.java.classLoader.getResource(simpleResFilename).file
    } catch (e: NullPointerException) {
      throw FileNotFoundException(simpleResFilename)
    }

    val jsonList: JsonArray<*> = Parser().parse(absResFilename) as JsonArray<*>

    return jsonList.flatMap { it as JsonObject

      if (type == "null")
        listOf(Test(text = it.string("text")!!))
      else
        this.paddings.flatMap { (paddingBefore, paddingAfter) ->
          this.getTests(jsonObj = it, type = type, paddingBefore = paddingBefore, paddingAfter = paddingAfter)
        }
    }
  }

  /**
   * Get tests related to an object of the test file.
   * For each object 4 tests are added, applying combinations of padding texts before and after the 'text'.
   *
   * @param jsonObj a JSON object read from the test resource file
   * @param type the test type
   * @param paddingBefore a string to add before the text to generate an adding test
   * @param paddingAfter a string to add after the text to generate an adding test
   *
   * @return a list of tests
   */
  private fun getTests(jsonObj: JsonObject, type: String, paddingBefore: String, paddingAfter: String): List<Test> {

    val text: String = jsonObj.string("text")!!
    val startWithPad: Int = paddingBefore.length
    val endWithPad: Int = paddingBefore.length + text.lastIndex
    val allPaddedText: String = paddingBefore + text + paddingAfter

    return listOf(
      this.buildTest(jsonObj, type = type, text = text, start = 0, end = text.lastIndex),
      this.buildTest(jsonObj, type = type, text = text + paddingAfter, start = 0, end = text.lastIndex),
      this.buildTest(jsonObj, type = type, text = paddingBefore + text, start = startWithPad, end = endWithPad),
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
      "date_time_simple" -> this.buildDateTime(jsonObj = jsonObj, start = start, end = end)
      "offset" -> this.buildOffset(jsonObj = jsonObj, start = start, end = end)
      "date_offset" -> this.buildDateOffset(jsonObj = jsonObj, start = start, end = end)
      "date_ordinal" -> this.buildDateOrdinal(jsonObj = jsonObj, start = start, end = end)
      "interval" -> this.buildInterval(jsonObj = jsonObj, start = start, end = end)
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
    generic = jsonObj.string("generic")?.let { gTime ->
      Time.Generic.values().first { it.toString().toLowerCase() == gTime }
    },
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
  private fun buildDateTime(jsonObj: JsonObject, start: Int, end: Int) = DateTimeSimple(
    startToken = start,
    endToken = end,
    date = this.buildInnerDate(jsonObj = jsonObj.obj("date")!!, offset = start),
    time = this.buildInnerTime(jsonObj = jsonObj.obj("time")!!, offset = start)
  )

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
    val units: Int = jsonObj.int("units")!!

    return when (type) {
      "date" -> Offset.Date(
        startToken = start,
        endToken = end,
        units = units,
        value = this.buildInnerDate(jsonObj = jsonObj.obj("date")!!, offset = start)
      )
      "hour" -> Offset.Hours(startToken = start, endToken = end, units = units)
      "min" -> Offset.Minutes(startToken = start, endToken = end, units = units)
      "sec" -> Offset.Seconds(startToken = start, endToken = end, units = units)
      "day" -> Offset.Days(startToken = start, endToken = end, units = units)
      "week" -> Offset.Weeks(startToken = start, endToken = end, units = units)
      "weekend" -> Offset.Weekends(startToken = start, endToken = end, units = units)
      "month" -> Offset.Months(startToken = start, endToken = end, units = units)
      "year" -> Offset.Years(startToken = start, endToken = end, units = units)
      else -> throw RuntimeException("Invalid offset type: $type")
    }
  }

  /**
   * Build a [DateOffset] object.
   *
   * @param jsonObj the JSON object containing the information of the date-offset
   * @param start the char index at which the expected date-offset starts in the text (inclusive)
   * @param end the char index at which the expected date-offset ends in the text (inclusive)
   *
   * @return a date-offset object
   */
  private fun buildDateOffset(jsonObj: JsonObject, start: Int, end: Int) = DateOffset(
    startToken = start,
    endToken = end,
    dateTime = this.getInnerDateTime(jsonObj = jsonObj, offset = start),
    offset = this.buildInnerOffset(jsonObj = jsonObj.obj("offset")!!, offset = start)
  )

  /**
   * Build a [DateOrdinal] object.
   *
   * @param jsonObj the JSON object containing the information of the ordinal date
   * @param start the char index at which the expected ordinal date starts in the text (inclusive)
   * @param end the char index at which the expected ordinal date ends in the text (inclusive)
   *
   * @return an ordinal date object
   */
  private fun buildDateOrdinal(jsonObj: JsonObject, start: Int, end: Int): DateOrdinal {

    val type: String = jsonObj.string("type")!!
    val refDateTime: SingleDateTime = this.getInnerDateTime(jsonObj = jsonObj.obj("ref")!!, offset = start)
    val pos: DateOrdinal.Position = jsonObj.int("position")!!.let {
      if (it < 0) DateOrdinal.Position.Last() else DateOrdinal.Position.Ordinal(count = it)
    }

    return when (type) {
      "date" -> DateOrdinal.Date(
        startToken = start,
        endToken = end,
        position = pos,
        dateTime = refDateTime,
        value = this.buildInnerDate(jsonObj = jsonObj.obj("date-unit")!!, offset = start)
      )
      "day" -> DateOrdinal.Day(startToken = start, endToken = end, position = pos, dateTime = refDateTime)
      "week" -> DateOrdinal.Week(startToken = start, endToken = end, position = pos, dateTime = refDateTime)
      "weekend" -> DateOrdinal.Weekend(startToken = start, endToken = end, position = pos, dateTime = refDateTime)
      "month" -> DateOrdinal.Month(startToken = start, endToken = end, position = pos, dateTime = refDateTime)
      "year" -> DateOrdinal.Year(startToken = start, endToken = end, position = pos, dateTime = refDateTime)
      else -> throw RuntimeException("Invalid offset type: $type")
    }
  }

  /**
   * Build an [Interval] object.
   *
   * @param jsonObj the JSON object containing the information of the interval
   * @param start the char index at which the expected interval starts in the text (inclusive)
   * @param end the char index at which the expected interval ends in the text (inclusive)
   *
   * @return an interval object
   */
  private fun buildInterval(jsonObj: JsonObject, start: Int, end: Int): Interval {

    val from: SingleDateTime? = jsonObj.obj("from")?.let {
      this.getInnerDateTime(jsonObj = it, offset = start)
    }

    val to: SingleDateTime? = jsonObj.obj("to")?.let {
      this.getInnerDateTime(jsonObj = it, offset = start)
    }

    return when {
      from != null && to != null -> CloseInterval(startToken = start, endToken = end, from = from, to = to)
      from != null -> OpenToInterval(startToken = start, endToken = end, from = from)
      to != null -> OpenFromInterval(startToken = start, endToken = end, to = to)
      else -> throw RuntimeException("Missing 'from' or 'to' interval properties.")
    }
  }

  /**
   * Build a generic [SingleDateTime] that is part of an outer [DateTime] object.
   *
   * @param jsonObj the JSON object containing the information of the date-time
   * @param offset the start char index of the outer object (added as offset to the start-end indices of the [jsonObj])
   *
   * @return a date-time object
   */
  private fun getInnerDateTime(jsonObj: JsonObject, offset: Int): SingleDateTime = when {
    "date" in jsonObj -> this.buildInnerDate(jsonObj = jsonObj.obj("date")!!, offset = offset)
    "time" in jsonObj -> this.buildInnerTime(jsonObj = jsonObj.obj("time")!!, offset = offset)
    "offset" in jsonObj -> this.buildInnerOffset(jsonObj = jsonObj.obj("offset")!!, offset = offset)
    else -> throw RuntimeException("Missing a valid inner date-time.")
  }

  /**
   * Build a [Date] that is part of an outer [DateTime] object.
   *
   * @param jsonObj the JSON object containing the information of the date
   * @param offset the start char index of the outer object (added as offset to the start-end indices of the [jsonObj])
   *
   * @return a date object
   */
  private fun buildInnerDate(jsonObj: JsonObject, offset: Int): Date = this.buildDate(
    jsonObj = jsonObj,
    start = offset + jsonObj.int("start")!!,
    end = offset + jsonObj.int("end")!!
  )

  /**
   * Build a [Time] that is part of an outer [DateTime] object.
   *
   * @param jsonObj the JSON object containing the information of the time
   * @param offset the start char index of the outer object (added as offset to the start-end indices of the [jsonObj])
   *
   * @return a time object
   */
  private fun buildInnerTime(jsonObj: JsonObject, offset: Int): Time = this.buildTime(
    jsonObj = jsonObj,
    start = offset + jsonObj.int("start")!!,
    end = offset + jsonObj.int("end")!!
  )

  /**
   * Build an [Offset] that is part of an outer [DateTime] object.
   *
   * @param jsonObj the JSON object containing the information of the offset
   * @param offset the start char index of the outer object (added as offset to the start-end indices of the [jsonObj])
   *
   * @return an offset object
   */
  private fun buildInnerOffset(jsonObj: JsonObject, offset: Int): Offset = this.buildOffset(
    jsonObj = jsonObj,
    start = offset + jsonObj.int("start")!!,
    end = offset + jsonObj.int("end")!!
  )
}