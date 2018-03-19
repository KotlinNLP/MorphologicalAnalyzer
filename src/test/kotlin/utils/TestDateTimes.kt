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
  private val resFilename: String = TestDateTimes::class.java.classLoader.getResource("test_date_times.json").file

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

    val jsonList: JsonArray<*> = Parser().parse(resFilename) as JsonArray<*>

    jsonList.map { this.buildDateTime(it as JsonObject ) }
  }

  /**
   *
   */
  private fun buildDateTime(jsonObj: JsonObject) {

    val type: String = jsonObj.string("type")!!
    val text: String = jsonObj.string("text")!!

    if (type == "null") {
      this.emptyDateTimesTexts.add(text)

    } else {
      this.textsToDateTimes.add(
        text to when (type) {
          "date" -> this.buildDate(jsonObj = jsonObj, startIndex = 0, endIndex = text.lastIndex)
          "time" -> this.buildTime(jsonObj = jsonObj, startIndex = 0, endIndex = text.lastIndex)
          "datetime" -> this.buildDateTime(jsonObj = jsonObj, startIndex = 0, endIndex = text.lastIndex)
          else -> throw RuntimeException("Invalid DateTime type: $type")
        }
      )
    }
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