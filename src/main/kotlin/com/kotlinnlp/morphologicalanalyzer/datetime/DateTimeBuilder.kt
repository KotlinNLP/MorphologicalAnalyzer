/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

import com.kotlinnlp.neuraltokenizer.Token

/**
 * The helper to build a [DateTime].
 *
 * @param tokens the list of tokens that compose the input text.
 */
internal class DateTimeBuilder(private val tokens: List<Token>) {

  /**
   *
   */
  private var year: Int? = null

  /**
   *
   */
  private var yearStr: String? = null

  /**
   *
   */
  private var month: Int? = null

  /**
   *
   */
  private var monthStr: String? = null

  /**
   *
   */
  private var day: Int? = null

  /**
   *
   */
  private var dayStr: String? = null

  /**
   *
   */
  fun getDateTime(startIndex: Int, endIndex: Int) = DateTime(
    startToken = this.tokens.indexOfFirst { it.startAt == startIndex },
    endToken = this.tokens.indexOfFirst { it.endAt == endIndex },
    year = this.year,
    yearStr = this.yearStr,
    month = this.month,
    monthStr = this.monthStr,
    day = this.day,
    dayStr = this.dayStr
  )

  /**
   *
   */
  fun setYearNum(tokenIndex: Int) {
    this.year = this.tokens.first { it.startAt == tokenIndex }.form.trim('\'', '’').toInt()
  }

  /**
   *
   */
  fun setMonthNum(tokenIndex: Int) {
    this.month = this.tokens.first { it.startAt == tokenIndex }.form.toInt()
  }

  /**
   *
   */
  fun setMonthStr(tokenIndex: Int) {
    this.monthStr = this.tokens.first { it.startAt == tokenIndex }.form
  }

  /**
   *
   */
  fun setDayNum(tokenIndex: Int) {
    this.day = this.tokens.first { it.startAt == tokenIndex }.form.takeWhile { it.isDigit() } .toInt()
  }

  /**
   *
   */
  fun setDayStr(tokenIndex: Int) {
    this.dayStr = this.tokens.first { it.startAt == tokenIndex }.form
  }
}