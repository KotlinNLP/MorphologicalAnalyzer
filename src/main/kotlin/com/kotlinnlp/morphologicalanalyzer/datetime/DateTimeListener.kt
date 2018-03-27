/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.DateTimeBaseListener
import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.DateTimeParser
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Date
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTime
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.SingleDateTime
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Time
import com.kotlinnlp.morphologicalanalyzer.datetime.utils.DateUnit
import com.kotlinnlp.neuraltokenizer.Token
import java.util.*

/**
 * An event listener for an ANTRL DateTime Parser.
 */
@Suppress("FunctionName") // for Java overridden methods
internal class DateTimeListener(private val tokens: List<Token>) : DateTimeBaseListener() {

  /**
   * The list of date-times recognized.
   */
  private val dateTimes: MutableList<DateTime> = mutableListOf()

  /**
   * The builder helper of a [DateTime].
   */
  private lateinit var dateTimeBuilder: DateTimeBuilder

  /**
   * The last string number parsed (ordinal or cardinal).
   * It is set when an 'exit ns_*' event is triggered.
   */
  private var strNumber: Int = -1

  /**
   * Whether in the currently building date-time there is an 'interval'.
   */
  private var isInterval: Boolean = false

  /**
   * Whether in the currently building date-time there is a 'date_ordinal'.
   */
  private var isDateOrdinal: Boolean = false

  /**
   * Whether in the currently building date-time there is an 'offset'.
   */
  private var isOffset: Boolean = false

  /**
   * Whether in the currently building date-time there is a 'date_offset'.
   */
  private var isDateOffset: Boolean = false

  /**
   * Whether in the currently building date-time there is a 'date_time_simple'.
   */
  private var isDateTimeSimple: Boolean = false

  /**
   * Whether in the currently building date-time there is a 'date'.
   */
  private var isDate: Boolean = false

  /**
   * Whether in the currently building date-time there is a 'time'.
   */
  private var isTime: Boolean = false

  /**
   * @return the list of date-times recognized during the last parsing
   */
  fun getDateTimes(): List<DateTime> = this.dateTimes

  /**
   * The listener of the 'enter root' event.
   *
   * @param ctx the context of the 'root' rule that is being parsed
   */
  override fun enterRoot(ctx: DateTimeParser.RootContext?) {

    this.dateTimes.clear()
  }

  /**
   * The listener of the 'enter datetime' event.
   *
   * @param ctx the context of the 'datetime' rule that is being parsed
   */
  override fun enterDatetime(ctx: DateTimeParser.DatetimeContext) {

    this.dateTimeBuilder = DateTimeBuilder(this.tokens)
  }

  /**
   * The listener of the 'enter interval' event.
   *
   * @param ctx the context of the 'interval' rule that is being parsed
   */
  override fun enterInterval(ctx: DateTimeParser.IntervalContext) {

    this.isInterval = true
  }

  /**
   * The listener of the 'enter date_ordinal' event.
   *
   * @param ctx the context of the 'date_ordinal' rule that is being parsed
   */
  override fun enterDate_ordinal(ctx: DateTimeParser.Date_ordinalContext) {

    this.isDateOrdinal = true
  }

  /**
   * The listener of the 'enter offset' event.
   *
   * @param ctx the context of the 'offset' rule that is being parsed
   */
  override fun enterOffset(ctx: DateTimeParser.OffsetContext) {

    this.isOffset = true
  }

  /**
   * The listener of the 'enter date_offset' event.
   *
   * @param ctx the context of the 'date_offset' rule that is being parsed
   */
  override fun enterDate_offset(ctx: DateTimeParser.Date_offsetContext) {

    this.isDateOffset = true
  }

  /**
   * The listener of the 'enter date_time_simple' event.
   *
   * @param ctx the context of the 'date_time_simple' rule that is being parsed
   */
  override fun enterDate_time_simple(ctx: DateTimeParser.Date_time_simpleContext) {

    this.isDateTimeSimple = true
  }

  /**
   * The listener of the 'enter date' event.
   *
   * @param ctx the context of the 'date' rule that is being parsed
   */
  override fun enterDate(ctx: DateTimeParser.DateContext) {

    this.isDate = true
  }

  /**
   * The listener of the 'enter time' event.
   *
   * @param ctx the context of the 'time' rule that is being parsed
   */
  override fun enterTime(ctx: DateTimeParser.TimeContext) {

    this.isTime = true
  }

  /**
   * The listener of the 'exit datetime' event.
   *
   * @param ctx the context of the 'datetime' rule just parsed
   */
  override fun exitDatetime(ctx: DateTimeParser.DatetimeContext) {

    this.dateTimes.add(
      when { // the order of the conditions is very important!
        this.isInterval -> this.dateTimeBuilder.buildInterval()
        else -> this.buildSingleDateTime()
      }
    )

    this.isInterval = false
  }

  /**
   * @return a single date-time object just parsed
   */
  private fun buildSingleDateTime(): SingleDateTime {

    val dateTime: SingleDateTime = when { // the order of the conditions is very important!
      this.isDateOrdinal -> this.dateTimeBuilder.buildDateOrdinal()
      this.isDateOffset -> this.dateTimeBuilder.buildDateOffset()
      this.isOffset -> this.dateTimeBuilder.buildOffset()
      this.isDateTimeSimple -> this.dateTimeBuilder.buildDateTimeSimple()
      this.isDate -> this.dateTimeBuilder.buildDate()
      this.isTime -> this.dateTimeBuilder.buildTime()
      else -> throw RuntimeException("Expected single date-time object but no 'enter' event has been triggered.")
    }

    this.isDateOrdinal = false
    this.isDateOffset = false
    this.isOffset = false
    this.isDateTimeSimple = false
    this.isDate = false
    this.isTime = false

    return dateTime
  }

  /**
   * The listener of the 'exit date' event.
   *
   * @param ctx the context of the 'date' rule just parsed
   */
  override fun exitDate(ctx: DateTimeParser.DateContext) {

    this.dateTimeBuilder.setDateTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit time' event.
   *
   * @param ctx the context of the 'time' rule just parsed
   */
  override fun exitTime(ctx: DateTimeParser.TimeContext) {

    this.dateTimeBuilder.setTimeTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit date_time_simple' event.
   *
   * @param ctx the context of the 'date_time_simple' rule that is being parsed
   */
  override fun exitDate_time_simple(ctx: DateTimeParser.Date_time_simpleContext) {

    this.dateTimeBuilder.setDateTimeSimpleTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit offset' event.
   *
   * @param ctx the context of the 'offset' rule that is being parsed
   */
  override fun exitOffset(ctx: DateTimeParser.OffsetContext) {

    this.dateTimeBuilder.setOffsetTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit date_offset' event.
   *
   * @param ctx the context of the 'date_offset' rule that is being parsed
   */
  override fun exitDate_offset(ctx: DateTimeParser.Date_offsetContext) {

    this.dateTimeBuilder.setDateOffsetTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit date_ordinal' event.
   *
   * @param ctx the context of the 'date_ordinal' rule that is being parsed
   */
  override fun exitDate_ordinal(ctx: DateTimeParser.Date_ordinalContext) {

    this.dateTimeBuilder.setDateOrdinalTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit interval' event.
   *
   * @param ctx the context of the 'interval' rule that is being parsed
   */
  override fun exitInterval(ctx: DateTimeParser.IntervalContext) {

    this.dateTimeBuilder.setIntervalTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit interval_datetime_from' event.
   *
   * @param ctx the context of the 'interval_datetime_from' rule that is being parsed
   */
  override fun exitInterval_datetime_from(ctx: DateTimeParser.Interval_datetime_fromContext) {

    this.dateTimeBuilder.intervalFromDateTime = this.buildSingleDateTime()
  }

  /**
   * The listener of the 'exit interval_datetime_to' event.
   *
   * @param ctx the context of the 'interval_datetime_to' rule that is being parsed
   */
  override fun exitInterval_datetime_to(ctx: DateTimeParser.Interval_datetime_toContext) {

    this.dateTimeBuilder.intervalToDateTime = this.buildSingleDateTime()
  }

  /**
   * The listener of the 'exit interval_offset_from' event.
   *
   * @param ctx the context of the 'interval_offset_from' rule that is being parsed
   */
  override fun exitInterval_offset_from(ctx: DateTimeParser.Interval_offset_fromContext) {

    this.isOffset = true

    this.dateTimeBuilder.positiveOffset = false
    this.dateTimeBuilder.setOffsetTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit interval_offset_to' event.
   *
   * @param ctx the context of the 'interval_offset_to' rule that is being parsed
   */
  override fun exitInterval_offset_to(ctx: DateTimeParser.Interval_offset_toContext) {

    this.isOffset = true

    this.dateTimeBuilder.positiveOffset = true
    this.dateTimeBuilder.setOffsetTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit date_time_simple_generic_time' event.
   *
   * @param ctx the context of the 'date_time_simple_generic_time' rule that is being parsed
   */
  override fun exitDate_time_simple_generic_time(ctx: DateTimeParser.Date_time_simple_generic_timeContext) {

    this.dateTimeBuilder.setTimeTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit offset_date_ref' event.
   *
   * @param ctx the context of the 'offset_date_ref' rule that is being parsed
   */
  override fun exitOffset_date_ref(ctx: DateTimeParser.Offset_date_refContext) {

    this.dateTimeBuilder.offsetDateRef = this.dateTimeBuilder.buildDate()
  }

  /**
   * The listener of the 'exit date_offset_date_ref' event.
   *
   * @param ctx the context of the 'date_offset_date_ref' rule that is being parsed
   */
  override fun exitDate_offset_date_ref(ctx: DateTimeParser.Date_offset_date_refContext) {

    this.dateTimeBuilder.dateOffsetDateTimeRef = this.dateTimeBuilder.buildDate()
  }

  /**
   * The listener of the 'exit date_offset_time_ref' event.
   *
   * @param ctx the context of the 'date_offset_time_ref' rule that is being parsed
   */
  override fun exitDate_offset_time_ref(ctx: DateTimeParser.Date_offset_time_refContext) {

    this.dateTimeBuilder.setTimeTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)

    this.dateTimeBuilder.dateOffsetDateTimeRef = this.dateTimeBuilder.buildTime()
  }

  /**
   * The listener of the 'exit date_offset_time' event.
   *
   * @param ctx the context of the 'date_offset_time' rule that is being parsed
   */
  override fun exitDate_offset_time(ctx: DateTimeParser.Date_offset_timeContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Day
    this.dateTimeBuilder.setOffsetTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
  }

  /**
   * The listener of the 'exit date_offset_tonight' event.
   *
   * @param ctx the context of the 'date_offset_tonight' rule that is being parsed
   */
  override fun exitDate_offset_tonight(ctx: DateTimeParser.Date_offset_tonightContext) {

    val start: Int = ctx.start.startIndex
    val end: Int = ctx.stop.stopIndex

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Day
    this.dateTimeBuilder.offsetLength = 0
    this.dateTimeBuilder.setOffsetTokens(startIndex = start, endIndex = end)

    this.dateTimeBuilder.genericTime = Time.Generic.Night
    this.dateTimeBuilder.setTimeTokens(startIndex = start, endIndex = end)

    this.dateTimeBuilder.dateOffsetDateTimeRef = this.dateTimeBuilder.buildTime()
  }

  /**
   * The listener of the 'exit ordinal_date_ref' event.
   *
   * @param ctx the context of the 'ordinal_date_ref' rule that is being parsed
   */
  override fun exitOrdinal_date_ref(ctx: DateTimeParser.Ordinal_date_refContext) {

    this.dateTimeBuilder.ordinalDateTimeRef = this.dateTimeBuilder.buildDate()
  }

  /**
   * The listener of the 'exit ordinal_offset_ref' event.
   *
   * @param ctx the context of the 'ordinal_offset_ref' rule that is being parsed
   */
  override fun exitOrdinal_offset_ref(ctx: DateTimeParser.Ordinal_offset_refContext) {

    this.dateTimeBuilder.ordinalDateTimeRef = this.dateTimeBuilder.buildOffset()
  }

  /**
   * The listener of the 'exit ordinal_day_week_unit' event.
   *
   * @param ctx the context of the 'ordinal_day_week_unit' rule that is being parsed
   */
  override fun exitOrdinal_day_week_unit(ctx: DateTimeParser.Ordinal_day_week_unitContext) {

    this.dateTimeBuilder.setDateTokens(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
    this.dateTimeBuilder.ordinalDateUnit = this.dateTimeBuilder.buildDate()
  }

  /**
   * The listener of the 'exit offset_single_zero_prefix' event.
   *
   * @param ctx the context of the 'offset_single_zero_prefix' rule that is being parsed
   */
  override fun exitOffset_single_zero_prefix(ctx: DateTimeParser.Offset_single_zero_prefixContext) {

    this.dateTimeBuilder.offsetLength = 0
    this.dateTimeBuilder.positiveOffset = true
  }

  /**
   * The listener of the 'exit offset_single_pos_prefix' event.
   *
   * @param ctx the context of the 'offset_single_pos_prefix' rule that is being parsed
   */
  override fun exitOffset_single_pos_prefix(ctx: DateTimeParser.Offset_single_pos_prefixContext) {

    this.dateTimeBuilder.offsetLength = 1
    this.dateTimeBuilder.positiveOffset = true
  }

  /**
   * The listener of the 'exit offset_single_neg_prefix' event.
   *
   * @param ctx the context of the 'offset_single_neg_prefix' rule that is being parsed
   */
  override fun exitOffset_single_neg_prefix(ctx: DateTimeParser.Offset_single_neg_prefixContext) {

    this.dateTimeBuilder.offsetLength = 1
    this.dateTimeBuilder.positiveOffset = false
  }

  /**
   * The listener of the 'exit offset_double_pos_suffix' event.
   *
   * @param ctx the context of the 'offset_double_pos_suffix' rule that is being parsed
   */
  override fun exitOffset_double_pos_suffix(ctx: DateTimeParser.Offset_double_pos_suffixContext) {

    this.dateTimeBuilder.offsetLength = 2
    this.dateTimeBuilder.positiveOffset = true
  }

  /**
   * The listener of the 'exit offset_double_neg_suffix' event.
   *
   * @param ctx the context of the 'offset_double_neg_suffix' rule that is being parsed
   */
  override fun exitOffset_double_neg_suffix(ctx: DateTimeParser.Offset_double_neg_suffixContext) {

    this.dateTimeBuilder.offsetLength = 2
    this.dateTimeBuilder.positiveOffset = false
  }

  /**
   * The listener of the 'exit offset_neg_suffix' event.
   *
   * @param ctx the context of the 'offset_neg_suffix' rule that is being parsed
   */
  override fun exitOffset_neg_suffix(ctx: DateTimeParser.Offset_neg_suffixContext) {

    this.dateTimeBuilder.positiveOffset = false
  }

  /**
   * The listener of the 'exit datetime_utc' event.
   *
   * @param ctx the context of the 'datetime_utc' rule just parsed
   */
  override fun exitDatetime_utc(ctx: DateTimeParser.Datetime_utcContext) {

    this.dateTimeBuilder.timezone = TimeZone.getTimeZone("UTC")
  }

  /**
   * The listener of the 'exit sec_lit' event.
   *
   * @param ctx the context of the 'sec_lit' rule just parsed
   */
  override fun exitSec_lit(ctx: DateTimeParser.Sec_litContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Second
  }

  /**
   * The listener of the 'exit min_lit' event.
   *
   * @param ctx the context of the 'min_lit' rule just parsed
   */
  override fun exitMin_lit(ctx: DateTimeParser.Min_litContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Minute
  }

  /**
   * The listener of the 'exit hour_lit' event.
   *
   * @param ctx the context of the 'hour_lit' rule just parsed
   */
  override fun exitHour_lit(ctx: DateTimeParser.Hour_litContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Hour
  }

  /**
   * The listener of the 'exit quarter_hour_lit' event.
   *
   * @param ctx the context of the 'quarter_hour_lit' rule just parsed
   */
  override fun exitQuarter_hour_lit(ctx: DateTimeParser.Quarter_hour_litContext) {

    this.dateTimeBuilder.min = 15
    this.dateTimeBuilder.dateUnit = DateUnit.Type.QuarterHour
  }

  /**
   * The listener of the 'exit half_hour_lit' event.
   *
   * @param ctx the context of the 'half_hour_lit' rule just parsed
   */
  override fun exitHalf_hour_lit(ctx: DateTimeParser.Half_hour_litContext) {

    this.dateTimeBuilder.min = 30
    this.dateTimeBuilder.dateUnit = DateUnit.Type.HalfHour
  }

  /**
   * The listener of the 'exit day_lit' event.
   *
   * @param ctx the context of the 'day_lit' rule just parsed
   */
  override fun exitDay_lit(ctx: DateTimeParser.Day_litContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Day
  }

  /**
   * The listener of the 'exit week_lit' event.
   *
   * @param ctx the context of the 'week_lit' rule just parsed
   */
  override fun exitWeek_lit(ctx: DateTimeParser.Week_litContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Week
  }

  /**
   * The listener of the 'exit weekend_lit' event.
   *
   * @param ctx the context of the 'weekend_lit' rule just parsed
   */
  override fun exitWeekend_lit(ctx: DateTimeParser.Weekend_litContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Weekend
  }

  /**
   * The listener of the 'exit month_lit' event.
   *
   * @param ctx the context of the 'month_lit' rule just parsed
   */
  override fun exitMonth_lit(ctx: DateTimeParser.Month_litContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Month
  }

  /**
   * The listener of the 'exit year_lit' event.
   *
   * @param ctx the context of the 'year_lit' rule just parsed
   */
  override fun exitYear_lit(ctx: DateTimeParser.Year_litContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Year
  }

  /**
   * The listener of the 'exit year_num' event.
   *
   * @param ctx the context of the 'year_num' rule just parsed
   */
  override fun exitYear_num(ctx: DateTimeParser.Year_numContext) {

    this.dateTimeBuilder.year = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit year_full' event.
   *
   * @param ctx the context of the 'year_full' rule just parsed
   */
  override fun exitYear_full(ctx: DateTimeParser.Year_fullContext) {

    this.dateTimeBuilder.year = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit year_modern' event.
   *
   * @param ctx the context of the 'year_modern' rule just parsed
   */
  override fun exitYear_modern(ctx: DateTimeParser.Year_modernContext) {

    this.dateTimeBuilder.year = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit year_abbr' event.
   *
   * @param ctx the context of the 'year_abbr' rule just parsed
   */
  override fun exitYear_abbr(ctx: DateTimeParser.Year_abbrContext) {

    this.dateTimeBuilder.year = ctx.text.toInt()
    this.dateTimeBuilder.yearAbbr = true
  }

  /**
   * The listener of the 'exit year_APEX' event.
   *
   * @param ctx the context of the 'year_APEX' rule just parsed
   */
  override fun exitYear_APEX(ctx: DateTimeParser.Year_APEXContext) {

    val numYear: Int = ctx.text.trim('\'', '’').toInt()

    this.dateTimeBuilder.year = numYear
    this.dateTimeBuilder.yearAbbr = true
  }

  /**
   * The listener of the 'exit month_num' event.
   *
   * @param ctx the context of the 'month_num' rule just parsed
   */
  override fun exitMonth_num(ctx: DateTimeParser.Month_numContext) {

    this.dateTimeBuilder.month = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit month_jan' event.
   *
   * @param ctx the context of the 'month_jan' rule just parsed
   */
  override fun exitMonth_jan(ctx: DateTimeParser.Month_janContext) {

    this.dateTimeBuilder.month = 1
  }

  /**
   * The listener of the 'exit month_feb' event.
   *
   * @param ctx the context of the 'month_feb' rule just parsed
   */
  override fun exitMonth_feb(ctx: DateTimeParser.Month_febContext) {

    this.dateTimeBuilder.month = 2
  }

  /**
   * The listener of the 'exit month_mar' event.
   *
   * @param ctx the context of the 'month_mar' rule just parsed
   */
  override fun exitMonth_mar(ctx: DateTimeParser.Month_marContext) {

    this.dateTimeBuilder.month = 3
  }

  /**
   * The listener of the 'exit month_apr' event.
   *
   * @param ctx the context of the 'month_apr' rule just parsed
   */
  override fun exitMonth_apr(ctx: DateTimeParser.Month_aprContext) {

    this.dateTimeBuilder.month = 4
  }

  /**
   * The listener of the 'exit month_may' event.
   *
   * @param ctx the context of the 'month_may' rule just parsed
   */
  override fun exitMonth_may(ctx: DateTimeParser.Month_mayContext) {

    this.dateTimeBuilder.month = 5
  }

  /**
   * The listener of the 'exit month_jun' event.
   *
   * @param ctx the context of the 'month_jun' rule just parsed
   */
  override fun exitMonth_jun(ctx: DateTimeParser.Month_junContext) {

    this.dateTimeBuilder.month = 6
  }

  /**
   * The listener of the 'exit month_jul' event.
   *
   * @param ctx the context of the 'month_jul' rule just parsed
   */
  override fun exitMonth_jul(ctx: DateTimeParser.Month_julContext) {

    this.dateTimeBuilder.month = 7
  }

  /**
   * The listener of the 'exit month_aug' event.
   *
   * @param ctx the context of the 'month_aug' rule just parsed
   */
  override fun exitMonth_aug(ctx: DateTimeParser.Month_augContext) {

    this.dateTimeBuilder.month = 8
  }

  /**
   * The listener of the 'exit month_sep' event.
   *
   * @param ctx the context of the 'month_sep' rule just parsed
   */
  override fun exitMonth_sep(ctx: DateTimeParser.Month_sepContext) {

    this.dateTimeBuilder.month = 9
  }

  /**
   * The listener of the 'exit month_oct' event.
   *
   * @param ctx the context of the 'month_oct' rule just parsed
   */
  override fun exitMonth_oct(ctx: DateTimeParser.Month_octContext) {

    this.dateTimeBuilder.month = 10
  }

  /**
   * The listener of the 'exit month_nov' event.
   *
   * @param ctx the context of the 'month_nov' rule just parsed
   */
  override fun exitMonth_nov(ctx: DateTimeParser.Month_novContext) {

    this.dateTimeBuilder.month = 11
  }

  /**
   * The listener of the 'exit month_dec' event.
   *
   * @param ctx the context of the 'month_dec' rule just parsed
   */
  override fun exitMonth_dec(ctx: DateTimeParser.Month_decContext) {

    this.dateTimeBuilder.month = 11
  }

  /**
   * The listener of the 'exit day_num_canonical' event.
   *
   * @param ctx the context of the 'day_num_canonical' rule just parsed
   */
  override fun exitDay_num_canonical(ctx: DateTimeParser.Day_num_canonicalContext) {

    this.dateTimeBuilder.day = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit day_str' event.
   * It is called just after a string number has been parsed.
   *
   * @param ctx the context of the 'day_str' rule just parsed
   */
  override fun exitDay_str(ctx: DateTimeParser.Day_strContext) {

    this.dateTimeBuilder.day = this.strNumber
  }

  /**
   * The listener of the 'exit day_num' event.
   *
   * @param ctx the context of the 'day_num' rule just parsed
   */
  override fun exitDay_num(ctx: DateTimeParser.Day_numContext) {

    this.dateTimeBuilder.day = ctx.text.takeWhile { it.isDigit() }.toInt() // e.g. trim the non-digit part of '21st'
  }

  /**
   * The listener of the 'exit day_num_th' event.
   *
   * @param ctx the context of the 'day_num_th' rule just parsed
   */
  override fun exitDay_num_th(ctx: DateTimeParser.Day_num_thContext) {

    this.dateTimeBuilder.day = ctx.text.takeWhile { it.isDigit() }.toInt() // e.g. trim the non-digit part of '21st'
  }

  /**
   * The listener of the 'exit day_mon' event.
   *
   * @param ctx the context of the 'day_mon' rule just parsed
   */
  override fun exitDay_mon(ctx: DateTimeParser.Day_monContext) {

    this.dateTimeBuilder.weekDay = 1
  }

  /**
   * The listener of the 'exit day_tue' event.
   *
   * @param ctx the context of the 'day_tue' rule just parsed
   */
  override fun exitDay_tue(ctx: DateTimeParser.Day_tueContext) {

    this.dateTimeBuilder.weekDay = 2
  }

  /**
   * The listener of the 'exit day_wed' event.
   *
   * @param ctx the context of the 'day_wed' rule just parsed
   */
  override fun exitDay_wed(ctx: DateTimeParser.Day_wedContext) {

    this.dateTimeBuilder.weekDay = 3
  }

  /**
   * The listener of the 'exit day_thu' event.
   *
   * @param ctx the context of the 'day_thu' rule just parsed
   */
  override fun exitDay_thu(ctx: DateTimeParser.Day_thuContext) {

    this.dateTimeBuilder.weekDay = 4
  }

  /**
   * The listener of the 'exit day_fri' event.
   *
   * @param ctx the context of the 'day_fri' rule just parsed
   */
  override fun exitDay_fri(ctx: DateTimeParser.Day_friContext) {

    this.dateTimeBuilder.weekDay = 5
  }

  /**
   * The listener of the 'exit day_sat' event.
   *
   * @param ctx the context of the 'day_sat' rule just parsed
   */
  override fun exitDay_sat(ctx: DateTimeParser.Day_satContext) {

    this.dateTimeBuilder.weekDay = 6
  }

  /**
   * The listener of the 'exit day_sun' event.
   *
   * @param ctx the context of the 'day_sun' rule just parsed
   */
  override fun exitDay_sun(ctx: DateTimeParser.Day_sunContext) {

    this.dateTimeBuilder.weekDay = 7
  }

  /**
   * The listener of the 'exit christmas' event.
   *
   * @param ctx the context of the 'christmas' rule just parsed
   */
  override fun exitChristmas(ctx: DateTimeParser.ChristmasContext) {

    this.dateTimeBuilder.day = 25
    this.dateTimeBuilder.month = 12
    this.dateTimeBuilder.holiday = Date.Holiday.Christmas
  }

  /**
   * The listener of the 'exit christmas_eve' event.
   *
   * @param ctx the context of the 'christmas_eve' rule just parsed
   */
  override fun exitChristmas_eve(ctx: DateTimeParser.Christmas_eveContext) {

    this.dateTimeBuilder.day = 24
    this.dateTimeBuilder.month = 12
    this.dateTimeBuilder.holiday = Date.Holiday.ChristmasEve
  }

  /**
   * The listener of the 'exit easter' event.
   *
   * @param ctx the context of the 'easter' rule just parsed
   */
  override fun exitEaster(ctx: DateTimeParser.EasterContext) {

    this.dateTimeBuilder.holiday = Date.Holiday.Easter
  }

  /**
   * The listener of the 'exit time_suffix' event.
   *
   * @param ctx the context of the 'time_suffix' rule just parsed
   */
  override fun exitTime_suffix(ctx: DateTimeParser.Time_suffixContext) {

    this.dateTimeBuilder.hour?.let {
      when (ctx.text.toLowerCase()) {
        "am" -> if (it == 12) this.dateTimeBuilder.hour = 0
        "pm" -> if (it <= 11) this.dateTimeBuilder.hour = it + 12
      }
    }
  }

  /**
   * The listener of the 'exit hour_str' event.
   *
   * @param ctx the context of the 'hour_str' rule just parsed
   */
  override fun exitHour_str(ctx: DateTimeParser.Hour_strContext) {

    this.dateTimeBuilder.hour = this.strNumber
  }

  /**
   * The listener of the 'exit hour' event.
   *
   * @param ctx the context of the 'hour' rule just parsed
   */
  override fun exitHour(ctx: DateTimeParser.HourContext) {

    this.dateTimeBuilder.hour = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit hour_00' event.
   *
   * @param ctx the context of the 'hour_00' rule just parsed
   */
  override fun exitHour_00(ctx: DateTimeParser.Hour_00Context) {

    this.dateTimeBuilder.hour = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit half_hour' event.
   *
   * @param ctx the context of the 'half_hour' rule just parsed
   */
  override fun exitHalf_hour(ctx: DateTimeParser.Half_hourContext) {

    this.dateTimeBuilder.min = 30
  }

  /**
   * The listener of the 'exit quarter_hour' event.
   *
   * @param ctx the context of the 'quarter_hour' rule just parsed
   */
  override fun exitQuarter_hour(ctx: DateTimeParser.Quarter_hourContext) {

    this.dateTimeBuilder.min = 15
  }

  /**
   * The listener of the 'exit three_quarters_hour' event.
   *
   * @param ctx the context of the 'three_quarters_hour' rule just parsed
   */
  override fun exitThree_quarters_hour(ctx: DateTimeParser.Three_quarters_hourContext) {

    this.dateTimeBuilder.min = 45
  }

  /**
   * The listener of the 'exit min' event.
   *
   * @param ctx the context of the 'min' rule just parsed
   */
  override fun exitMin(ctx: DateTimeParser.MinContext) {

    this.dateTimeBuilder.min = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit min_00' event.
   *
   * @param ctx the context of the 'min_00' rule just parsed
   */
  override fun exitMin_00(ctx: DateTimeParser.Min_00Context) {

    this.dateTimeBuilder.min = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit sec_00' event.
   *
   * @param ctx the context of the 'sec_00' rule just parsed
   */
  override fun exitSec_00(ctx: DateTimeParser.Sec_00Context) {

    this.dateTimeBuilder.sec = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit sec' event.
   *
   * @param ctx the context of the 'sec' rule just parsed
   */
  override fun exitSec(ctx: DateTimeParser.SecContext) {
    this.dateTimeBuilder.sec = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit millisec' event.
   *
   * @param ctx the context of the 'millisec' rule just parsed
   */
  override fun exitMillisec(ctx: DateTimeParser.MillisecContext) {
    this.dateTimeBuilder.millisec = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit millisec_000' event.
   *
   * @param ctx the context of the 'millisec_000' rule just parsed
   */
  override fun exitMillisec_000(ctx: DateTimeParser.Millisec_000Context) {
    this.dateTimeBuilder.millisec = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit morning' event.
   *
   * @param ctx the context of the 'morning' rule just parsed
   */
  override fun exitMorning(ctx: DateTimeParser.MorningContext) {

    this.dateTimeBuilder.genericTime = Time.Generic.Morning
  }

  /**
   * The listener of the 'exit noon' event.
   *
   * @param ctx the context of the 'noon' rule just parsed
   */
  override fun exitNoon(ctx: DateTimeParser.NoonContext) {

    this.dateTimeBuilder.genericTime = Time.Generic.Noon
  }

  /**
   * The listener of the 'exit afternoon' event.
   *
   * @param ctx the context of the 'afternoon' rule just parsed
   */
  override fun exitAfternoon(ctx: DateTimeParser.AfternoonContext) {

    this.dateTimeBuilder.genericTime = Time.Generic.Afternoon
  }

  /**
   * The listener of the 'exit evening' event.
   *
   * @param ctx the context of the 'evening' rule just parsed
   */
  override fun exitEvening(ctx: DateTimeParser.EveningContext) {

    this.dateTimeBuilder.genericTime = Time.Generic.Evening
  }

  /**
   * The listener of the 'exit night' event.
   *
   * @param ctx the context of the 'night' rule just parsed
   */
  override fun exitNight(ctx: DateTimeParser.NightContext) {

    this.dateTimeBuilder.genericTime = Time.Generic.Night
  }

  /**
   * The listener of the 'exit now' event.
   *
   * @param ctx the context of the 'now' rule just parsed
   */
  override fun exitNow(ctx: DateTimeParser.NowContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Second
    this.dateTimeBuilder.offsetLength = 0
  }

  /**
   * The listener of the 'exit today' event.
   *
   * @param ctx the context of the 'today' rule just parsed
   */
  override fun exitToday(ctx: DateTimeParser.TodayContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Day
    this.dateTimeBuilder.offsetLength = 0
  }

  /**
   * The listener of the 'exit yesterday' event.
   *
   * @param ctx the context of the 'yesterday' rule just parsed
   */
  override fun exitYesterday(ctx: DateTimeParser.YesterdayContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Day
    this.dateTimeBuilder.positiveOffset = false
    this.dateTimeBuilder.offsetLength = 1
  }

  /**
   * The listener of the 'exit tomorrow' event.
   *
   * @param ctx the context of the 'tomorrow' rule just parsed
   */
  override fun exitTomorrow(ctx: DateTimeParser.TomorrowContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Day
    this.dateTimeBuilder.offsetLength = 1
  }

  /**
   * The listener of the 'exit day_after_tomorrow' event.
   *
   * @param ctx the context of the 'day_after_tomorrow' rule just parsed
   */
  override fun exitDay_after_tomorrow(ctx: DateTimeParser.Day_after_tomorrowContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Day
    this.dateTimeBuilder.offsetLength = 2
  }

  /**
   * The listener of the 'exit day_before_yesterday' event.
   *
   * @param ctx the context of the 'day_before_yesterday' rule just parsed
   */
  override fun exitDay_before_yesterday(ctx: DateTimeParser.Day_before_yesterdayContext) {

    this.dateTimeBuilder.dateUnit = DateUnit.Type.Day
    this.dateTimeBuilder.positiveOffset = false
    this.dateTimeBuilder.offsetLength = 2
  }

  /**
   * The listener of the 'exit offset_units_digits' event.
   *
   * @param ctx the context of the 'offset_units_digits' rule just parsed
   */
  override fun exitOffset_units_digits(ctx: DateTimeParser.Offset_units_digitsContext) {

    this.dateTimeBuilder.offsetLength = ctx.text.toInt()
  }

  /**
   * The listener of the 'exit offset_units_str' event.
   *
   * @param ctx the context of the 'offset_units_str' rule just parsed
   */
  override fun exitOffset_units_str(ctx: DateTimeParser.Offset_units_strContext) {

    this.dateTimeBuilder.offsetLength = this.strNumber
  }

  /**
   * The listener of the 'exit ordinal_prefix_number' event.
   *
   * @param ctx the context of the 'ordinal_prefix_number' rule just parsed
   */
  override fun exitOrdinal_prefix_number(ctx: DateTimeParser.Ordinal_prefix_numberContext) {

    this.dateTimeBuilder.ordinalPosition = this.strNumber
  }

  /**
   * The listener of the 'exit ns_1' event.
   *
   * @param ctx the context of the 'ns_1' rule just parsed
   */
  override fun exitNs_1(ctx: DateTimeParser.Ns_1Context) {

    this.strNumber = 1
  }

  /**
   * The listener of the 'exit ns_2' event.
   *
   * @param ctx the context of the 'ns_2' rule just parsed
   */
  override fun exitNs_2(ctx: DateTimeParser.Ns_2Context) {

    this.strNumber = 2
  }

  /**
   * The listener of the 'exit ns_3' event.
   *
   * @param ctx the context of the 'ns_3' rule just parsed
   */
  override fun exitNs_3(ctx: DateTimeParser.Ns_3Context) {

    this.strNumber = 3
  }

  /**
   * The listener of the 'exit ns_4' event.
   *
   * @param ctx the context of the 'ns_4' rule just parsed
   */
  override fun exitNs_4(ctx: DateTimeParser.Ns_4Context) {

    this.strNumber = 4
  }

  /**
   * The listener of the 'exit ns_5' event.
   *
   * @param ctx the context of the 'ns_5' rule just parsed
   */
  override fun exitNs_5(ctx: DateTimeParser.Ns_5Context) {

    this.strNumber = 5
  }

  /**
   * The listener of the 'exit ns_6' event.
   *
   * @param ctx the context of the 'ns_6' rule just parsed
   */
  override fun exitNs_6(ctx: DateTimeParser.Ns_6Context) {

    this.strNumber = 6
  }

  /**
   * The listener of the 'exit ns_7' event.
   *
   * @param ctx the context of the 'ns_7' rule just parsed
   */
  override fun exitNs_7(ctx: DateTimeParser.Ns_7Context) {

    this.strNumber = 7
  }

  /**
   * The listener of the 'exit ns_8' event.
   *
   * @param ctx the context of the 'ns_8' rule just parsed
   */
  override fun exitNs_8(ctx: DateTimeParser.Ns_8Context) {

    this.strNumber = 8
  }

  /**
   * The listener of the 'exit ns_9' event.
   *
   * @param ctx the context of the 'ns_9' rule just parsed
   */
  override fun exitNs_9(ctx: DateTimeParser.Ns_9Context) {

    this.strNumber = 9
  }

  /**
   * The listener of the 'exit ns_10' event.
   *
   * @param ctx the context of the 'ns_10' rule just parsed
   */
  override fun exitNs_10(ctx: DateTimeParser.Ns_10Context) {

    this.strNumber = 10
  }

  /**
   * The listener of the 'exit ns_11' event.
   *
   * @param ctx the context of the 'ns_11' rule just parsed
   */
  override fun exitNs_11(ctx: DateTimeParser.Ns_11Context) {

    this.strNumber = 11
  }

  /**
   * The listener of the 'exit ns_12' event.
   *
   * @param ctx the context of the 'ns_12' rule just parsed
   */
  override fun exitNs_12(ctx: DateTimeParser.Ns_12Context) {

    this.strNumber = 12
  }

  /**
   * The listener of the 'exit ns_13' event.
   *
   * @param ctx the context of the 'ns_13' rule just parsed
   */
  override fun exitNs_13(ctx: DateTimeParser.Ns_13Context) {

    this.strNumber = 13
  }

  /**
   * The listener of the 'exit ns_14' event.
   *
   * @param ctx the context of the 'ns_14' rule just parsed
   */
  override fun exitNs_14(ctx: DateTimeParser.Ns_14Context) {

    this.strNumber = 14
  }

  /**
   * The listener of the 'exit ns_15' event.
   *
   * @param ctx the context of the 'ns_15' rule just parsed
   */
  override fun exitNs_15(ctx: DateTimeParser.Ns_15Context) {

    this.strNumber = 15
  }

  /**
   * The listener of the 'exit ns_16' event.
   *
   * @param ctx the context of the 'ns_16' rule just parsed
   */
  override fun exitNs_16(ctx: DateTimeParser.Ns_16Context) {

    this.strNumber = 16
  }

  /**
   * The listener of the 'exit ns_17' event.
   *
   * @param ctx the context of the 'ns_17' rule just parsed
   */
  override fun exitNs_17(ctx: DateTimeParser.Ns_17Context) {

    this.strNumber = 17
  }

  /**
   * The listener of the 'exit ns_18' event.
   *
   * @param ctx the context of the 'ns_18' rule just parsed
   */
  override fun exitNs_18(ctx: DateTimeParser.Ns_18Context) {

    this.strNumber = 18
  }

  /**
   * The listener of the 'exit ns_19' event.
   *
   * @param ctx the context of the 'ns_19' rule just parsed
   */
  override fun exitNs_19(ctx: DateTimeParser.Ns_19Context) {

    this.strNumber = 19
  }

  /**
   * The listener of the 'exit ns_20' event.
   *
   * @param ctx the context of the 'ns_20' rule just parsed
   */
  override fun exitNs_20(ctx: DateTimeParser.Ns_20Context) {

    this.strNumber = 20
  }

  /**
   * The listener of the 'exit ns_21' event.
   *
   * @param ctx the context of the 'ns_21' rule just parsed
   */
  override fun exitNs_21(ctx: DateTimeParser.Ns_21Context) {

    this.strNumber = 21
  }

  /**
   * The listener of the 'exit ns_22' event.
   *
   * @param ctx the context of the 'ns_22' rule just parsed
   */
  override fun exitNs_22(ctx: DateTimeParser.Ns_22Context) {

    this.strNumber = 22
  }

  /**
   * The listener of the 'exit ns_23' event.
   *
   * @param ctx the context of the 'ns_23' rule just parsed
   */
  override fun exitNs_23(ctx: DateTimeParser.Ns_23Context) {

    this.strNumber = 23
  }

  /**
   * The listener of the 'exit ns_24' event.
   *
   * @param ctx the context of the 'ns_24' rule just parsed
   */
  override fun exitNs_24(ctx: DateTimeParser.Ns_24Context) {

    this.strNumber = 24
  }

  /**
   * The listener of the 'exit ns_25' event.
   *
   * @param ctx the context of the 'ns_25' rule just parsed
   */
  override fun exitNs_25(ctx: DateTimeParser.Ns_25Context) {

    this.strNumber = 25
  }

  /**
   * The listener of the 'exit ns_26' event.
   *
   * @param ctx the context of the 'ns_26' rule just parsed
   */
  override fun exitNs_26(ctx: DateTimeParser.Ns_26Context) {

    this.strNumber = 26
  }

  /**
   * The listener of the 'exit ns_27' event.
   *
   * @param ctx the context of the 'ns_27' rule just parsed
   */
  override fun exitNs_27(ctx: DateTimeParser.Ns_27Context) {

    this.strNumber = 27
  }

  /**
   * The listener of the 'exit ns_28' event.
   *
   * @param ctx the context of the 'ns_28' rule just parsed
   */
  override fun exitNs_28(ctx: DateTimeParser.Ns_28Context) {

    this.strNumber = 28
  }

  /**
   * The listener of the 'exit ns_29' event.
   *
   * @param ctx the context of the 'ns_29' rule just parsed
   */
  override fun exitNs_29(ctx: DateTimeParser.Ns_29Context) {

    this.strNumber = 29
  }

  /**
   * The listener of the 'exit ns_30' event.
   *
   * @param ctx the context of the 'ns_30' rule just parsed
   */
  override fun exitNs_30(ctx: DateTimeParser.Ns_30Context) {

    this.strNumber = 30
  }

  /**
   * The listener of the 'exit ns_31' event.
   *
   * @param ctx the context of the 'ns_31' rule just parsed
   */
  override fun exitNs_31(ctx: DateTimeParser.Ns_31Context) {

    this.strNumber = 31
  }
}
