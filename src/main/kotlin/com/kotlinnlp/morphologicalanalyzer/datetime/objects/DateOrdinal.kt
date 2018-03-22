/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.objects

import com.kotlinnlp.morphologicalanalyzer.datetime.utils.DateTimeObj

/**
 * An ordinal date object (e.g. "The second week of 2015").
 *
 * @property startToken the index of the first token of this expression
 * @property endToken the index of the last token of this expression
 * @property position the ordinal position of the date unit
 * @property dateTime the reference date-time
 */
sealed class DateOrdinal(
  override val startToken: Int,
  override val endToken: Int,
  val position: Position,
  val dateTime: DateTimeObj
) : DateTime {

  /**
   * The position.
   */
  sealed class Position {

    /**
     * The ordinal position.
     *
     * @property count the number that represent the ordinal position (1 = first, 2 = second, etc.)
     */
    class Ordinal(val count: Int) : Position() {

      init {
        require(this.count > 0)
      }

      override fun toString() = "n. $count"
    }

    /**
     * The 'last' position.
     */
    class Last : Position() {

      override fun toString() = "last"
    }
  }

  /**
   * The date unit as string.
   */
  abstract protected val dateUnit: String

  /**
   * Get the string representing this ordinal date in the following standard format:
   *   the POSITION DATE_UNIT of DATE_TIME
   *
   * @return the string representing this ordinal date
   */
  override fun toStandardFormat(): String = "the $position '$dateUnit' of '$dateTime'"

  /**
   * @return a string representation of this ordinal date object
   */
  override fun toString(): String = this.toStandardFormat()

  /**
   * An ordinal date of [DateTimeObj] units.
   *
   * @property value the date-time value (can be a date or an offset)
   */
  class DateTime(startToken: Int, endToken: Int, position: Position, dateTime: DateTimeObj, val value: DateTimeObj)
    : DateOrdinal(startToken = startToken, endToken = endToken, position = position, dateTime = dateTime) {

    override val dateUnit: String = this.value.toString()
  }

  /**
   * An ordinal date of 'day' units.
   */
  class Day(startToken: Int, endToken: Int, position: Position, dateTime: DateTimeObj)
    : DateOrdinal(startToken = startToken, endToken = endToken, position = position, dateTime = dateTime) {

    override val dateUnit: String = "day"
  }

  /**
   * An ordinal date of 'week' units.
   */
  class Week(startToken: Int, endToken: Int, position: Position, dateTime: DateTimeObj)
    : DateOrdinal(startToken = startToken, endToken = endToken, position = position, dateTime = dateTime) {

    override val dateUnit: String = "week"
  }

  /**
   * An ordinal date of 'weekend' units.
   */
  class Weekend(startToken: Int, endToken: Int, position: Position, dateTime: DateTimeObj)
    : DateOrdinal(startToken = startToken, endToken = endToken, position = position, dateTime = dateTime) {

    override val dateUnit: String = "weekend"
  }

  /**
   * An ordinal date of 'month' units.
   */
  class Month(startToken: Int, endToken: Int, position: Position, dateTime: DateTimeObj)
    : DateOrdinal(startToken = startToken, endToken = endToken, position = position, dateTime = dateTime) {

    override val dateUnit: String = "month"
  }

  /**
   * An ordinal date of 'year' units.
   */
  class Year(startToken: Int, endToken: Int, position: Position, dateTime: DateTimeObj)
    : DateOrdinal(startToken = startToken, endToken = endToken, position = position, dateTime = dateTime) {

    override val dateUnit: String = "year"
  }
}
