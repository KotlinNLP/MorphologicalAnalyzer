/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime

/**
 * A date-time object.
 *
 * @property startToken the index of the first token of the expression
 * @property endToken the index of the last token of the expression
 * @property year
 * @property yearStr
 * @property month
 * @property monthStr
 * @property day
 * @property dayStr
 */
data class DateTime(
  val startToken: Int,
  val endToken: Int,
  val year: Int?,
  val yearStr: String?,
  val month: Int?,
  val monthStr: String?,
  val day: Int?,
  val dayStr: String?
) {

  /**
   *
   */
  fun toStandardFormat(): String = "%s/%s/%s".format(
    this.day?.let { "%02d".format(it) } ?: this.dayStr ?: "-",
    this.month?.let { "%02d".format(it) } ?: this.monthStr ?: "-",
    this.year?.let { "%02d".format(it) } ?: this.yearStr ?: "-"
  )
}
