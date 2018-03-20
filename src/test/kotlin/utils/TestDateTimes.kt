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

/**
 *
 */
object TestDateTimes {

  /**
   *
   */
  private const val RES_FILENAME = "test_date_times.json"

  /**
   *
   */
  private const val PADDING_BEFORE = "Some padding before "

  /**
   *
   */
  private const val PADDING_AFTER = " and some padding after."

  /**
   *
   */
  val textsToDateTimes = mutableListOf<Pair<String, DateTime>>()

  /**
   *
   */
  val emptyDateTimesTexts = mutableListOf<String>()

  /**
   *
   */
  init {
    this.readDateTimes()
  }

  /**
   *
   */
  private fun readDateTimes() {

    val absResFilename: String = TestDateTimes::class.java.classLoader.getResource(this.RES_FILENAME).file
    val jsonList: JsonArray<*> = Parser().parse(absResFilename) as JsonArray<*>

    jsonList.map { it as JsonObject

      val type: String = it.string("type")!!
      val text: String = it.string("text")!!

      if (type == "null")
        this.emptyDateTimesTexts.add(text)
      else
        this.addDateTimes(it)
    }
  }

  /**
   *
   */
  private fun addDateTimes(jsonObj: JsonObject) {

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
   *
   */
  private fun addDateTime(jsonObj: JsonObject, text: String, start: Int, end: Int) {

    val type: String = jsonObj.string("type")!!

    this.textsToDateTimes.add(
      text to when (type) {
        "date" -> this.buildDate(jsonObj = jsonObj, startIndex = start, endIndex = end)
        "time" -> this.buildTime(jsonObj = jsonObj, startIndex = start, endIndex = end)
        "datetime" -> this.buildDateTime(jsonObj = jsonObj, startIndex = start, endIndex = end)
        else -> throw RuntimeException("Invalid DateTime type: $type")
      }
    )
  }

  /**
   *
   */
  private fun buildDate(jsonObj: JsonObject, startIndex: Int, endIndex: Int) = Date(
    startToken = startIndex,
    endToken = endIndex,
    day = jsonObj.int("D"),
    weekDay = jsonObj.int("week-D"),
    month = jsonObj.int("M"),
    year = jsonObj.int("Y"),
    yearAbbr = jsonObj.boolean("Y-abbr") ?: false
  )

  /**
   *
   */
  private fun buildTime(jsonObj: JsonObject, startIndex: Int, endIndex: Int) = Time(
    startToken = startIndex,
    endToken = endIndex,
    sec = jsonObj.int("s"),
    min = jsonObj.int("m"),
    hour = jsonObj.int("h")
  )

  /**
   *
   */
  private fun buildDateTime(jsonObj: JsonObject, startIndex: Int, endIndex: Int): DateTimeSimple {

    val dateObj: JsonObject = jsonObj.obj("date")!!
    val timeObj: JsonObject = jsonObj.obj("time")!!

    return DateTimeSimple(
      startToken = startIndex,
      endToken = endIndex,
      date = this.buildDate(
        jsonObj = dateObj,
        startIndex = dateObj.int("start")!!,
        endIndex = dateObj.int("end")!!
      ),
      time = this.buildTime(
        jsonObj = timeObj,
        startIndex = timeObj.int("start")!!,
        endIndex = timeObj.int("end")!!
      )
    )
  }
}