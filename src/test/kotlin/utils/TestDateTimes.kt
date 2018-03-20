/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package utils

import com.beust.klaxon.*
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Date
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTime
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTimeSimple
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Time
import java.util.*

/**
 * Contains list of [DateTime]s for tests.
 */
object TestDateTimes {

  /**
   * The name of the JSON file containing the test date times, placed in the 'resources'.
   * The JSON in it is a list of objects representing a test.
   *
   * All tests have 'text' and 'type' fields. Depending on the value of 'type' they can contain adding specific fields.
   *
   * The 'type' field represents the type of [DateTime] contained in the 'text' and it can have one of the following
   * values: 'date', 'time', 'datetime', 'null'.
   */
  private const val RES_FILENAME = "test_date_times.json"

  /**
   * A padding text to put before a date-time text.
   */
  private const val PADDING_BEFORE = "Some padding before "

  /**
   * A padding text to put after a date-time text.
   */
  private const val PADDING_AFTER = " and some padding after."

  /**
   * A list of pairs with a text and the date-time object contained in it.
   */
  val textsToDateTimes = mutableListOf<Pair<String, DateTime>>()

  /**
   * A list of texts that do not contain any date-time.
   */
  val emptyDateTimesTexts = mutableListOf<String>()

  /**
   * Initialize the lists of texts and date-times.
   */
  init {

    val absResFilename: String = TestDateTimes::class.java.classLoader.getResource(this.RES_FILENAME).file
    val jsonList: JsonArray<*> = Parser().parse(absResFilename) as JsonArray<*>

    jsonList.map { it as JsonObject

      val type: String = it.string("type")!!
      val text: String = it.string("text")!!

      if (type == "null")
        this.emptyDateTimesTexts.add(text)
      else
        this.addDateTimeTests(it)
    }
  }

  /**
   * Add tests related to an object of the test file.
   * For each object 4 tests are added, applying combinations of padding texts before and after the 'text'.
   *
   * @param jsonObj a JSON object read from the test resource file
   */
  private fun addDateTimeTests(jsonObj: JsonObject) {

    val text: String = jsonObj.string("text")!!
    val startWithPad: Int = this.PADDING_BEFORE.length
    val endWithPad: Int = this.PADDING_BEFORE.length + text.lastIndex
    val allPaddedText: String = this.PADDING_BEFORE + text + this.PADDING_AFTER

    this.addDateTime(jsonObj = jsonObj, text = text, start = 0, end = text.lastIndex)
    this.addDateTime(jsonObj = jsonObj, text = text + this.PADDING_AFTER, start = 0, end = text.lastIndex)
    this.addDateTime(jsonObj = jsonObj, text = this.PADDING_BEFORE + text, start = startWithPad, end = endWithPad)
    this.addDateTime(jsonObj = jsonObj, text = allPaddedText, start = startWithPad, end = endWithPad)
  }

  /**
   * Add a [DateTime] test.
   *
   * @param jsonObj the JSON object of the test
   * @param text the text of the test
   * @param start the char index at which the expected date-time starts in the [text] (inclusive)
   * @param end the char index at which the expected date-time ends in the [text] (inclusive)
   */
  private fun addDateTime(jsonObj: JsonObject, text: String, start: Int, end: Int) {

    val type: String = jsonObj.string("type")!!

    this.textsToDateTimes.add(
      text to when (type) {
        "date" -> this.buildDate(jsonObj = jsonObj, start = start, end = end)
        "time" -> this.buildTime(jsonObj = jsonObj, start = start, end = end)
        "datetime" -> this.buildDateTime(jsonObj = jsonObj, start = start, end = end)
        else -> throw RuntimeException("Invalid DateTime type: $type")
      }
    )
  }

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
    yearAbbr = jsonObj.boolean("Y-abbr") ?: false
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
}