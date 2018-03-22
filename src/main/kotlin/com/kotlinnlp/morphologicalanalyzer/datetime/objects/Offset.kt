/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.objects

import com.kotlinnlp.morphologicalanalyzer.datetime.utils.DateObj
import com.kotlinnlp.morphologicalanalyzer.datetime.utils.TimeObj

/**
 * An offset object.
 *
 * @property startToken the index of the first token of this expression
 * @property endToken the index of the last token of this expression
 * @property positive whether this offset is positive or negative
 * @property units the count of offset units, in the range [0, +inf] (e.g. + 2 weeks)
 */
sealed class Offset(
  override val startToken: Int,
  override val endToken: Int,
  val positive: Boolean,
  val units: Int
) : DateTime {

  /**
   * The string prefix: "(+|-) UNITS".
   */
  protected val stringPrefix: String = "%s %d".format(if (this.positive) "+" else "-", this.units)

  /**
   * Check requirements.
   */
  init {
    require(this.units >= 0) { "Units must be >= 0." }
  }

  /**
   * Get the string representing this offset in the following standard format:
   *   (+|-) UNITS OFFSET_TYPE.
   *
   * @return the string representing this offset
   */
  override fun toStandardFormat(): String = "%s %s".format(this.stringPrefix, this::class.simpleName)

  /**
   * @return a string representation of this offset object
   */
  override fun toString(): String = this.toStandardFormat()

  /**
   * An offset of [DateObj].
   *
   * @property value the Date value
   */
  class Date(startToken: Int, endToken: Int, positive: Boolean, units: Int, val value: DateObj)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units) {

    /**
     * Get the string representing this offset in the following standard format:
     *   (+|-) UNITS DATE.
     *
     * @return the string representing this offset
     */
    override fun toStandardFormat(): String = "%s %s".format(this.stringPrefix, this.value)
  }

  /**
   * An offset of [TimeObj].
   *
   * @property value the Time value
   */
  class Time(startToken: Int, endToken: Int, positive: Boolean, units: Int, val value: TimeObj)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units) {

    /**
     * Get the string representing this offset in the following standard format:
     *   (+|-) UNITS TIME.
     *
     * @return the string representing this offset
     */
    override fun toStandardFormat(): String = "%s %s".format(this.stringPrefix, this.value)
  }

  /**
   * An offset of hours.
   */
  class Hours(startToken: Int, endToken: Int, positive: Boolean, units: Int)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units)

  /**
   * An offset of minutes.
   */
  class Minutes(startToken: Int, endToken: Int, positive: Boolean, units: Int)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units)

  /**
   * An offset of seconds.
   */
  class Seconds(startToken: Int, endToken: Int, positive: Boolean, units: Int)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units)

  /**
   * An offset of days.
   */
  class Days(startToken: Int, endToken: Int, positive: Boolean, units: Int)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units)

  /**
   * An offset of weeks.
   */
  class Weeks(startToken: Int, endToken: Int, positive: Boolean, units: Int)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units)

  /**
   * An offset of weekends.
   */
  class Weekends(startToken: Int, endToken: Int, positive: Boolean, units: Int)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units)

  /**
   * An offset of months.
   */
  class Months(startToken: Int, endToken: Int, positive: Boolean, units: Int)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units)

  /**
   * An offset of years.
   */
  class Years(startToken: Int, endToken: Int, positive: Boolean, units: Int)
    : Offset(startToken = startToken, endToken = endToken, positive = positive, units = units)
}
