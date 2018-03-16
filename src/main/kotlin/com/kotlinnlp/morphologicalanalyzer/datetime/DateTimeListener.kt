/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.DateTimeBaseListener
import com.kotlinnlp.morphologicalanalyzer.datetime.grammar.DateTimeParser
import com.kotlinnlp.neuraltokenizer.Token

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
   * The listener of the 'exit year' event.
   *
   * @param ctx the context of the 'year' rule just parsed
   */
  override fun exitYear(ctx: DateTimeParser.YearContext) {

    this.dateTimeBuilder.setYearNum(ctx.start.startIndex)
  }

  /**
   * The listener of the 'exit year_APEX' event.
   *
   * @param ctx the context of the 'year_APEX' rule just parsed
   */
  override fun exitYear_APEX(ctx: DateTimeParser.Year_APEXContext) {

    this.dateTimeBuilder.setYearNum(ctx.start.startIndex)
  }

  /**
   * The listener of the 'exit year_modern' event.
   *
   * @param ctx the context of the 'year_modern' rule just parsed
   */
  override fun exitYear_modern(ctx: DateTimeParser.Year_modernContext) {

    this.dateTimeBuilder.setYearNum(ctx.start.startIndex)
  }

  /**
   * The listener of the 'exit month_num' event.
   *
   * @param ctx the context of the 'month_num' rule just parsed
   */
  override fun exitMonth_num(ctx: DateTimeParser.Month_numContext) {

    this.dateTimeBuilder.setMonthNum(ctx.start.startIndex)
  }

  /**
   * The listener of the 'exit month_str' event.
   *
   * @param ctx the context of the 'month_str' rule just parsed
   */
  override fun exitMonth_str(ctx: DateTimeParser.Month_strContext) {

    this.dateTimeBuilder.setMonthStr(ctx.start.startIndex)
  }

  /**
   * The listener of the 'exit day_num' event.
   *
   * @param ctx the context of the 'day_num' rule just parsed
   */
  override fun exitDay_num(ctx: DateTimeParser.Day_numContext) {

    this.dateTimeBuilder.setDayNum(ctx.start.startIndex)
  }

  /**
   * The listener of the 'exit day_str' event.
   *
   * @param ctx the context of the 'day_str' rule just parsed
   */
  override fun exitDay_str(ctx: DateTimeParser.Day_strContext) {

    this.dateTimeBuilder.setDayStr(ctx.start.startIndex)
  }
}
