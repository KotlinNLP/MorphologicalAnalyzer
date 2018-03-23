/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.objects

/**
 * A date-offset object.
 *
 * E.g. "Monday of next week".
 *
 * @property startToken the index of the first token of this expression
 * @property endToken the index of the last token of this expression
 * @property date the date
 * @property offset the offset of the [date] (e.g. "next week")
 */
data class DateOffset(
  override val startToken: Int,
  override val endToken: Int,
  val date: Date,
  val offset: Offset
) : DateTime {

  /**
   * Get the string representing this date-offset in the following standard format:
   *   DATE of OFFSET.
   *
   * @return the string representing this date-offset
   */
  override fun toStandardFormat(): String = "%s of %s".format(this.date, this.offset)

  /**
   * @return a string representation of this date-offset object
   */
  override fun toString(): String = this.toStandardFormat()
}
