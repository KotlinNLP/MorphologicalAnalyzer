/* Copyright 2017-present The KotlinNLP Authors. All Rights Reserved.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 * ------------------------------------------------------------------*/

package com.kotlinnlp.morphologicalanalyzer.dictionary.compressor

import com.kotlinnlp.linguisticdescription.morphology.properties.MorphologyPropertyFactory

/**
 * A helper that explodes a given temporary entry into more entries.
 *
 * For each morphology property with multiple values (separated by the '+' char) new entries are created copying the
 * given temporary entry and assigning to that morphology all its exploded values in turn.
 *
 * @param tmpEntry a decoded temporary morphology entry
 */
class MorphologyExploder(tmpEntry: TmpEntry) {

  /**
   * The entries exploded from the given tmpEntry.
   */
  var explodedEntries: List<TmpEntry> = listOf(tmpEntry)
    private set

  /**
   * Explode the entry respect to each property name.
   */
  init {
    MorphologyPropertyFactory.propertyNames.forEach { this.explodeEntries(propertyName = it) }
  }

  /**
   * Explode the entries respect to a given [propertyName].
   *
   * @param propertyName the name of a property
   */
  private fun explodeEntries(propertyName: String) {

    val newEntries = mutableListOf<TmpEntry>()

    this.explodedEntries.forEach {
      newEntries.addAll(this.explodeEntry(tmpEntry = it, propertyName = propertyName))
    }

    this.explodedEntries = newEntries
  }

  /**
   * Explode an entry respect to a given [propertyName].
   *
   * @param tmpEntry a temporary entry
   * @param propertyName the name of a property
   */
  private fun explodeEntry(tmpEntry: TmpEntry, propertyName: String): List<TmpEntry> {

    val explodingIndices: Pair<Int, Int>? = this.getExplodingIndices(tmpEntry = tmpEntry, propertyName = propertyName)

    return if (explodingIndices != null)
      this.explodeByProperty(
        tmpEntry = tmpEntry,
        morphoIndex = explodingIndices.first,
        propertyIndex = explodingIndices.second,
        propertyName = propertyName)
    else
      listOf(tmpEntry)
  }

  /**
   * Get the pair of indices (<morphology_index, property_index>) of the first property to explode.
   *
   * @param tmpEntry a temporary entry
   * @param propertyName the name of a property
   *
   * @return a pair of <morphology_index, property_index> or null if no property has to be exploded
   */
  private fun getExplodingIndices(tmpEntry: TmpEntry, propertyName: String): Pair<Int, Int>? {

    tmpEntry.morphologies.forEachIndexed { morphoIndex, morphology ->
      morphology.properties.list.forEachIndexed { propertyIndex, property ->
        val (entryPropName, entryPropValue) = property

        if (entryPropName == propertyName && entryPropValue.contains('+')) {
          return Pair(morphoIndex, propertyIndex)
        }
      }
    }

    return null
  }

  /**
   * Explode an entry respect to a specific morphology property.
   *
   * @param tmpEntry a temporary entry
   * @param morphoIndex the index of a morphology of the entry
   * @param propertyIndex the index of a property of the morphology
   * @param propertyName the name of the property
   *
   * @return a list of exploded entries
   */
  private fun explodeByProperty(tmpEntry: TmpEntry,
                                morphoIndex: Int,
                                propertyIndex: Int,
                                propertyName: String): List<TmpEntry> {

    val newEntries = mutableListOf<TmpEntry>()

    val propertyValue = tmpEntry.morphologies[morphoIndex].properties.list[propertyIndex].second

    propertyValue.split('+').forEach { annotation ->
      newEntries.addAll(
        this.explodeEntry(
          tmpEntry = tmpEntry.replaceProperty(
            morphoIndex = morphoIndex,
            propertyIndex = propertyIndex,
            newValue = annotation),
          propertyName = propertyName
        ))
    }

    return newEntries
  }

  /**
   * Create a new temporary entry from this, replacing the value of a specific morphology property.
   *
   * @param morphoIndex the index of a morphology of the entry
   * @param propertyIndex the index of a property of the morphology
   * @param newValue the new value
   *
   * @return a new temporary entry
   */
  private fun TmpEntry.replaceProperty(morphoIndex: Int, propertyIndex: Int, newValue: String) = this.copy(
    morphologies = this.morphologies.mapIndexed { morphoI, it ->
      if (morphoI == morphoIndex)
        it.copy(
          properties = Properties(it.properties.list.mapIndexed { propI, prop ->
            if (propI == propertyIndex) Pair(prop.first, newValue) else prop
          }))
      else
        it
    }
  )
}
