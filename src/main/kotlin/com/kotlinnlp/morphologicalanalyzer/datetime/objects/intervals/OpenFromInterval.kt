/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.objects.intervals

import com.kotlinnlp.morphologicalanalyzer.datetime.objects.SingleDateTime

/**
 * A date-time interval without lower bound.
 *
 * E.g. "".
 *
 * @property startToken the index of the first token of this expression
 * @property endToken the index of the last token of this expression
 * @property to the upper bound date-time
 */
data class OpenFromInterval(
  override val startToken: Int,
  override val endToken: Int,
  override val to: SingleDateTime
) : UpperLimitedInterval {

  /**
   * Get the string representing this interval in the following standard format:
   *
   * @return the string representing this interval
   */
  override fun toStandardFormat(): String = "to ${this.to}"

  /**
   * @return a string representation of this interval object
   */
  override fun toString(): String = this.toStandardFormat()
}
