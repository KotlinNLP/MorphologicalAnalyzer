/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.objects

import com.kotlinnlp.morphologicalanalyzer.datetime.utils.DateObj
import com.kotlinnlp.morphologicalanalyzer.datetime.utils.DateTimeObj

/**
 * An ordinal date object.
 *
 * E.g. "The second week of 2015".
 *
 * @property dateUnit the date unit as string
 */
sealed class DateOrdinal(private val dateUnit: String) : DateTime {

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
   * The ordinal position of the date unit.
   */
  abstract val position: Position

  /**
   * The reference date-time.
   */
  abstract val dateTime: DateTimeObj

  /**
   * Get the string representing this ordinal date in the following standard format:
   *   the POSITION DATE_UNIT of DATE_TIME
   *
   * @return the string representing this ordinal date
   */
  override fun toStandardFormat(): String = "the $position '$dateUnit' of '$dateTime'"

  /**
   * An ordinal date of [DateObj] units.
   *
   * @property value the date unit value
   */
  data class Date(
    override val startToken: Int,
    override val endToken: Int,
    override val position: Position,
    override val dateTime: DateTimeObj,
    val value: DateObj
  ) : DateOrdinal(dateUnit = value.toString()) {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An ordinal date of 'day' units.
   */
  data class Day(
    override val startToken: Int,
    override val endToken: Int,
    override val position: Position,
    override val dateTime: DateTimeObj
  ) : DateOrdinal(dateUnit = "day") {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An ordinal date of 'week' units.
   */
  data class Week(
    override val startToken: Int,
    override val endToken: Int,
    override val position: Position,
    override val dateTime: DateTimeObj
  ) : DateOrdinal(dateUnit = "week") {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An ordinal date of 'weekend' units.
   */
  data class Weekend(
    override val startToken: Int,
    override val endToken: Int,
    override val position: Position,
    override val dateTime: DateTimeObj
  ) : DateOrdinal(dateUnit = "weekend") {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An ordinal date of 'month' units.
   */
  data class Month(
    override val startToken: Int,
    override val endToken: Int,
    override val position: Position,
    override val dateTime: DateTimeObj
  ) : DateOrdinal(dateUnit = "month") {

    override fun toString(): String = this.toStandardFormat()
  }

  /**
   * An ordinal date of 'year' units.
   */
  data class Year(
    override val startToken: Int,
    override val endToken: Int,
    override val position: Position,
    override val dateTime: DateTimeObj
  ) : DateOrdinal(dateUnit = "year") {

    override fun toString(): String = this.toStandardFormat()
  }
}
