/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.objects

/**
 * A date object.
 * At least one of [day], [weekDay], [month] and [year] is not null.
 *
 * @property startToken the index of the first token of this expression
 * @property endToken the index of the last token of this expression
 * @property day the number of the day in the range [1, 31] (can be null)
 * @property weekDay the number of the week day in the range [1, 7] (can be null)
 * @property month the number of the mont in the range [1, 12] (can be null)
 * @property year the number of the year in the range [0, 9999] (can be null)
 * @property yearAbbr whether the [year] is intended as abbreviated form (e.g. '98 stands for 1998)
 */
data class Date(
  override val startToken: Int,
  override val endToken: Int,
  val day: Int?,
  val weekDay: Int?,
  val month: Int?,
  val year: Int?,
  val yearAbbr: Boolean
) : DateTime {

  companion object {

    /**
     * The list of week day names.
     */
    val WEEK_DAYS = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
  }

  /**
   * Check that at least one property is defined.
   */
  init {
    require(listOf(this.day, this.weekDay, this.month, this.year).any { it != null })
  }

  /**
   * Get the string representing this date in the following standard format: DD/MM/YYYY.
   *
   * @return the string representing this date
   */
  override fun toStandardFormat(): String = "%s/%s/%s".format(
    this.day?.let { "%02d".format(it) } ?: "-",
    this.month?.let { "%02d".format(it) } ?: "-",
    this.year?.let { (if (this.yearAbbr) "%02d" else "%04d").format(it) } ?: "-"
  )

  /**
   * @return the standard string representation of the [weekDay]
   */
  fun weekDayToString(): String = this.weekDay?.let { WEEK_DAYS[it - 1] } ?: "-"

  /**
   * @return a string representation of this date-time object
   */
  override fun toString(): String = "%s %s%s".format(
    this.weekDayToString(),
    this.toStandardFormat(),
    if (this.yearAbbr) " (year abbr.)" else ""
  )
}
