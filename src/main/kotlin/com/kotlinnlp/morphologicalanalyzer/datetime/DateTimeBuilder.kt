/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.morphologicalanalyzer.datetime.objects.*
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Date
import com.kotlinnlp.morphologicalanalyzer.datetime.utils.DateUnit
import com.kotlinnlp.neuraltokenizer.Token
import java.util.*

/**
 * The helper to build a [DateTimeSimple].
 *
 * @param tokens the list of tokens that compose the input text.
 */
internal class DateTimeBuilder(private val tokens: List<Token>) {

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
   * The holiday name of the currently building date-time.
   */
  var holiday: Date.Holiday? = null

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
   * The generic time of the currently building date-time.
   */
  var genericTime: Time.Generic? = null

  /**
   * The timezone of the currently building date-time.
   */
  var timezone: TimeZone? = null

  /**
   * The date unit type.
   */
  var dateUnit: DateUnit.Type? = null

  /**
   * Whether the currently building offset is positive (default true).
   */
  var positiveOffset: Boolean = true

  /**
   * The length of the currently building offset (default 1).
   */
  var offsetLength: Int = 1

  /**
   * The ordinal position of a [DateOrdinal].
   */
  var ordinalPosition: Int = 0

  /**
   * The reference date of an offset (null if the reference is a date unit - e.g. "days", "weeks").
   */
  var offsetDateRef: Date? = null

  /**
   * The reference date-time of a date-offset.
   */
  lateinit var dateOffsetDateTimeRef: DateTime

  /**
   * The date as unit of an ordinal date (null if the reference is a date unit - e.g. "days", "weeks").
   */
  var ordinalDateUnit: Date? = null

  /**
   * The reference date-time of an ordinal date (can be a [Date] or an [Offset]).
   */
  lateinit var ordinalDateTimeRef: DateTime

  /**
   * The range of token indices related to the currently parsing 'date' rule.
   */
  private lateinit var dateTokens: IntRange

  /**
   * The range of token indices related to the currently parsing 'time' rule.
   */
  private lateinit var timeTokens: IntRange

  /**
   * The range of token indices related to the currently parsing 'date_time_simple' rule.
   */
  private lateinit var dateTimeSimpleTokens: IntRange

  /**
   * The range of token indices related to the currently parsing 'offset' rule.
   */
  private lateinit var offsetTokens: IntRange

  /**
   * The range of token indices related to the currently parsing 'date_offset' rule.
   */
  private lateinit var dateOffsetTokens: IntRange

  /**
   * The range of token indices related to the currently parsing 'date_ordinal' rule.
   */
  private lateinit var dateOrdinalTokens: IntRange

  /**
   *
   */
  fun buildDate(): Date {

    val date = Date(
      startToken = this.dateTokens.start,
      endToken = this.dateTokens.endInclusive,
      day = this.day,
      weekDay = this.weekDay,
      month = this.month,
      year = this.year,
      yearAbbr = this.yearAbbr,
      holiday = this.holiday
    )

    this.day = null
    this.weekDay = null
    this.month = null
    this.year = null
    this.yearAbbr = false
    this.holiday = null

    return date
  }

  /**
   *
   */
  fun buildTime() = Time(
    startToken = this.timeTokens.start,
    endToken = this.timeTokens.endInclusive,
    hour = this.hour,
    min = this.min,
    sec = this.sec,
    millisec = this.millisec,
    generic = this.genericTime,
    timezone = this.timezone
  )

  /**
   *
   */
  fun buildDateTimeSimple() = DateTimeSimple(
    startToken = this.dateTimeSimpleTokens.start,
    endToken = this.dateTimeSimpleTokens.endInclusive,
    date = this.buildDate(),
    time = this.buildTime()
  )

  /**
   *
   */
  fun buildOffset(): Offset {

    val start: Int = this.offsetTokens.start
    val end: Int = this.offsetTokens.endInclusive

    val unitsFactor: Int = if (this.positiveOffset) 1 else -1
    val units: Int = unitsFactor * this.offsetLength

    return this.offsetDateRef?.let {
      Offset.Date(startToken = start, endToken = end, units = units, value = it)
    } ?:
      DateUnit.toOffsetClasses.getValue(this.dateUnit!!).constructors.first().call(start, end, units) as Offset
  }

  /**
   *
   */
  fun buildDateOffset() = DateOffset(
    startToken = this.dateOffsetTokens.start,
    endToken = this.dateOffsetTokens.endInclusive,
    dateTime = this.dateOffsetDateTimeRef,
    offset = this.buildOffset()
  )

  /**
   *
   */
  fun buildDateOrdinal(): DateOrdinal {

    val start: Int = this.dateOrdinalTokens.start
    val end: Int = this.dateOrdinalTokens.endInclusive

    val pos: DateOrdinal.Position = if (this.ordinalPosition > 0)
      DateOrdinal.Position.Ordinal(count = this.ordinalPosition)
    else
      DateOrdinal.Position.Last()

    return this.ordinalDateUnit?.let {
      DateOrdinal.Date(startToken = start, endToken = end, position = pos, dateTime = ordinalDateTimeRef, value = it)
    } ?:
      DateUnit.toDateOrdinalClasses.getValue(this.dateUnit!!).constructors.first()
        .call(start, end, pos, this.ordinalDateTimeRef) as DateOrdinal
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
  fun setDateTimeSimpleTokens(startIndex: Int, endIndex: Int) {

    this.dateTimeSimpleTokens = IntRange(
      this.tokens.indexOfFirst { it.startAt == startIndex },
      this.tokens.indexOfFirst { it.endAt == endIndex }
    )
  }

  /**
   *
   */
  fun setOffsetTokens(startIndex: Int, endIndex: Int) {

    this.offsetTokens = IntRange(
      this.tokens.indexOfFirst { it.startAt == startIndex },
      this.tokens.indexOfFirst { it.endAt == endIndex }
    )
  }

  /**
   *
   */
  fun setDateOffsetTokens(startIndex: Int, endIndex: Int) {

    this.dateOffsetTokens = IntRange(
      this.tokens.indexOfFirst { it.startAt == startIndex },
      this.tokens.indexOfFirst { it.endAt == endIndex }
    )
  }

  /**
   *
   */
  fun setDateOrdinalTokens(startIndex: Int, endIndex: Int) {

    this.dateOrdinalTokens = IntRange(
      this.tokens.indexOfFirst { it.startAt == startIndex },
      this.tokens.indexOfFirst { it.endAt == endIndex }
    )
  }
}
