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
  private var yearAbbr: Boolean = false

  /**
   *
   */
  private var month: Int? = null

  /**
   *
   */
  private var day: Int? = null

  /**
   *
   */
  private var weekDay: Int? = null

  /**
   *
   */
  fun getDateTime(startIndex: Int, endIndex: Int) = DateTime(
    startToken = this.tokens.indexOfFirst { it.startAt == startIndex },
    endToken = this.tokens.indexOfFirst { it.endAt == endIndex },
    day = this.day,
    weekDay = this.weekDay,
    month = this.month,
    year = this.year,
    yearAbbr = this.yearAbbr
  )

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
  fun setMonth(value: Int) {
    this.month = value
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
}