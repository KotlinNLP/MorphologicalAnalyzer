/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.morphologicalanalyzer.datetime.objects.*
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Date
import com.kotlinnlp.neuraltokenizer.Token
import java.util.*
import kotlin.reflect.KClass

/**
 * The helper to build a [DateTimeSimple].
 *
 * @param tokens the list of tokens that compose the input text.
 */
internal class DateTimeBuilder(private val tokens: List<Token>) {

  /**
   * The type of date unit.
   *
   * @property Hour the hour unit
   * @property Minute the minute unit
   * @property Second the second unit
   * @property Day the day unit
   * @property Week the week unit
   * @property Weekend the weekend unit
   * @property Month the month unit
   * @property Year the year unit
   */
  enum class DateUnitType { Hour, Minute, Second, Day, Week, Weekend, Month, Year }

  companion object {

    /**
     * A map of date unit types to [Offset] k-classes.
     */
    private val dateUnitToOffsetClasses: Map<DateUnitType, KClass<*>> = mapOf(
      DateUnitType.Hour to Offset.Hours::class,
      DateUnitType.Minute to Offset.Minutes::class,
      DateUnitType.Second to Offset.Seconds::class,
      DateUnitType.Day to Offset.Days::class,
      DateUnitType.Week to Offset.Weeks::class,
      DateUnitType.Weekend to Offset.Weekends::class,
      DateUnitType.Month to Offset.Months::class,
      DateUnitType.Year to Offset.Years::class
    )

    /**
     * A map of date unit types to [DateOrdinal] k-classes.
     */
    private val dateUnitToDateOrdinalClasses: Map<DateUnitType, KClass<*>> = mapOf(
      DateUnitType.Day to DateOrdinal.Day::class,
      DateUnitType.Week to DateOrdinal.Week::class,
      DateUnitType.Weekend to DateOrdinal.Weekend::class,
      DateUnitType.Month to DateOrdinal.Month::class,
      DateUnitType.Year to DateOrdinal.Year::class
    )
  }

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
   * The timezone of the currently building date-time.
   */
  var timezone: TimeZone? = null

  /**
   * The date unit type.
   */
  var dateUnit: DateUnitType? = null

  /**
   * Whether the currently building offset is positive (default true).
   */
  var positiveOffset: Boolean = true

  /**
   * The length of the currently building offset (default 1).
   */
  var offsetUnits: Int = 1

  /**
   * The ordinal position of a [DateOrdinal].
   */
  var ordinalPosition: Int = 0

  /**
   * The reference date of an offset (null if the reference is a date unit - e.g. "days", "weeks").
   */
  var offsetDateRef: Date? = null

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
  fun buildOffset(): Offset = this.offsetDateRef?.let {
    Offset.Date(
      startToken = this.offsetTokens.start,
      endToken = this.offsetTokens.endInclusive,
      positive = this.positiveOffset,
      units = this.offsetUnits,
      value = it
    )
  } ?: dateUnitToOffsetClasses.getValue(this.dateUnit!!).constructors.first().call(
    this.offsetTokens.start,
    this.offsetTokens.endInclusive,
    this.positiveOffset,
    this.offsetUnits
  ) as Offset

  /**
   *
   */
  fun buildDateOffset() = DateOffset(
    startToken = this.dateOffsetTokens.start,
    endToken = this.dateOffsetTokens.endInclusive,
    date = this.buildDate(),
    offset = this.buildOffset()
  )

  /**
   *
   */
  fun buildDateOrdinal(): DateOrdinal {

    val pos: DateOrdinal.Position = if (this.ordinalPosition > 0)
      DateOrdinal.Position.Ordinal(count = this.ordinalPosition)
    else
      DateOrdinal.Position.Last()

    return this.ordinalDateUnit?.let {
      DateOrdinal.DateTime(
        startToken = this.dateOrdinalTokens.start,
        endToken = this.dateOrdinalTokens.endInclusive,
        position = pos,
        value = it,
        dateTime = ordinalDateTimeRef
      )
    } ?: dateUnitToDateOrdinalClasses.getValue(this.dateUnit!!).constructors.first().call(
      this.dateOrdinalTokens.start,
      this.dateOrdinalTokens.endInclusive,
      pos,
      ordinalDateTimeRef
    ) as DateOrdinal
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
