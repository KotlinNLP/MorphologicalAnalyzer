/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.linguisticdescription.sentence.token.RealToken
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.*
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.Date
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.intervals.CloseInterval
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.intervals.Interval
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.intervals.OpenFromInterval
import com.kotlinnlp.linguisticdescription.sentence.properties.datetime.intervals.OpenToInterval
import java.util.*

/**
 * The helper to build a [DateTimeSimple].
 *
 * @param tokens the list of tokens that compose the text
 * @param offset the offset of the text in the containing text
 */
internal class DateTimeBuilder(private val tokens: List<RealToken>, private val offset: Int) {

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
  lateinit var dateOffsetDateTimeRef: SingleDateTime

  /**
   * The date as unit of an ordinal date (null if the reference is a date unit - e.g. "days", "weeks").
   */
  var ordinalDateUnit: Date? = null

  /**
   * The reference date-time of an ordinal date (can be a [Date] or an [Offset]).
   */
  lateinit var ordinalDateTimeRef: SingleDateTime

  /**
   * The lower bound date-time of an interval (usually it is a [Date], a [Time] or an [Offset]).
   */
  var intervalFromDateTime: SingleDateTime? = null

  /**
   * The upper bound date-time of an interval (usually it is a [Date], a [Time] or an [Offset]).
   */
  var intervalToDateTime: SingleDateTime? = null

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
   * The range of token indices related to the currently parsing 'interval' rule.
   */
  private lateinit var intervalTokens: IntRange

  /**
   *
   */
  fun buildDate(): Date {

    val date = Date(
      startToken = this.dateTokens.first,
      endToken = this.dateTokens.last,
      startChar = this.tokens[this.dateTokens.first].position.start,
      endChar = this.tokens[this.dateTokens.last].position.end,
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
  fun buildTime(): Time {

    val time = Time(
      startToken = this.timeTokens.first,
      endToken = this.timeTokens.last,
      startChar = this.tokens[this.timeTokens.first].position.start,
      endChar = this.tokens[this.timeTokens.last].position.end,
      hour = this.hour,
      min = this.min,
      sec = this.sec,
      millisec = this.millisec,
      generic = this.genericTime,
      timezone = this.timezone
    )

    this.hour = null
    this.min = null
    this.sec = null
    this.millisec = null
    this.genericTime = null
    this.timezone = null

    return time
  }

  /**
   *
   */
  fun buildDateTimeSimple() = DateTimeSimple(
    startToken = this.dateTimeSimpleTokens.first,
    endToken = this.dateTimeSimpleTokens.last,
    startChar = this.tokens[this.dateTimeSimpleTokens.first].position.start,
    endChar = this.tokens[this.dateTimeSimpleTokens.last].position.end,
    date = this.buildDate(),
    time = this.buildTime()
  )

  /**
   *
   */
  fun buildOffset(): Offset {

    val startTk: Int = this.offsetTokens.first
    val endTk: Int = this.offsetTokens.last

    val startCh: Int = this.tokens[startTk].position.start
    val endCh: Int = this.tokens[endTk].position.end

    val unitsFactor: Int = if (this.positiveOffset) 1 else -1
    val units: Int = unitsFactor * this.offsetLength

    val offset: Offset = this.offsetDateRef?.let {
      Offset.Date(
        startToken = startTk,
        endToken = endTk,
        startChar = startCh,
        endChar = endCh,
        units = units,
        value = it)
    } ?: DateUnit.toOffsetClasses.getValue(this.dateUnit!!).constructors.first()
      .call(startTk, endTk, startCh, endCh, units) as Offset

    this.offsetDateRef = null
    this.dateUnit = null

    return offset
  }

  /**
   *
   */
  fun buildDateOffset() = DateOffset(
    startToken = this.dateOffsetTokens.first,
    endToken = this.dateOffsetTokens.last,
    startChar = this.tokens[this.dateOffsetTokens.first].position.start,
    endChar = this.tokens[this.dateOffsetTokens.last].position.end,
    dateTime = this.dateOffsetDateTimeRef,
    offset = this.buildOffset()
  )

  /**
   *
   */
  fun buildDateOrdinal(): DateOrdinal {

    val startTk: Int = this.dateOrdinalTokens.first
    val endTk: Int = this.dateOrdinalTokens.last

    val startCh: Int = this.tokens[startTk].position.start
    val endCh: Int = this.tokens[endTk].position.end

    val pos: DateOrdinal.Position = if (this.ordinalPosition > 0)
      DateOrdinal.Position.Ordinal(count = this.ordinalPosition)
    else
      DateOrdinal.Position.Last()

    return this.ordinalDateUnit?.let {
      DateOrdinal.Date(
        startToken = startTk,
        endToken = endTk,
        startChar = startCh,
        endChar = endCh,
        position = pos,
        dateTime = ordinalDateTimeRef,
        value = it)
    } ?: DateUnit.toDateOrdinalClasses.getValue(this.dateUnit!!).constructors.first()
      .call(startTk, endTk, startCh, endCh, pos, this.ordinalDateTimeRef) as DateOrdinal
  }

  /**
   *
   */
  fun buildInterval(): Interval {

    val startTk: Int = this.intervalTokens.first
    val endTk: Int = this.intervalTokens.last

    val startCh: Int = this.tokens[startTk].position.start
    val endCh: Int = this.tokens[endTk].position.end

    val from: SingleDateTime? = this.intervalFromDateTime
    val to: SingleDateTime? = this.intervalToDateTime

    return when {
      from != null && to != null -> CloseInterval(
        startToken = startTk, endToken = endTk, startChar = startCh, endChar = endCh, from = from, to = to)
      from != null -> OpenToInterval(
        startToken = startTk, endToken = endTk, startChar = startCh, endChar = endCh, from = from)
      to != null -> OpenFromInterval(
        startToken = startTk, endToken = endTk, startChar = startCh, endChar = endCh, to = to)
      else -> throw RuntimeException("Missing 'from' or 'to' date-time to build an interval.")
    }
  }

  /**
   * @param startIndex the start char index of a date found
   * @param endIndex the end char index of a date found
   */
  fun setDateTokens(startIndex: Int, endIndex: Int) {

    this.dateTokens = this.getTokensRange(startIndex = startIndex, endIndex = endIndex)
  }

  /**
   * @param startIndex the start char index of a time found
   * @param endIndex the end char index of a time found
   */
  fun setTimeTokens(startIndex: Int, endIndex: Int) {

    this.timeTokens = this.getTokensRange(startIndex = startIndex, endIndex = endIndex)
  }

  /**
   * @param startIndex the start char index of a date-time-simple found
   * @param endIndex the end char index of a date-time-simple found
   */
  fun setDateTimeSimpleTokens(startIndex: Int, endIndex: Int) {

    this.dateTimeSimpleTokens = this.getTokensRange(startIndex = startIndex, endIndex = endIndex)
  }

  /**
   * @param startIndex the start char index of an offset found
   * @param endIndex the end char index of an offset found
   */
  fun setOffsetTokens(startIndex: Int, endIndex: Int) {

    this.offsetTokens = this.getTokensRange(startIndex = startIndex, endIndex = endIndex)
  }

  /**
   * @param startIndex the start char index of a date-offset found
   * @param endIndex the end char index of a date-offset found
   */
  fun setDateOffsetTokens(startIndex: Int, endIndex: Int) {

    this.dateOffsetTokens = this.getTokensRange(startIndex = startIndex, endIndex = endIndex)
  }

  /**
   * @param startIndex the start char index of a date-ordinal found
   * @param endIndex the end char index of a date-ordinal found
   */
  fun setDateOrdinalTokens(startIndex: Int, endIndex: Int) {

    this.dateOrdinalTokens = this.getTokensRange(startIndex = startIndex, endIndex = endIndex)
  }

  /**
   * @param startIndex the start char index of an interval found
   * @param endIndex the end char index of an interval found
   */
  fun setIntervalTokens(startIndex: Int, endIndex: Int) {

    this.intervalTokens = this.getTokensRange(startIndex = startIndex, endIndex = endIndex)
  }

  /**
   * @param startIndex the start char index of a token in the text
   * @param endIndex the end char index of a token in the text
   *
   * @return the range of tokens indices that include the given start and end chars
   */
  private fun getTokensRange(startIndex: Int, endIndex: Int): IntRange = IntRange(
    this.tokens.indexOfFirst { (it.position.end - this.offset) >= startIndex },
    this.tokens.indexOfFirst { (it.position.end - this.offset) >= endIndex }
  )
}
