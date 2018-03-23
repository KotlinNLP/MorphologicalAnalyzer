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
 * E.g. "Next year", "In three weeks", "The next 10th August".
 */
sealed class Offset : DateTime {

  /**
   * Whether this offset is positive or negative.
   */
  abstract val positive: Boolean

  /**
   * The count of offset units, in the range [0, +inf] (e.g. + 2 weeks).
   */
  abstract val units: Int

  /**
   * The string prefix: "(+|-) UNITS".
   */
  protected val stringPrefix: String by lazy { "%s %d".format(if (this.positive) "+" else "-", this.units) }

  /**
   * Get the string representing this offset in the following standard format:
   *   (+|-) UNITS OFFSET_TYPE.
   *
   * @return the string representing this offset
   */
  override fun toStandardFormat(): String = "%s %s".format(this.stringPrefix, this::class.simpleName!!.toLowerCase())

  /**
   * An offset of [DateObj].
   *
   * @property startToken the index of the first token of this expression
   * @property endToken the index of the last token of this expression
   * @property positive whether this offset is positive or negative
   * @property units the count of offset units, in the range [0, +inf] (e.g. + 2 weeks)
   * @property value the Date value
   */
  data class Date(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int,
    val value: DateObj
  ) : Offset() {

    /**
     * Get the string representing this offset in the following standard format:
     *   (+|-) UNITS DATE.
     *
     * @return the string representing this offset
     */
    override fun toStandardFormat(): String = "%s %s".format(this.stringPrefix, this.value)

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of [TimeObj].
   *
   * @property value the Time value
   */
  data class Time(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int,
    val value: TimeObj
  ) : Offset() {

    /**
     * Get the string representing this offset in the following standard format:
     *   (+|-) UNITS TIME.
     *
     * @return the string representing this offset
     */
    override fun toStandardFormat(): String = "%s %s".format(this.stringPrefix, this.value)

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of hours.
   */
  data class Hours(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int
  ) : Offset() {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of minutes.
   */
  data class Minutes(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int
  ) : Offset() {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of seconds.
   */
  data class Seconds(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int
  ) : Offset() {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of days.
   */
  data class Days(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int
  ) : Offset() {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of weeks.
   */
  data class Weeks(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int
  ) : Offset() {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of weekends.
   */
  data class Weekends(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int
  ) : Offset() {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of months.
   */
  data class Months(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int
  ) : Offset() {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An offset of years.
   */
  data class Years(
    override val startToken: Int,
    override val endToken: Int,
    override val positive: Boolean,
    override val units: Int
  ) : Offset(){

    override fun toString(): String = this.toStandardFormat()
  }
}
