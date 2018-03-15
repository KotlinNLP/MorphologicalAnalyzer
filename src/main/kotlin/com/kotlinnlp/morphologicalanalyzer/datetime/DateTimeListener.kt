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
internal class DateTimeListener(private val tokens: List<Token>) : DateTimeBaseListener() {

  /**
   * The list of date-times recognized.
   */
  private val dateTimes: MutableList<DateTime> = mutableListOf()

  /**
   * @return the list of date-times recognized during the last parsing
   */
  fun getDateTimes(): List<DateTime> = this.dateTimes

  /**
   * The listener of the 'enter root' event.
   *
   * @param ctx the context of the 'root' rule
   */
  override fun enterRoot(ctx: DateTimeParser.RootContext?) {

    this.dateTimes.clear()
  }

  /**
   * The listener of the 'enter root' event.
   *
   * @param ctx the context of the 'datetime' rule just parsed
   */
  override fun exitDatetime(ctx: DateTimeParser.DatetimeContext) {

    this.dateTimes.add(
      DateTime(
        startToken = this.tokens.indexOfFirst { it.startAt == ctx.start.startIndex },
        endToken = this.tokens.indexOfFirst { it.endAt == ctx.stop.stopIndex }
      )
    )
  }
}
