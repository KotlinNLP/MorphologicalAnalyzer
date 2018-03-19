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
  private var day: Int? = null

  /**
   * The week day of the currently building date-time.
   */
  private var weekDay: Int? = null

  /**
   * The month of the currently building date-time.
   */
  private var month: Int? = null

  /**
   * The year of the currently building date-time.
   */
  private var year: Int? = null

  /**
   * The year abbr. indication of the currently building date-time.
   */
  private var yearAbbr: Boolean = false

  /**
   * The sec of the currently building date-time.
   */
  private var sec: Int? = null

  /**
   * The min of the currently building date-time.
   */
  private var min: Int? = null

  /**
   * The hour of the currently building date-time.
   */
  private var hour: Int? = null

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
  fun setDay(value: Int) {
    this.day = value
  }

  /**
   *
   */
  fun setWeekDay(value: Int) {
    this.weekDay = value
  }

  /**
   *
   */
  fun setMonth(value: Int) {
    this.month = value
  }

  /**
   *
   */
  fun setYear(value: Int) {
    this.year = value
  }

  /**
   *
   */
  fun setYearAbbr(value: Int) {
    this.year = value
    this.yearAbbr = true
  }

  /**
   *
   */
  fun setSec(value: Int) {
    this.sec = value
  }

  /**
   *
   */
  fun setMin(value: Int) {
    this.min = value
  }

  /**
   *
   */
  fun setHour(value: Int) {
    this.hour = value
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
      sec = this.sec,
      min = this.min,
      hour = this.hour
    )
  }
}
