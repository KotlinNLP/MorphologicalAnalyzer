/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.DateTimeBaseListener
import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.DateTimeParser
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateTime
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
   * It is set when an 'exit n_s_*' event is triggered.
   */
  private var strNumber: Int = -1

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
   * The listener of the 'exit datetime' event.
   *
   * @param ctx the context of the 'datetime' rule just parsed
   */
  override fun exitDatetime(ctx: DateTimeParser.DatetimeContext) {

    this.dateTimes.add(
      this.dateTimeBuilder.getDateTime(startIndex = ctx.start.startIndex, endIndex = ctx.stop.stopIndex)
    )
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
   * The listener of the 'exit n_s_1' event.
   *
   * @param ctx the context of the 'n_s_1' rule just parsed
   */
  override fun exitN_s_1(ctx: DateTimeParser.N_s_1Context) {

    this.strNumber = 1
  }

  /**
   * The listener of the 'exit n_s_2' event.
   *
   * @param ctx the context of the 'n_s_2' rule just parsed
   */
  override fun exitN_s_2(ctx: DateTimeParser.N_s_2Context) {

    this.strNumber = 2
  }

  /**
   * The listener of the 'exit n_s_3' event.
   *
   * @param ctx the context of the 'n_s_3' rule just parsed
   */
  override fun exitN_s_3(ctx: DateTimeParser.N_s_3Context) {

    this.strNumber = 3
  }

  /**
   * The listener of the 'exit n_s_4' event.
   *
   * @param ctx the context of the 'n_s_4' rule just parsed
   */
  override fun exitN_s_4(ctx: DateTimeParser.N_s_4Context) {

    this.strNumber = 4
  }

  /**
   * The listener of the 'exit n_s_5' event.
   *
   * @param ctx the context of the 'n_s_5' rule just parsed
   */
  override fun exitN_s_5(ctx: DateTimeParser.N_s_5Context) {

    this.strNumber = 5
  }

  /**
   * The listener of the 'exit n_s_6' event.
   *
   * @param ctx the context of the 'n_s_6' rule just parsed
   */
  override fun exitN_s_6(ctx: DateTimeParser.N_s_6Context) {

    this.strNumber = 6
  }

  /**
   * The listener of the 'exit n_s_7' event.
   *
   * @param ctx the context of the 'n_s_7' rule just parsed
   */
  override fun exitN_s_7(ctx: DateTimeParser.N_s_7Context) {

    this.strNumber = 7
  }

  /**
   * The listener of the 'exit n_s_8' event.
   *
   * @param ctx the context of the 'n_s_8' rule just parsed
   */
  override fun exitN_s_8(ctx: DateTimeParser.N_s_8Context) {

    this.strNumber = 8
  }

  /**
   * The listener of the 'exit n_s_9' event.
   *
   * @param ctx the context of the 'n_s_9' rule just parsed
   */
  override fun exitN_s_9(ctx: DateTimeParser.N_s_9Context) {

    this.strNumber = 9
  }

  /**
   * The listener of the 'exit n_s_10' event.
   *
   * @param ctx the context of the 'n_s_10' rule just parsed
   */
  override fun exitN_s_10(ctx: DateTimeParser.N_s_10Context) {

    this.strNumber = 10
  }

  /**
   * The listener of the 'exit n_s_11' event.
   *
   * @param ctx the context of the 'n_s_11' rule just parsed
   */
  override fun exitN_s_11(ctx: DateTimeParser.N_s_11Context) {

    this.strNumber = 11
  }

  /**
   * The listener of the 'exit n_s_12' event.
   *
   * @param ctx the context of the 'n_s_12' rule just parsed
   */
  override fun exitN_s_12(ctx: DateTimeParser.N_s_12Context) {

    this.strNumber = 12
  }

  /**
   * The listener of the 'exit n_s_13' event.
   *
   * @param ctx the context of the 'n_s_13' rule just parsed
   */
  override fun exitN_s_13(ctx: DateTimeParser.N_s_13Context) {

    this.strNumber = 13
  }

  /**
   * The listener of the 'exit n_s_14' event.
   *
   * @param ctx the context of the 'n_s_14' rule just parsed
   */
  override fun exitN_s_14(ctx: DateTimeParser.N_s_14Context) {

    this.strNumber = 14
  }

  /**
   * The listener of the 'exit n_s_15' event.
   *
   * @param ctx the context of the 'n_s_15' rule just parsed
   */
  override fun exitN_s_15(ctx: DateTimeParser.N_s_15Context) {

    this.strNumber = 15
  }

  /**
   * The listener of the 'exit n_s_16' event.
   *
   * @param ctx the context of the 'n_s_16' rule just parsed
   */
  override fun exitN_s_16(ctx: DateTimeParser.N_s_16Context) {

    this.strNumber = 16
  }

  /**
   * The listener of the 'exit n_s_17' event.
   *
   * @param ctx the context of the 'n_s_17' rule just parsed
   */
  override fun exitN_s_17(ctx: DateTimeParser.N_s_17Context) {

    this.strNumber = 17
  }

  /**
   * The listener of the 'exit n_s_18' event.
   *
   * @param ctx the context of the 'n_s_18' rule just parsed
   */
  override fun exitN_s_18(ctx: DateTimeParser.N_s_18Context) {

    this.strNumber = 18
  }

  /**
   * The listener of the 'exit n_s_19' event.
   *
   * @param ctx the context of the 'n_s_19' rule just parsed
   */
  override fun exitN_s_19(ctx: DateTimeParser.N_s_19Context) {

    this.strNumber = 19
  }

  /**
   * The listener of the 'exit n_s_20' event.
   *
   * @param ctx the context of the 'n_s_20' rule just parsed
   */
  override fun exitN_s_20(ctx: DateTimeParser.N_s_20Context) {

    this.strNumber = 20
  }

  /**
   * The listener of the 'exit n_s_21' event.
   *
   * @param ctx the context of the 'n_s_21' rule just parsed
   */
  override fun exitN_s_21(ctx: DateTimeParser.N_s_21Context) {

    this.strNumber = 21
  }

  /**
   * The listener of the 'exit n_s_22' event.
   *
   * @param ctx the context of the 'n_s_22' rule just parsed
   */
  override fun exitN_s_22(ctx: DateTimeParser.N_s_22Context) {

    this.strNumber = 22
  }

  /**
   * The listener of the 'exit n_s_23' event.
   *
   * @param ctx the context of the 'n_s_23' rule just parsed
   */
  override fun exitN_s_23(ctx: DateTimeParser.N_s_23Context) {

    this.strNumber = 23
  }

  /**
   * The listener of the 'exit n_s_24' event.
   *
   * @param ctx the context of the 'n_s_24' rule just parsed
   */
  override fun exitN_s_24(ctx: DateTimeParser.N_s_24Context) {

    this.strNumber = 24
  }

  /**
   * The listener of the 'exit n_s_25' event.
   *
   * @param ctx the context of the 'n_s_25' rule just parsed
   */
  override fun exitN_s_25(ctx: DateTimeParser.N_s_25Context) {

    this.strNumber = 25
  }

  /**
   * The listener of the 'exit n_s_26' event.
   *
   * @param ctx the context of the 'n_s_26' rule just parsed
   */
  override fun exitN_s_26(ctx: DateTimeParser.N_s_26Context) {

    this.strNumber = 26
  }

  /**
   * The listener of the 'exit n_s_27' event.
   *
   * @param ctx the context of the 'n_s_27' rule just parsed
   */
  override fun exitN_s_27(ctx: DateTimeParser.N_s_27Context) {

    this.strNumber = 27
  }

  /**
   * The listener of the 'exit n_s_28' event.
   *
   * @param ctx the context of the 'n_s_28' rule just parsed
   */
  override fun exitN_s_28(ctx: DateTimeParser.N_s_28Context) {

    this.strNumber = 28
  }

  /**
   * The listener of the 'exit n_s_29' event.
   *
   * @param ctx the context of the 'n_s_29' rule just parsed
   */
  override fun exitN_s_29(ctx: DateTimeParser.N_s_29Context) {

    this.strNumber = 29
  }

  /**
   * The listener of the 'exit n_s_30' event.
   *
   * @param ctx the context of the 'n_s_30' rule just parsed
   */
  override fun exitN_s_30(ctx: DateTimeParser.N_s_30Context) {

    this.strNumber = 30
  }

  /**
   * The listener of the 'exit n_s_31' event.
   *
   * @param ctx the context of the 'n_s_31' rule just parsed
   */
  override fun exitN_s_31(ctx: DateTimeParser.N_s_31Context) {

    this.strNumber = 31
  }
}
