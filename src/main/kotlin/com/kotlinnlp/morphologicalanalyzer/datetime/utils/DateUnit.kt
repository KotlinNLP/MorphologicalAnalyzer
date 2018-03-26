/* Copyright 2018-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.datetime.utils

import com.kotlinnlp.morphologicalanalyzer.datetime.objects.DateOrdinal
import com.kotlinnlp.morphologicalanalyzer.datetime.objects.Offset
import kotlin.reflect.KClass

/**
 * Utils for the date units.
 */
internal object DateUnit {

  /**
   * The type of date unit.
   *
   * @property Hour the hour unit
   * @property QuarterHour the quarter of an hour unit
   * @property HalfHour the half-hour unit
   * @property Minute the minute unit
   * @property Second the second unit
   * @property Day the day unit
   * @property Week the week unit
   * @property Weekend the weekend unit
   * @property Month the month unit
   * @property Year the year unit
   */
  enum class Type { Hour, QuarterHour, HalfHour, Minute, Second, Day, Week, Weekend, Month, Year }

  /**
   * A map of date unit types to [Offset] k-classes.
   */
  val toOffsetClasses: Map<Type, KClass<*>> = mapOf(
    Type.Hour to Offset.Hours::class,
    Type.HalfHour to Offset.HalfHours::class,
    Type.QuarterHour to Offset.QuarterHours::class,
    Type.Minute to Offset.Minutes::class,
    Type.Second to Offset.Seconds::class,
    Type.Day to Offset.Days::class,
    Type.Week to Offset.Weeks::class,
    Type.Weekend to Offset.Weekends::class,
    Type.Month to Offset.Months::class,
    Type.Year to Offset.Years::class
  )

  /**
   * A map of date unit types to [DateOrdinal] k-classes.
   */
  val toDateOrdinalClasses: Map<Type, KClass<*>> = mapOf(
    Type.Day to DateOrdinal.Day::class,
    Type.Week to DateOrdinal.Week::class,
    Type.Weekend to DateOrdinal.Weekend::class,
    Type.Month to DateOrdinal.Month::class,
    Type.Year to DateOrdinal.Year::class
  )
}
