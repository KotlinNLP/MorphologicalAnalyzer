/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Date
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTime
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTimeSimple
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Time
import com.kotlinnlp.neuraltokenizer.Token
import java.util.*

/**
 * The helper to build a [DateTimeSimple].
 *
 * @param tokens the list of tokens that compose the input text.
 */
internal class DateTimeBuilder(private val tokens: List<Token>) {

  /**
   *
   */
  private var dateTokens: IntRange? = null

  /**
   *
   */
  private var timeTokens: IntRange? = null

  /**
   * The day of the currently building date-time.
   */
  var day: Int? = null

  /**
   * The week day of the currently building date-time.
   */
  var weekDay: Int? = null

  /**
   * The month of the currently building date-time.
   */
  var month: Int? = null

  /**
   * The year of the currently building date-time.
   */
  var year: Int? = null

  /**
   * The year abbr. indication of the currently building date-time.
   */
  var yearAbbr: Boolean = false

  /**
   * The hour of the currently building date-time.
   */
  var hour: Int? = null

  /**
   * The minute of the currently building date-time.
   */
  var min: Int? = null

  /**
   * The second of the currently building date-time.
   */
  var sec: Int? = null

  /**
   * The millisecond of the currently building date-time.
   */
  var millisec: Int? = null

  /**
   * The timezone of the currently building date-time.
   */
  var timezone: TimeZone? = null

  /**
   *
   */
  fun getDateTime(startIndex: Int, endIndex: Int): DateTime {

    val start: Int = this.tokens.indexOfFirst { it.startAt == startIndex }
    val end: Int = this.tokens.indexOfFirst { it.endAt == endIndex }

    val date: Date? = this.buildDate()
    val time: Time? = this.buildTime()

    return when {
      date != null && time != null -> DateTimeSimple(startToken = start, endToken = end, date = date, time = time)
      date != null -> date
      time != null -> time
      else -> throw RuntimeException("Invalid date-time: missing date and time.")
    }
  }

  /**
   *
   */
  fun setDateTokens(startIndex: Int, endIndex: Int) {

    this.dateTokens = IntRange(
      this.tokens.indexOfFirst { it.startAt == startIndex },
      this.tokens.indexOfFirst { it.endAt == endIndex }
    )
  }

  /**
   *
   */
  fun setTimeTokens(startIndex: Int, endIndex: Int) {

    this.timeTokens = IntRange(
      this.tokens.indexOfFirst { it.startAt == startIndex },
      this.tokens.indexOfFirst { it.endAt == endIndex }
    )
  }

  /**
   *
   */
  private fun buildDate(): Date? = this.dateTokens?.let {
    Date(
      startToken = it.start,
      endToken = it.endInclusive,
      day = this.day,
      weekDay = this.weekDay,
      month = this.month,
      year = this.year,
      yearAbbr = this.yearAbbr
    )
  }

  /**
   *
   */
  private fun buildTime(): Time? = this.timeTokens?.let {
    Time(
      startToken = it.start,
      endToken = it.endInclusive,
      hour = this.hour,
      min = this.min,
      sec = this.sec,
      millisec = this.millisec,
      timezone = this.timezone
    )
  }
}
