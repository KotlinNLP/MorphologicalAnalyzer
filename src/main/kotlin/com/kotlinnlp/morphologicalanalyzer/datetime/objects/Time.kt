/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.objects

/**
 * A time object.
 * At least one of [sec], [min] and [hour] is not null.
 *
 * @property startToken the index of the first token of this expression
 * @property endToken the index of the last token of this expression
 * @property sec the number of the sec in the range [0, 59] (can be null)
 * @property min the number of the min in the range [0, 59] (can be null)
 * @property hour the number of the hour in the range [0, 23] (can be null)
 */
data class Time(
  override val startToken: Int,
  override val endToken: Int,
  val sec: Int?,
  val min: Int?,
  val hour: Int?
) : DateTime {

  /**
   * Check that at least one property is defined.
   */
  init {
    require(listOf(this.sec, this.min, this.hour).any { it != null })
  }

  /**
   * Get the string representing this time in the following standard format: hh:mm:ss.
   *
   * @return the string representing this time
   */
  override fun toStandardFormat(): String = "%s:%s:%s".format(
    this.hour?.let { "%02d".format(it) } ?: "-",
    this.min?.let { "%02d".format(it) } ?: "-",
    this.sec?.let { "%02d".format(it) } ?: "-"
  )

  /**
   * @return a string representation of this time object
   */
  override fun toString(): String = this.toStandardFormat()
}
