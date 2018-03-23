/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.objects

import java.util.*

/**
 * A time object.
 * At least one of [sec], [min] and [hour] is not null.
 *
 * E.g. "9:15", "h 9", "09:15:45".
 *
 * @property startToken the index of the first token of this expression
 * @property endToken the index of the last token of this expression
 * @property hour the number of the hour in the range [0, 23] (can be null)
 * @property min the number of the minute in the range [0, 59] (can be null)
 * @property sec the number of the second in the range [0, 59] (can be null)
 * @property millisec the number of the millisecond in the range [0, 999] (can be null)
 * @property timezone the timezone (can be null)
 */
data class Time(
  override val startToken: Int,
  override val endToken: Int,
  val hour: Int?,
  val min: Int?,
  val sec: Int?,
  val millisec: Int?,
  val timezone: TimeZone?
) : DateTime {

  /**
   * Check that at least one property is defined.
   */
  init {
    require(listOf(this.sec, this.min, this.hour).any { it != null })
  }

  /**
   * Get the string representing this time in the following standard format: hh:mm:ss.ms[ (TIMEZONE)].
   *
   * @return the string representing this time
   */
  override fun toStandardFormat(): String = "%s:%s:%s.%s%s".format(
    this.hour?.let { "%02d".format(it) } ?: "-",
    this.min?.let { "%02d".format(it) } ?: "-",
    this.sec?.let { "%02d".format(it) } ?: "-",
    this.millisec?.let { "%02d".format(it) } ?: "-",
    this.timezone?.let { " ${it.toZoneId()}" } ?: ""
  )

  /**
   * @return a string representation of this time object
   */
  override fun toString(): String = this.toStandardFormat()
}
