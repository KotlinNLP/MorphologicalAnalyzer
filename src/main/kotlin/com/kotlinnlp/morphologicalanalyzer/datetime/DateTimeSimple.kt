/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

/**
 * A simple date-time object.
 *
 * @property startToken the index of the first token of the expression
 * @property endToken the index of the last token of the expression
 * @property day the number of the day in the range [1, 31] (can be null)
 * @property weekDay the number of the week day in the range [1, 7] (can be null)
 * @property month the number of the mont in the range [1, 12] (can be null)
 * @property year the number of the year in the range [0, 9999] (can be null)
 * @property yearAbbr whether the [year] is intended as abbreviated form (e.g. '98 stands for 1998)
 * @property sec the number of the sec in the range [0, 59] (can be null)
 * @property min the number of the min in the range [0, 59] (can be null)
 * @property hour the number of the hour in the range [0, 23] (can be null)
 */
data class DateTimeSimple(
  val startToken: Int,
  val endToken: Int,
  val day: Int?,
  val weekDay: Int?,
  val month: Int?,
  val year: Int?,
  val yearAbbr: Boolean,
  val sec: Int?,
  val min: Int?,
  val hour: Int?
) : DateTime {

  companion object {

    /**
     * The list of week day names.
     */
    val WEEK_DAYS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
  }

  /**
   * Get the string representing this date-time in the following standard format: DD/MM/YYYY.
   *
   * @return the string representing this date-time
   */
  fun toStandardFormat(): String = "%s/%s/%s %s:%s:%s".format(
    this.day?.let { "%02d".format(it) } ?: "-",
    this.month?.let { "%02d".format(it) } ?: "-",
    this.year?.let { (if (this.yearAbbr) "%02d" else "%04d").format(it) } ?: "-",
    this.hour?.let { "%02d".format(it) } ?: "-",
    this.min?.let { "%02d".format(it) } ?: "-",
    this.sec?.let { "%02d".format(it) } ?: "-"
  )

  /**
   * @return the standard string representation of the [weekDay]
   */
  fun weekDayToString(): String = this.weekDay?.let { WEEK_DAYS[it - 1] } ?: "-"

  /**
   * @return a string representation of this date-time object
   */
  override fun toString(): String = "%s (week day: %s, year abbr.: %s)".format(
    this.toStandardFormat(),
    this.weekDayToString(),
    this.yearAbbr
  )
}
